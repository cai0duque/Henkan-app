package com.example.henkanlists.api

import com.example.henkanlists.ListaHenkan
import com.example.henkanlists.models.User
import retrofit2.Response
import retrofit2.http.*
// Atualizar conforme o necessário

interface ApiService {
    // Endpoint para criar um novo usuário
    @POST("/user")
    suspend fun createUser(@Body user: User): Response<User>

    // Endpoint para buscar uma lista específica pelo ID
    @GET("/list/{ID}")
    suspend fun getList(@Path("ID") id: String): Response<ListaHenkan>

    // Endpoint para buscar todas as listas
    @GET("/list")
    suspend fun getLists(): Response<List<ListaHenkan>>

    // Endpoint para criar uma nova lista
    @POST("/list")
    suspend fun createList(
        @Query("id_owner") idOwner: String,
        @Query("name") name: String,
        @Query("conteudo") conteudo: String
    ): Response<ListaHenkan>

    // Endpoint para atualizar uma lista existente
    @PUT("/list/{id}")
    suspend fun updateList(
        @Path("id") id: String,
        @Query("id_requester") idRequester: String,
        @Query("name") name: String,
        @Query("conteudo") conteudo: String
    ): Response<ListaHenkan>

    // Endpoint para deletar uma lista
    @DELETE("/list/{id}")
    suspend fun deleteList(
        @Path("id") id: String,
        @Query("id_requester") idRequester: String
    ): Response<Void>

    @GET("/user/mac/{mac}")
    suspend fun getUserByMac(@Path("mac") mac: String): Response<User>
}
