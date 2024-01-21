package cl.mcortesr.android.ev3_p2.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity
data class Solicitante (
    @PrimaryKey(autoGenerate = true) var id:Long? = null,
    var nombre:String,
    var rut:String,
    var fecnac:LocalDate,
    var email:String,
    var telefono:String,
    var lat:Double,
    var lon:Double,
    var imgf:String,
    var imgt:String,
    var feccrea:LocalDate
    )
