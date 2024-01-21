package cl.mcortesr.android.ev3_p2.ui

import android.location.Location
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import cl.mcortesr.android.ev3_p2.Aplicacion
import cl.mcortesr.android.ev3_p2.UbicacionRepository
import cl.mcortesr.android.ev3_p2.data.Solicitante
import cl.mcortesr.android.ev3_p2.data.SolicitanteDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListaSolicitanteViewModel(
    private val solicitanteDao: SolicitanteDao,
    private val ubicacionRepository: UbicacionRepository
) : ViewModel() {

    var solicitante by mutableStateOf(listOf<Solicitante>())
    var ubicacion by mutableStateOf<Location?>(null)

    fun refrescarUbicacion(){
        viewModelScope.launch(Dispatchers.IO){
            ubicacion = ubicacionRepository.conseguirUbicacion()
        }
    }

    fun insertarSolicitante(solicitante:Solicitante){
        viewModelScope.launch(Dispatchers.IO) {
            solicitanteDao.insertar(solicitante)
            obtenerSolicitante()
        }
        }


    fun obtenerSolicitante(): List<Solicitante> {
        viewModelScope.launch(Dispatchers.IO) {
            solicitante = solicitanteDao.obtenerTodos()
        }
        return solicitante
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val aplicacion = (this[APPLICATION_KEY] as Aplicacion)
                ListaSolicitanteViewModel(aplicacion.solicitanteDao, aplicacion.ubicacionRepository)
            }
        }
    }


}