package com.carlos.gymapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

data class EjercicioDeDia(
    val rutinaEjercicioId: Long,
    val ejercicioId: Long,
    val nombreEjercicio: String,
    val notas: String,
    val orden: Int
)

@Dao
interface RutinaDao {
    @Query("SELECT * FROM dias_rutina WHERE clienteId = :clienteId ORDER BY orden")
    fun obtenerDiasDeCliente(clienteId: Long): Flow<List<DiaRutina>>

    @Insert
    suspend fun insertarDia(dia: DiaRutina): Long

    @Update
    suspend fun actualizarDia(dia: DiaRutina)

    @Delete
    suspend fun eliminarDia(dia: DiaRutina)

    @Query(
        """
        SELECT re.id AS rutinaEjercicioId, e.id AS ejercicioId, e.nombre AS nombreEjercicio, re.notas AS notas, re.orden AS orden
        FROM rutina_ejercicios re
        INNER JOIN ejercicios e ON e.id = re.ejercicioId
        WHERE re.diaRutinaId = :diaRutinaId
        ORDER BY re.orden
        """
    )
    fun obtenerEjerciciosDeDia(diaRutinaId: Long): Flow<List<EjercicioDeDia>>

    @Insert
    suspend fun agregarEjercicioADia(re: RutinaEjercicio): Long

    @Update
    suspend fun actualizarRutinaEjercicio(re: RutinaEjercicio)

    @Query("DELETE FROM rutina_ejercicios WHERE id = :id")
    suspend fun quitarEjercicioDeDia(id: Long)
}
