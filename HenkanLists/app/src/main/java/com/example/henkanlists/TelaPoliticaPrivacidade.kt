// TelaPoliticaPrivacidade.kt
package com.example.henkanlists

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.henkanlists.ui.theme.AmareloPastel

@Composable
fun TelaPoliticaPrivacidade(controladorNavegacao: NavHostController) {
    Scaffold(
        topBar = { BarraSuperiorPolitica(controladorNavegacao) },
        content = { paddingValues ->
            ConteudoPolitica(modifier = Modifier.padding(paddingValues))
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraSuperiorPolitica(controladorNavegacao: NavHostController) {
    TopAppBar(
        title = { Text("Política de Privacidade") },
        navigationIcon = {
            IconButton(onClick = { controladorNavegacao.popBackStack() }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Voltar")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = AmareloPastel)
    )
}

@Composable
fun ConteudoPolitica(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Esta é a Política de Privacidade da HenkanLists LTDA. Nós respeitamos a sua privacidade e estamos comprometidos em protegê-la.",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
