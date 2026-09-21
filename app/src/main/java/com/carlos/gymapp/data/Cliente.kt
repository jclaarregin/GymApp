package com.carlos.gymapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clientes")
data class Cliente(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nombre: String,
    val apellido: String,
    val edad: Int,
    val telefono: String,
    val sexo: String, // "M" / "F" / "Otro"
    val pin: String? = null // PIN opcional para que el cliente entre a su propio perfil
)
