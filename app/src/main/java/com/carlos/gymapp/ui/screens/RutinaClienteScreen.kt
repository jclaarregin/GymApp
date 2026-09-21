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
import androidx.compose.material3.OutlinedTextField
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
import com.carlos.gymapp.data.DiaRutina
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RutinaClienteScreen(app: GymApplication, clienteId: Long, navController: NavHostController) {
    val scope = rememberCoroutineScope()
    val dias by app.database.rutinaDao().obtenerDiasDeCliente(clienteId).collectAsState(initial = emptyList())
    var mostrarDialogo by remember { mutableStateOf(false) }
    var nombreNuevoDia by remember { mutableStateOf("") }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Rutina") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                nombreNuevoDia = ""
                mostrarDialogo = true
            }) { Icon(Icons.Default.Add, contentDescription = "Agregar día") }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(dias) { dia ->
                ListItem(
                    headlineContent = { Text(dia.nombre) },
                    modifier = Modifier.clickable { navController.navigate("dia/${dia.id}") },
                    trailingContent = {
                        IconButton(onClick = {
                            scope.launch { app.database.rutinaDao().eliminarDia(dia) }
                        }) { Icon(Icons.Default.Delete, contentDescription = "Eliminar día") }
                    }
                )
            }
        }
    }

    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = { Text("Nuevo día de rutina") },
            text = {
                OutlinedTextField(
                    value = nombreNuevoDia,
                    onValueChange = { nombreNuevoDia = it },
                    label = { Text("Nombre (ej. Día 1 Pecho + Tríceps)") }
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    scope.launch {
                        app.database.rutinaDao().insertarDia(
                            DiaRutina(clienteId = clienteId, nombre = nombreNuevoDia, orden = dias.size)
                        )
                    }
                    mostrarDialogo = false
                }) { Text("Guardar") }
            },
            dismissButton = { TextButton(onClick = { mostrarDialogo = false }) { Text("Cancelar") } }
        )
    }
}
