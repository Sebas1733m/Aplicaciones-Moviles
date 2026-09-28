package com.example.myapplication33

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    var nombre by remember { mutableStateOf("") }
    var matricula by remember { mutableStateOf("") }
    var asignatura by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }

    var expandedMenu by remember { mutableStateOf(false) }
    var showTime by remember { mutableStateOf(false) }
    var showDate by remember { mutableStateOf(false) }
    var mostrarDatos by remember { mutableStateOf(false) }

    val errorNombre = nombre.isNotEmpty() && !nombre.all { it.isLetter() || it.isWhitespace() }
    val errorMatricula = matricula.isNotEmpty() && !matricula.all { it.isDigit() }
    val opcionesAsignatura = listOf("Aplicaciones Web", "Estructura de Datos", "Aplicaciones Móviles", "Inglés")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 50.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it; mostrarDatos = false },
            label = { Text("Nombre") },
            isError = errorNombre,
            supportingText = { if (errorNombre) Text("Tipo de dato incorrecto. Ingrese solo letras.") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = matricula,
            onValueChange = { matricula = it; mostrarDatos = false },
            label = { Text("Matrícula") },
            isError = errorMatricula,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), // Teclado numérico
            supportingText = { if (errorMatricula) Text("Tipo de dato incorrecto. Ingrese solo números.") },
            modifier = Modifier.fillMaxWidth()
        )

        ExposedDropdownMenuBox(
            expanded = expandedMenu,
            onExpandedChange = { expandedMenu = it }
        ) {
            OutlinedTextField(
                value = asignatura,
                onValueChange = {},
                readOnly = true,
                label = { Text("Asignatura") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedMenu) },
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(expanded = expandedMenu, onDismissRequest = { expandedMenu = false }) {
                opcionesAsignatura.forEach { op ->
                    DropdownMenuItem(
                        text = { Text(op) },
                        onClick = { asignatura = op; expandedMenu = false; mostrarDatos = false }
                    )
                }
            }
        }

        OutlinedButton(onClick = { showTime = true }, modifier = Modifier.fillMaxWidth()) {
            Text(if (hora.isEmpty()) "Seleccionar Hora" else "Hora seleccionada: $hora")
        }

        OutlinedButton(onClick = { showDate = true }, modifier = Modifier.fillMaxWidth()) {
            Text(if (fecha.isEmpty()) "Seleccionar Fecha" else "Fecha seleccionada: $fecha")
        }

        Button(
            onClick = {
                if (!errorNombre && !errorMatricula && nombre.isNotBlank() && matricula.isNotBlank() && asignatura.isNotBlank()) {
                    mostrarDatos = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar")
        }

        // Resultados en pantalla con formato JSON
        // Resultados en pantalla (formato de lista normal)
        if (mostrarDatos) {
            Card(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                Text(
                    text = "Datos Registrados:\n\n" +
                            "Nombre: $nombre\n" +
                            "Matrícula: $matricula\n" +
                            "Asignatura: $asignatura\n" +
                            "Hora: $hora\n" +
                            "Fecha: $fecha",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }

    if (showTime) {
        val timeState = rememberTimePickerState()
        AlertDialog(
            onDismissRequest = { showTime = false },
            confirmButton = {
                TextButton(onClick = { hora = String.format("%02d:%02d", timeState.hour, timeState.minute); showTime = false }) { Text("OK") }
            },
            text = { TimePicker(timeState) }
        )
    }

    if (showDate) {
        val dateState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDate = false },
            confirmButton = {
                TextButton(onClick = {
                    dateState.selectedDateMillis?.let {
                        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply { timeZone = TimeZone.getTimeZone("UTC") }
                        fecha = format.format(Date(it))
                    }
                    showDate = false
                }) { Text("OK") }
            }
        ) { DatePicker(dateState) }
    }
}