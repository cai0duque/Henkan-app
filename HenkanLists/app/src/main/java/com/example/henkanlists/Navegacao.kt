// Navegacao.kt
package com.example.henkanlists

import java.util.UUID

sealed class Tela(val rota: String) {
    object Inicial : Tela("inicial")
    object CriarLista : Tela("criar_lista") {
        fun criarRota(listaId: UUID?) = if (listaId != null) "criar_lista?listaId=$listaId" else "criar_lista"
    }
    object Opcoes : Tela("opcoes")
    object PoliticaPrivacidade : Tela("politica_privacidade")
}
