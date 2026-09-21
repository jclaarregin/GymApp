package com.carlos.gymapp.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "registros_series",
    foreignKeys = [ForeignKey(
        entity = RutinaEjercicio::class,
        parentColumns = ["id"],
        childColumns = ["rutinaEjercicioId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class RegistroSerie(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val rutinaEjercicioId: Long,
    val fecha: Long, // epoch millis
    val serieNumero: Int,
    val peso: Double,
    val repeticiones: Int
)
