package com.carlos.gymapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.carlos.gymapp.ui.screens.AdminClientesScreen
import com.carlos.gymapp.ui.screens.AdminEjerciciosScreen
import com.carlos.gymapp.ui.screens.DiaRutinaScreen
import com.carlos.gymapp.ui.screens.RegistroEjercicioScreen
import com.carlos.gymapp.ui.screens.RutinaClienteScreen
import com.carlos.gymapp.ui.screens.SelectorPerfilScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as GymApplication
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    GymNavHost(app)
                }
            }
        }
    }
}

@Composable
fun GymNavHost(app: GymApplication) {
    val navController: NavHostController = rememberNavController()
    NavHost(navController = navController, startDestination = "selector") {
        composable("selector") {
            SelectorPerfilScreen(app = app, navController = navController)
        }
        composable("admin_clientes") {
            AdminClientesScreen(app = app, navController = navController)
        }
        composable("admin_ejercicios") {
            AdminEjerciciosScreen(app = app, navController = navController)
        }
        composable(
            "rutina/{clienteId}",
            arguments = listOf(navArgument("clienteId") { type = NavType.LongType })
        ) { backStackEntry ->
            val clienteId = backStackEntry.arguments?.getLong("clienteId") ?: 0L
            RutinaClienteScreen(app = app, clienteId = clienteId, navController = navController)
        }
        composable(
            "dia/{diaId}",
            arguments = listOf(navArgument("diaId") { type = NavType.LongType })
        ) { backStackEntry ->
            val diaId = backStackEntry.arguments?.getLong("diaId") ?: 0L
            DiaRutinaScreen(app = app, diaId = diaId, navController = navController)
        }
        composable(
            "ejercicio/{rutinaEjercicioId}/{nombre}",
            arguments = listOf(
                navArgument("rutinaEjercicioId") { type = NavType.LongType },
                navArgument("nombre") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val reId = backStackEntry.arguments?.getLong("rutinaEjercicioId") ?: 0L
            val nombre = backStackEntry.arguments?.getString("nombre") ?: ""
            RegistroEjercicioScreen(
                app = app,
                rutinaEjercicioId = reId,
                nombreEjercicio = nombre,
                navController = navController
            )
        }
    }
}
