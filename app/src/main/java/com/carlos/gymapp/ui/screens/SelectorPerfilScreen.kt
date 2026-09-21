package com.carlos.gymapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.carlos.gymapp.GymApplication
import com.carlos.gymapp.data.Cliente

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectorPerfilScreen(app: GymApplication, navController: NavHostController) {
    val clientes by app.database.clienteDao().obtenerTodos().collectAsState(initial = emptyList())
    var clienteParaPin by remember { mutableStateOf<Cliente?>(null) }
    var pinIngresado by remember { mutableStateOf("") }
    var error by remember { mutableStateOf(false) }

    Scaffold(topBar = { TopAppBar(title = { Text("¿Quién sos?") }) }) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            Button(
                onClick = { navController.navigate("admin_clientes") },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Entrenador (Administrar todo)") }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Clientes", style = MaterialTheme.typography.titleMedium)

            LazyColumn {
                items(clientes) { cliente ->
                    ListItem(
                        headlineContent = { Text("${cliente.nombre} ${cliente.apellido}") },
                        modifier = Modifier.clickable {
                            if (cliente.pin.isNullOrBlank()) {
                                navController.navigate("rutina/${cliente.id}")
                            } else {
                                clienteParaPin = cliente
                                pinIngresado = ""
                                error = false
                            }
                        }
                    )
                }
            }
        }
    }

    clienteParaPin?.let { cliente ->
        AlertDialog(
            onDismissRequest = { clienteParaPin = null },
            title = { Text("PIN de ${cliente.nombre}") },
            text = {
                Column {
                    OutlinedTextField(
                        value = pinIngresado,
                        onValueChange = { pinIngresado = it },
                        label = { Text("PIN") }
                    )
                    if (error) Text("PIN incorrecto", color = MaterialTheme.colorScheme.error)
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (pinIngresado == cliente.pin) {
                        navController.navigate("rutina/${cliente.id}")
                        clienteParaPin = null
                    } else {
                        error = true
                    }
                }) { Text("Entrar") }
            },
            dismissButton = {
                TextButton(onClick = { clienteParaPin = null }) { Text("Cancelar") }
            }
        )
    }
}
