@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.henkanlists

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.henkanlists.ui.theme.AmareloPastel
import com.example.henkanlists.ui.theme.VerdeClaro
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import com.example.henkanlists.HenkanViewModel
import java.util.*

@Composable
fun TelaCriarLista(
    controladorNavegacao: NavHostController,
    henkanViewModel: HenkanViewModel,
    listaExistente: ListaHenkan? = null,
    idOwner: String // Novo parâmetro para o ID do proprietário
) {
    var textoTitulo by remember { mutableStateOf(listaExistente?.titulo ?: "") }
    var textoConteudo by remember { mutableStateOf(listaExistente?.conteudo ?: "") }

    Scaffold(
        topBar = {
            BarraSuperiorCriarLista(
                controladorNavegacao,
                aoSalvar = {
                    val dataAtual = Date()
                    val lista = listaExistente?.copy(
                        titulo = textoTitulo,
                        conteudo = textoConteudo,
                        dataModificacao = dataAtual
                    ) ?: ListaHenkan(
                        titulo = textoTitulo,
                        conteudo = textoConteudo,
                        idOwner = idOwner, // Passa o ID do proprietário
                        dataCriacao = dataAtual,
                        dataModificacao = dataAtual
                    )
                    henkanViewModel.adicionarOuAtualizarLista(lista)
                    controladorNavegacao.popBackStack()
                },
                aoDeletar = listaExistente?.let {
                    {
                        henkanViewModel.removerLista(it)
                        controladorNavegacao.popBackStack()
                    }
                }
            )
        },
        content = { paddingValues ->
            ConteudoCriarLista(
                textoTitulo = textoTitulo,
                aoAlterarTitulo = { textoTitulo = it },
                textoConteudo = textoConteudo,
                aoAlterarConteudo = { textoConteudo = it },
                modifier = Modifier.padding(paddingValues)
            )
        }
    )
}

@Composable
fun BarraSuperiorCriarLista(
    controladorNavegacao: NavHostController,
    aoSalvar: () -> Unit,
    aoDeletar: (() -> Unit)? = null
) {
    TopAppBar(
        title = { Text("Henkan Lists") },
        navigationIcon = {
            IconButton(onClick = { controladorNavegacao.popBackStack() }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Voltar")
            }
        },
        actions = {
            if (aoDeletar != null) {
                IconButton(onClick = aoDeletar) {
                    Icon(Icons.Filled.Delete, contentDescription = "Deletar")
                }
            }
            IconButton(onClick = aoSalvar) {
                Icon(Icons.Filled.Check, contentDescription = "Salvar")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = AmareloPastel)
    )
}

@Composable
fun ConteudoCriarLista(
    textoTitulo: String,
    aoAlterarTitulo: (String) -> Unit,
    textoConteudo: String,
    aoAlterarConteudo: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val contadorCaracteres = textoConteudo.length
    val dataAtual = Date()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Campo de Título
        OutlinedTextField(
            value = textoTitulo,
            onValueChange = aoAlterarTitulo,
            placeholder = { Text("Título...") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        // Data e Contador de Caracteres
        Text("$dataAtual | $contadorCaracteres caracteres")
        Spacer(modifier = Modifier.height(8.dp))
        // Campo de Conteúdo
        OutlinedTextField(
            value = textoConteudo,
            onValueChange = aoAlterarConteudo,
            placeholder = { Text("Comece a escrever...") },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = VerdeClaro
            )
        )
    }
}
