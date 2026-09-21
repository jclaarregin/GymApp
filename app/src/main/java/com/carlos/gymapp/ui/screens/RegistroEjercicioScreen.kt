package com.carlos.gymapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.carlos.gymapp.data.RegistroSerie
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroEjercicioScreen(
    app: GymApplication,
    rutinaEjercicioId: Long,
    nombreEjercicio: String,
    navController: NavHostController
) {
    val scope = rememberCoroutineScope()
    val historial by app.database.registroSerieDao().obtenerHistorial(rutinaEjercicioId).collectAsState(initial = emptyList())
    var peso by remember { mutableStateOf("") }
    var reps by remember { mutableStateOf("") }

    val ultimo = historial.firstOrNull()
    val formato = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    Scaffold(topBar = { TopAppBar(title = { Text(nombreEjercicio) }) }) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            if (ultimo != null) {
                Text(
                    "Última vez: ${ultimo.peso} kg × ${ultimo.repeticiones} reps (${formato.format(Date(ultimo.fecha))})",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
            } else {
                Text("Todavía no registraste este ejercicio.")
                Spacer(modifier = Modifier.height(8.dp))
            }

            Row {
                OutlinedTextField(
                    value = peso,
                    onValueChange = { peso = it },
                    label = { Text("Peso (kg)") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = reps,
                    onValueChange = { reps = it },
                    label = { Text("Repeticiones") },
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                val pesoNum = peso.toDoubleOrNull()
                val repsNum = reps.toIntOrNull()
                if (pesoNum != null && repsNum != null) {
                    scope.launch {
                        app.database.registroSerieDao().insertar(
                            RegistroSerie(
                                rutinaEjercicioId = rutinaEjercicioId,
                                fecha = System.currentTimeMillis(),
                                serieNumero = historial.size + 1,
                                peso = pesoNum,
                                repeticiones = repsNum
                            )
                        )
                        peso = ""
                        reps = ""
                    }
                }
            }) { Text("Registrar serie") }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Historial", style = MaterialTheme.typography.titleMedium)
            LazyColumn {
                items(historial) { registro ->
                    ListItem(
                        headlineContent = { Text("${registro.peso} kg × ${registro.repeticiones} reps") },
                        supportingContent = { Text(formato.format(Date(registro.fecha))) }
                    )
                }
            }
        }
    }
}
