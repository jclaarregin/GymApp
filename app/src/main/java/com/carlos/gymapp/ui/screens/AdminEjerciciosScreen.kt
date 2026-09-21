package com.carlos.gymapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import com.carlos.gymapp.data.Ejercicio
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminEjerciciosScreen(app: GymApplication, navController: NavHostController) {
    val scope = rememberCoroutineScope()
    val ejercicios by app.database.ejercicioDao().obtenerTodos().collectAsState(initial = emptyList())
    var ejercicioEditando by remember { mutableStateOf<Ejercicio?>(null) }
    var mostrarDialogo by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Catálogo de ejercicios") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                ejercicioEditando = null
                mostrarDialogo = true
            }) { Icon(Icons.Default.Add, contentDescription = "Agregar ejercicio") }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(ejercicios) { ejercicio ->
                ListItem(
                    headlineContent = { Text(ejercicio.nombre) },
                    supportingContent = { Text(ejercicio.grupoMuscular) },
                    trailingContent = {
                        Row {
                            IconButton(onClick = {
                                ejercicioEditando = ejercicio
                                mostrarDialogo = true
                            }) { Icon(Icons.Default.Edit, contentDescription = "Editar") }
                            IconButton(onClick = {
                                scope.launch { app.database.ejercicioDao().eliminar(ejercicio) }
                            }) { Icon(Icons.Default.Delete, contentDescription = "Eliminar") }
                        }
                    }
                )
            }
        }
    }

    if (mostrarDialogo) {
        DialogoEjercicio(
            ejercicioExistente = ejercicioEditando,
            onDismiss = { mostrarDialogo = false },
            onGuardar = { ejercicio ->
                scope.launch {
                    if (ejercicio.id == 0L) app.database.ejercicioDao().insertar(ejercicio)
                    else app.database.ejercicioDao().actualizar(ejercicio)
                }
                mostrarDialogo = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DialogoEjercicio(
    ejercicioExistente: Ejercicio?,
    onDismiss: () -> Unit,
    onGuardar: (Ejercicio) -> Unit
) {
    var nombre by remember { mutableStateOf(ejercicioExistente?.nombre ?: "") }
    var grupo by remember { mutableStateOf(ejercicioExistente?.grupoMuscular ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (ejercicioExistente == null) "Nuevo ejercicio" else "Editar ejercicio") },
        text = {
            Column {
                OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
                OutlinedTextField(value = grupo, onValueChange = { grupo = it }, label = { Text("Grupo muscular") })
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onGuardar(
                    Ejercicio(
                        id = ejercicioExistente?.id ?: 0L,
                        nombre = nombre,
                        grupoMuscular = grupo
                    )
                )
            }) { Text("Guardar") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}
