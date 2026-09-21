package com.carlos.gymapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.carlos.gymapp.GymApplication
import com.carlos.gymapp.data.RutinaEjercicio
import kotlinx.coroutines.launch
import java.net.URLEncoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaRutinaScreen(app: GymApplication, diaId: Long, navController: NavHostController) {
    val scope = rememberCoroutineScope()
    val ejerciciosDia by app.database.rutinaDao().obtenerEjerciciosDeDia(diaId).collectAsState(initial = emptyList())
    val catalogo by app.database.ejercicioDao().obtenerTodos().collectAsState(initial = emptyList())
    var mostrarSelector by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Ejercicios del día") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { mostrarSelector = true }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar ejercicio")
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(ejerciciosDia) { ed ->
                ListItem(
                    headlineContent = { Text(ed.nombreEjercicio) },
                    supportingContent = { if (ed.notas.isNotBlank()) Text(ed.notas) },
                    modifier = Modifier.clickable {
                        val nombreCodificado = URLEncoder.encode(ed.nombreEjercicio, "UTF-8")
                        navController.navigate("ejercicio/${ed.rutinaEjercicioId}/$nombreCodificado")
                    },
                    trailingContent = {
                        IconButton(onClick = {
                            scope.launch { app.database.rutinaDao().quitarEjercicioDeDia(ed.rutinaEjercicioId) }
                        }) { Icon(Icons.Default.Delete, contentDescription = "Quitar") }
                    }
                )
            }
        }
    }

    if (mostrarSelector) {
        AlertDialog(
            onDismissRequest = { mostrarSelector = false },
            title = { Text("Elegí un ejercicio del catálogo") },
            text = {
                LazyColumn {
                    items(catalogo) { ejercicio ->
                        ListItem(
                            headlineContent = { Text(ejercicio.nombre) },
                            modifier = Modifier.clickable {
                                scope.launch {
                                    app.database.rutinaDao().agregarEjercicioADia(
                                        RutinaEjercicio(
                                            diaRutinaId = diaId,
                                            ejercicioId = ejercicio.id,
                                            orden = ejerciciosDia.size
                                        )
                                    )
                                }
                                mostrarSelector = false
                            }
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { mostrarSelector = false }) { Text("Cerrar") }
            }
        )
    }
}
