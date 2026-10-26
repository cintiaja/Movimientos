package com.example.movimientos.ui.Registro

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.room.Room
import com.example.movimientos.ui.theme.MovimientosTheme
import kotlinx.coroutines.launch

@Composable
fun RegistroScreen() {
    var codigo by remember { mutableStateOf("") }
    var cantidads by remember { mutableStateOf("") }


    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val db = remember {
        Room.databaseBuilder(context, AppDatabase::class.java, "stockmanager.db").build()
    }

    val movimientos by db.movimientoDao().obtenerTodos().collectAsState(initial = emptyList())

    Column(
        modifier = Modifier.padding(100.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("REGISTRO")
        Spacer(Modifier.height(20.dp))
        Text("Codigo del Producto")
        TextField(
            value = codigo,
            onValueChange = { codigo = it },
            label = { Text("Escribe aqui") }
        )
        Spacer(Modifier.height(40.dp))
        Text("Cantidad")
        TextField(
            value = cantidads,
            onValueChange = { cantidads = it },
            label = { Text("Escribe aqui") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = {
            val cantidadNum = cantidads.toIntOrNull()
            if (codigo.isNotBlank() && cantidadNum != null) {
                scope.launch {
                    db.movimientoDao().insertar(
                        Movimiento(codigoProducto = codigo, cantidad = cantidadNum)
                    )
                    codigo = ""
                    cantidads = ""
                    Toast.makeText(context, "Guardado", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Revisa el código y la cantidad", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Guardar")
        }
        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn {
            items(movimientos) { movimiento ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Producto: ${movimiento.codigoProducto}")
                        Text("Cantidad: ${movimiento.cantidad}")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegistroScreenPreview() {
    MovimientosTheme {
        RegistroScreen()
    }
}
