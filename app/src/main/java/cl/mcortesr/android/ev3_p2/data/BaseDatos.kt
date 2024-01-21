package cl.mcortesr.android.ev3_p2.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Solicitante::class], version = 1)
@TypeConverters(LocalDateConverter::class)

abstract class BaseDatos : RoomDatabase() {
    abstract  fun SolicitanteDao(): SolicitanteDao
}