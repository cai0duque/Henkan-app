// ListaHenkan.kt
package com.example.henkanlists

import java.util.UUID
import java.util.Date

data class ListaHenkan(
    val id: UUID = UUID.randomUUID(),
    val titulo: String,
    val conteudo: String,
    val idOwner: String,
    val dataCriacao: Date,
    var dataModificacao: Date = dataCriacao
)
