package com.carlos.gymapp.ui.screens

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Button
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.carlos.gymapp.GymApplication
import com.carlos.gymapp.data.Cliente
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminClientesScreen(app: GymApplication, navController: NavHostController) {
    val scope = rememberCoroutineScope()
    val clientes by app.database.clienteDao().obtenerTodos().collectAsState(initial = emptyList())
    var clienteEditando by remember { mutableStateOf<Cliente?>(null) }
    var mostrarDialogo by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Clientes") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                clienteEditando = null
                mostrarDialogo = true
            }) { Icon(Icons.Default.Add, contentDescription = "Agregar cliente") }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Button(
                onClick = { navController.navigate("admin_ejercicios") },
                modifier = Modifier.padding(16.dp)
            ) { Text("Administrar catálogo de ejercicios") }

            LazyColumn {
                items(clientes) { cliente ->
                    ListItem(
                        headlineContent = { Text("${cliente.nombre} ${cliente.apellido}") },
                        supportingContent = { Text("${cliente.edad} años · ${cliente.telefono}") },
                        modifier = Modifier.clickable { navController.navigate("rutina/${cliente.id}") },
                        trailingContent = {
                            Row {
                                IconButton(onClick = {
                                    clienteEditando = cliente
                                    mostrarDialogo = true
                                }) { Icon(Icons.Default.Edit, contentDescription = "Editar") }
                                IconButton(onClick = {
                                    scope.launch { app.database.clienteDao().eliminar(cliente) }
                                }) { Icon(Icons.Default.Delete, contentDescription = "Eliminar") }
                            }
                        }
                    )
                }
            }
        }
    }

    if (mostrarDialogo) {
        DialogoCliente(
            clienteExistente = clienteEditando,
            onDismiss = { mostrarDialogo = false },
            onGuardar = { cliente ->
                scope.launch {
                    if (cliente.id == 0L) app.database.clienteDao().insertar(cliente)
                    else app.database.clienteDao().actualizar(cliente)
                }
                mostrarDialogo = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DialogoCliente(
    clienteExistente: Cliente?,
    onDismiss: () -> Unit,
    onGuardar: (Cliente) -> Unit
) {
    var nombre by remember { mutableStateOf(clienteExistente?.nombre ?: "") }
    var apellido by remember { mutableStateOf(clienteExistente?.apellido ?: "") }
    var edad by remember { mutableStateOf(clienteExistente?.edad?.toString() ?: "") }
    var telefono by remember { mutableStateOf(clienteExistente?.telefono ?: "") }
    var sexo by remember { mutableStateOf(clienteExistente?.sexo ?: "") }
    var pin by remember { mutableStateOf(clienteExistente?.pin ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (clienteExistente == null) "Nuevo cliente" else "Editar cliente") },
        text = {
            Column {
                OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
                OutlinedTextField(value = apellido, onValueChange = { apellido = it }, label = { Text("Apellido") })
                OutlinedTextField(value = edad, onValueChange = { edad = it }, label = { Text("Edad") })
                OutlinedTextField(value = telefono, onValueChange = { telefono = it }, label = { Text("Teléfono") })
                OutlinedTextField(value = sexo, onValueChange = { sexo = it }, label = { Text("Sexo") })
                OutlinedTextField(value = pin, onValueChange = { pin = it }, label = { Text("PIN (opcional)") })
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onGuardar(
                    Cliente(
                        id = clienteExistente?.id ?: 0L,
                        nombre = nombre,
                        apellido = apellido,
                        edad = edad.toIntOrNull() ?: 0,
                        telefono = telefono,
                        sexo = sexo,
                        pin = pin.ifBlank { null }
                    )
                )
            }) { Text("Guardar") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}
