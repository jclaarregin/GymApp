package com.carlos.gymapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RegistroSerieDao {
    @Insert
    suspend fun insertar(registro: RegistroSerie): Long

    @Query("SELECT * FROM registros_series WHERE rutinaEjercicioId = :rutinaEjercicioId ORDER BY fecha DESC")
    fun obtenerHistorial(rutinaEjercicioId: Long): Flow<List<RegistroSerie>>

    @Query(
        """
        SELECT * FROM registros_series
        WHERE rutinaEjercicioId = :rutinaEjercicioId
        ORDER BY fecha DESC LIMIT :limite
        """
    )
    suspend fun obtenerUltimos(rutinaEjercicioId: Long, limite: Int = 3): List<RegistroSerie>

    @Delete
    suspend fun eliminar(registro: RegistroSerie)
}
