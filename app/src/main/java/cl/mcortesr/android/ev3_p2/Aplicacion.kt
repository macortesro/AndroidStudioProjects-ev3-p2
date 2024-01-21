package cl.mcortesr.android.ev3_p2

import android.app.Application
import androidx.room.Room
import cl.mcortesr.android.ev3_p2.data.BaseDatos
import com.google.android.gms.location.LocationServices

class Aplicacion : Application() {

    val db by lazy { Room.databaseBuilder(this, BaseDatos::class.java, "solicitante.db").build() }
    val solicitanteDao by lazy { db.SolicitanteDao() }
    val locationServices by lazy { LocationServices.getFusedLocationProviderClient(this)}
    val ubicacionRepository by lazy {UbicacionRepository(locationServices)}

}