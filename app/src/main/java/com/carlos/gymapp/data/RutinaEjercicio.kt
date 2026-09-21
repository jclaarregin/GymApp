package com.carlos.gymapp.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "rutina_ejercicios",
    foreignKeys = [
        ForeignKey(entity = DiaRutina::class, parentColumns = ["id"], childColumns = ["diaRutinaId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Ejercicio::class, parentColumns = ["id"], childColumns = ["ejercicioId"], onDelete = ForeignKey.CASCADE)
    ]
)
data class RutinaEjercicio(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val diaRutinaId: Long,
    val ejercicioId: Long,
    val orden: Int = 0,
    val notas: String = "" // ej. "3 series x 10 reps"
)
