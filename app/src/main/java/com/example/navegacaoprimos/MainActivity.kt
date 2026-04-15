package com.example.navegacaoprimos

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import kotlin.math.sqrt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.Black // Mantendo o fundo preto como no seu código atual
            ) {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {

        composable("home") {
            HomeScreen { limite ->
                val primos = gerarPrimos(limite)
                val encoded = Uri.encode(primos)
                navController.navigate("results/$encoded")
            }
        }

        composable(
            route = "results/{primes}",
            arguments = listOf(
                navArgument("primes") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val primesData =
                backStackEntry.arguments?.getString("primes")
                    ?: "Nenhum dado encontrado"

            ResultScreen(
                primesData,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

@Composable
fun HomeScreen(onNavigateToPrimes: (Int) -> Unit) {
    var limiteInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Gerador de Primos",
            fontSize = 24.sp,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
        )

        Spacer(modifier = Modifier.height(30.dp))

        OutlinedTextField(
            value = limiteInput,
            onValueChange = { newValue ->
                if (newValue.all { it.isDigit() }) {
                    limiteInput = newValue
                }
            },
            label = { Text("Digite o limite", color = Color.Gray) },
            // ✅ Muda a cor do texto digitado
            textStyle = TextStyle(color = Color.White, fontSize = 18.sp),
            // ✅ Muda as cores da borda e do texto quando focado/desfocado
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = Color.Cyan,
                unfocusedBorderColor = Color.Gray,
                focusedLabelColor = Color.Cyan,
                unfocusedLabelColor = Color.Gray
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                val limite = limiteInput.toIntOrNull() ?: 0
                onNavigateToPrimes(limite)
            },
            enabled = limiteInput.isNotEmpty(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Cyan, contentColor = Color.Black)
        ) {
            Text("Gerar Primos")
        }
    }
}

@Composable
fun ResultScreen(primes: String, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black) // Fundo preto também no resultado
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text("Números primos:", fontSize = 20.sp, color = Color.White)

        Spacer(modifier = Modifier.height(16.dp))

        Text(primes, color = Color.Cyan)

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = onBack,
            colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black)
        ) {
            Text("Voltar")
        }
    }
}

fun gerarPrimos(limite: Int): String {
    if (limite < 2) return "Nenhum primo encontrado."
    val primos = mutableListOf<Int>()

    for (i in 2..limite) {
        var isPrimo = true

        for (j in 2..sqrt(i.toDouble()).toInt()) {
            if (i % j == 0) {
                isPrimo = false
                break
            }
        }

        if (isPrimo) primos.add(i)
    }

    return if (primos.isEmpty()) "Nenhum primo encontrado." else primos.joinToString(", ")
}
