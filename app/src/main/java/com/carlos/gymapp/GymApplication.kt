package com.carlos.gymapp

import android.app.Application
import com.carlos.gymapp.data.AppDatabase

class GymApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.obtenerInstancia(this) }
}
