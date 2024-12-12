// TelaOpcoes.kt
package com.example.henkanlists

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.UnfoldMore
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.henkanlists.ui.theme.AmareloPastel
import com.example.henkanlists.HenkanViewModel

@Composable
fun TelaOpcoes(
    controladorNavegacao: NavHostController,
    henkanViewModel: HenkanViewModel
) {
    Scaffold(
        topBar = { BarraSuperiorOpcoes(controladorNavegacao) },
        content = { paddingValues ->
            ConteudoOpcoes(
                henkanViewModel = henkanViewModel,
                controladorNavegacao = controladorNavegacao,
                modifier = Modifier.padding(paddingValues)
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraSuperiorOpcoes(controladorNavegacao: NavHostController) {
    TopAppBar(
        title = { Text("Henkan Lists") },
        navigationIcon = {
            IconButton(onClick = { controladorNavegacao.popBackStack() }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Voltar")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = AmareloPastel)
    )
}

@Composable
fun ConteudoOpcoes(
    henkanViewModel: HenkanViewModel,
    controladorNavegacao: NavHostController,
    modifier: Modifier = Modifier
) {
    var expandido by remember { mutableStateOf(false) }
    val opcaoOrdenacao by henkanViewModel.opcaoOrdenacao.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Estilo", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        // Botão "Ordenar por"
        Button(
            onClick = { expandido = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ordenar por ${opcaoOrdenacao.nome}")
            Spacer(modifier = Modifier.weight(1f))
            Icon(Icons.Filled.UnfoldMore, contentDescription = "Opções de Ordenação")
        }
        DropdownMenu(
            expanded = expandido,
            onDismissRequest = { expandido = false }
        ) {
            DropdownMenuItem(
                text = { Text("Data de Modificação") },
                onClick = {
                    henkanViewModel.definirOpcaoOrdenacao(OpcaoOrdenacao.DataModificacao)
                    expandido = false
                }
            )
            DropdownMenuItem(
                text = { Text("Alfabética") },
                onClick = {
                    henkanViewModel.definirOpcaoOrdenacao(OpcaoOrdenacao.Alfabetica)
                    expandido = false
                }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Outros", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        // Botão "Política de Privacidade"
        Button(
            onClick = { controladorNavegacao.navigate(Tela.PoliticaPrivacidade.rota) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Política de Privacidade")
            Spacer(modifier = Modifier.weight(1f))
            Icon(Icons.Filled.ArrowForward, contentDescription = "Política de Privacidade")
        }
    }
}
