package com.carlos.gymapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface EjercicioDao {
    @Query("SELECT * FROM ejercicios ORDER BY nombre")
    fun obtenerTodos(): Flow<List<Ejercicio>>

    @Insert
    suspend fun insertar(ejercicio: Ejercicio): Long

    @Update
    suspend fun actualizar(ejercicio: Ejercicio)

    @Delete
    suspend fun eliminar(ejercicio: Ejercicio)
}
