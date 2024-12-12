// HenkanViewModel.kt
package com.example.henkanlists

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.henkanlists.api.ApiClient
import com.example.henkanlists.api.ApiService
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import com.example.henkanlists.models.User

enum class OpcaoOrdenacao(val nome: String) {
    DataModificacao("Data de Modificação"),
    Alfabetica("Alfabética")
}

class HenkanViewModel : ViewModel() {
    private val apiService = ApiClient.retrofit.create(ApiService::class.java)

    private val _listas = MutableStateFlow<List<ListaHenkan>>(emptyList())
    val listas = _listas.asStateFlow()

    private val _opcaoOrdenacao = MutableStateFlow(OpcaoOrdenacao.DataModificacao)
    val opcaoOrdenacao = _opcaoOrdenacao.asStateFlow()

    private lateinit var mSocket: Socket

    // Initialize the listeners before the 'init' block
    private val onListCreated = Emitter.Listener { args ->
        if (args.isNotEmpty()) {
            val data = args[0] as JSONObject
            val listaHenkan = parseListaHenkanFromJson(data)
            viewModelScope.launch {
                adicionarOuAtualizarListaLocal(listaHenkan)
            }
        }
    }

    private val onListUpdated = Emitter.Listener { args ->
        if (args.isNotEmpty()) {
            val data = args[0] as JSONObject
            val listaHenkan = parseListaHenkanFromJson(data)
            viewModelScope.launch {
                adicionarOuAtualizarListaLocal(listaHenkan)
            }
        }
    }

    private val onListDeleted = Emitter.Listener { args ->
        if (args.isNotEmpty()) {
            val data = args[0] as JSONObject
            val idList = data.getString("id_list")
            viewModelScope.launch {
                removerListaLocalById(idList)
            }
        }
    }

    init {
        try {
            mSocket = IO.socket("http://192.168.4.105:80")
            mSocket.on(Socket.EVENT_CONNECT) {
                Log.d("SocketIO", "Connected to Socket.IO server")
            }
            mSocket.on(Socket.EVENT_DISCONNECT) {
                Log.d("SocketIO", "Disconnected from Socket.IO server")
            }
            mSocket.on("listCreated", onListCreated)
            mSocket.on("listUpdated", onListUpdated)
            mSocket.on("listDeleted", onListDeleted)
            mSocket.connect()
        } catch (e: Exception) {
            Log.e("SocketIO", "Error initializing Socket.IO: ${e.message}")
        }
    }

    private fun parseListaHenkanFromJson(json: JSONObject): ListaHenkan {
        val id = UUID.fromString(json.getString("id_list"))
        val titulo = json.getString("name")
        val conteudo = json.optString("conteudo", "")
        val idOwner = json.getString("id_owner")
        val dataCriacaoStr = json.getString("data_criacao")
        val dataModificacaoStr = json.getString("data_modificacao")

        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())

        val dataCriacao = formatter.parse(dataCriacaoStr) ?: Date()
        val dataModificacao = formatter.parse(dataModificacaoStr) ?: Date()

        return ListaHenkan(id, titulo, conteudo, idOwner, dataCriacao, dataModificacao)
    }

    private fun adicionarOuAtualizarListaLocal(listaHenkan: ListaHenkan) {
        val listasAtuais = _listas.value.toMutableList()
        val indice = listasAtuais.indexOfFirst { it.id == listaHenkan.id }

        if (indice >= 0) {
            listasAtuais[indice] = listaHenkan
        } else {
            listasAtuais.add(listaHenkan)
        }
        _listas.value = listasAtuais
        ordenarListas()
    }

    private fun removerListaLocalById(id: String) {
        val listasAtuais = _listas.value.toMutableList()
        listasAtuais.removeAll { it.id.toString() == id }
        _listas.value = listasAtuais
        ordenarListas()
    }

    // Define the 'ordenarListas' function
    private fun ordenarListas() {
        _listas.value = when (_opcaoOrdenacao.value) {
            OpcaoOrdenacao.DataModificacao -> _listas.value.sortedByDescending { it.dataModificacao }
            OpcaoOrdenacao.Alfabetica -> _listas.value.sortedBy { it.titulo }
        }
    }

    fun definirOpcaoOrdenacao(opcao: OpcaoOrdenacao) {
        _opcaoOrdenacao.value = opcao
        ordenarListas()
    }

    fun adicionarOuAtualizarLista(listaHenkan: ListaHenkan) {
        val listasAtuais = _listas.value.toMutableList()
        val indice = listasAtuais.indexOfFirst { it.id == listaHenkan.id }

        if (indice >= 0) {
            listasAtuais[indice] = listaHenkan
        } else {
            listasAtuais.add(listaHenkan)
        }
        _listas.value = listasAtuais
        ordenarListas()

        // Atualiza ou cria a lista no backend
        viewModelScope.launch {
            try {
                if (indice >= 0) {
                    // Atualizando
                    apiService.updateList(
                        id = listaHenkan.id.toString(),
                        idRequester = listaHenkan.idOwner,
                        name = listaHenkan.titulo,
                        conteudo = listaHenkan.conteudo
                    )
                } else {
                    // Criando
                    apiService.createList(
                        idOwner = listaHenkan.idOwner,
                        name = listaHenkan.titulo,
                        conteudo = listaHenkan.conteudo
                    )
                }
            } catch (e: Exception) {
                Log.e("Error", "Erro ao sincronizar lista com o backend: ${e.message}")
            }
        }
    }

    fun obterOuCriarUsuario(nome: String, macAddress: String, onResult: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = apiService.getUserByMac(macAddress)
                if (response.isSuccessful && response.body() != null) {
                    val user = response.body()!!
                    onResult(user.id_user!!)
                } else {
                    // User not found, create new user
                    val newUser = User(id_user = null, name = nome, mac = macAddress)
                    val createResponse = apiService.createUser(newUser)
                    if (createResponse.isSuccessful && createResponse.body() != null) {
                        val createdUser = createResponse.body()!!
                        onResult(createdUser.id_user!!)
                    } else {
                        Log.e("Error", "Failed to create user: ${createResponse.errorBody()?.string()}")
                    }
                }
            } catch (e: Exception) {
                Log.e("Error", "Error in obterOuCriarUsuario: ${e.message}")
            }
        }
    }

    fun removerLista(listaHenkan: ListaHenkan) {
        _listas.value = _listas.value.filterNot { it.id == listaHenkan.id }

        // Remove a lista do backend
        viewModelScope.launch {
            try {
                apiService.deleteList(
                    id = listaHenkan.id.toString(),
                    idRequester = listaHenkan.idOwner
                )
            } catch (e: Exception) {
                Log.e("Error", "Erro ao remover lista no backend: ${e.message}")
            }
        }
    }

    fun carregarListas() {
        // Busca listas do backend
        viewModelScope.launch {
            try {
                val response = apiService.getLists()
                if (response.isSuccessful) {
                    _listas.value = response.body() ?: emptyList()
                    ordenarListas()
                } else {
                    Log.e("Error", "Erro ao carregar listas: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("Error", "Erro ao buscar listas no backend: ${e.message}")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        mSocket.disconnect()
    }
}
