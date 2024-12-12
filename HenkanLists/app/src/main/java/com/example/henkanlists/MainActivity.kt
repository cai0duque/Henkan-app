package com.example.henkanlists

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import com.example.henkanlists.ui.theme.HenkanListsTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AplicativoHenkanLists()
        }
    }
}

@Composable
fun AplicativoHenkanLists() {
    HenkanListsTheme {
        val controladorNavegacao = rememberNavController()
        val henkanViewModel: HenkanViewModel = viewModel()

        // Variável para armazenar o ID do proprietário
        var idOwner by remember { mutableStateOf("") }

        // Obter ou criar o usuário ao inicializar
        LaunchedEffect(Unit) {
            henkanViewModel.obterOuCriarUsuario("Nome do Usuário", "00:11:22:33:44:55") { id ->
                idOwner = id
            }
        }

        // Carregar listas ao iniciar
        henkanViewModel.carregarListas()

        NavHost(
            navController = controladorNavegacao,
            startDestination = Tela.Inicial.rota
        ) {
            composable(Tela.Inicial.rota) {
                TelaInicial(controladorNavegacao, henkanViewModel)
            }
            composable(
                route = "criar_lista?listaId={listaId}",
                arguments = listOf(
                    navArgument("listaId") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    }
                )
            ) { backStackEntry ->
                val listaId = backStackEntry.arguments?.getString("listaId")
                val listaExistente = listaId?.let { id ->
                    henkanViewModel.listas.value.find { it.id.toString() == id }
                }
                TelaCriarLista(controladorNavegacao, henkanViewModel, listaExistente, idOwner)
            }
            composable(Tela.Opcoes.rota) {
                TelaOpcoes(controladorNavegacao, henkanViewModel)
            }
            composable(Tela.PoliticaPrivacidade.rota) {
                TelaPoliticaPrivacidade(controladorNavegacao)
            }
        }
    }
}
