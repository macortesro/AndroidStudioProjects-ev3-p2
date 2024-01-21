package cl.mcortesr.android.ev3_p2.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface SolicitanteDao {

    @Query("SELECT * FROM Solicitante ORDER BY feccrea DESC")
    suspend fun obtenerTodos(): List<Solicitante>

    @Query("SELECT * FROM Solicitante WHERE id = :id")
    suspend fun obtenerPorId(id:Long): Solicitante

    @Insert
    suspend fun insertar(solicitante: Solicitante)

    @Update
    suspend fun modificar(solicitante: Solicitante)

    @Delete
    suspend fun eliminar(solicitante: Solicitante)
}
