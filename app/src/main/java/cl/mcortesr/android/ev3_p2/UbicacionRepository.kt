package cl.mcortesr.android.ev3_p2

import android.annotation.SuppressLint
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await

class UbicacionRepository (
    val fusedLocationProviderClient: FusedLocationProviderClient
){


    @OptIn(ExperimentalCoroutinesApi::class)
    @SuppressLint("MissingPermission")
    suspend fun conseguirUbicacion(): Location {
        val cancellationTokenSource = CancellationTokenSource()
        val task = fusedLocationProviderClient.getCurrentLocation(
            Priority.PRIORITY_BALANCED_POWER_ACCURACY,
            cancellationTokenSource.token
        )
        return task.await(cancellationTokenSource)
    }


    @SuppressLint("MissingPermission")
    fun conseguirUbicacion(
        onExito:(u: Location) -> Unit,
        onError:(e:Exception) -> Unit
    ){
        val tarea = fusedLocationProviderClient.getCurrentLocation(
            Priority.PRIORITY_BALANCED_POWER_ACCURACY,
            null
        )
        tarea.addOnSuccessListener { onExito(it)  }
        tarea.addOnFailureListener { onError(it) }
    }

}