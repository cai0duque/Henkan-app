package com.example.henkanlists

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.henkanlists.ui.theme.AmareloPastel
import com.example.henkanlists.ui.theme.VerdeClaro
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.text.input.TextFieldValue
import java.util.UUID

@Composable
fun TelaInicial(controladorNavegacao: NavHostController, henkanViewModel: HenkanViewModel) {
    Scaffold(
        topBar = { BarraSuperiorInicial(controladorNavegacao) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    controladorNavegacao.navigate(Tela.CriarLista.criarRota(null))
                },
                containerColor = VerdeClaro
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Adicionar")
            }
        },
        content = { paddingValues ->
            ConteudoInicial(
                controladorNavegacao = controladorNavegacao,
                henkanViewModel = henkanViewModel,
                modifier = Modifier.padding(paddingValues)
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraSuperiorInicial(controladorNavegacao: NavHostController) {
    TopAppBar(
        title = { Text("Henkan Lists") },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = AmareloPastel),
        actions = {
            TextButton(
                onClick = { controladorNavegacao.navigate(Tela.Opcoes.rota) },
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onPrimary)
            ) {
                Text("Opções")
            }
        }
    )
}

@Composable
fun ConteudoInicial(
    controladorNavegacao: NavHostController,
    henkanViewModel: HenkanViewModel,
    modifier: Modifier = Modifier
) {
    var textoPesquisa by remember { mutableStateOf(TextFieldValue("")) }
    val listas by henkanViewModel.listas.collectAsState()

    val listasFiltradas = listas.filter {
        it.titulo.contains(textoPesquisa.text, ignoreCase = true)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Barra de Pesquisa
        OutlinedTextField(
            value = textoPesquisa,
            onValueChange = { textoPesquisa = it },
            placeholder = { Text("Pesquisar...") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (listasFiltradas.isEmpty()) {
            // Mensagem de Boas-Vindas
            Text(
                "BEM-VINDO! Você ainda não tem nenhuma lista!",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            // Exibir Listas
            listasFiltradas.forEach { listaHenkan ->
                ItemLista(
                    listaHenkan = listaHenkan,
                    aoClicar = {
                        controladorNavegacao.navigate(Tela.CriarLista.criarRota(listaHenkan.id))
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun ItemLista(listaHenkan: ListaHenkan, aoClicar: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = aoClicar),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(listaHenkan.titulo, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(listaHenkan.conteudo)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                listaHenkan.dataCriacao.toString(),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}
