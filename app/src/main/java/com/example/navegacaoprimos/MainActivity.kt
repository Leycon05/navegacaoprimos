package com.example.navegacaoprimos

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
                color = Color(0xFFE3F2FD)
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE3F2FD)),
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

        Button(onClick = { onNavigateToPrimes(50) }) {
            Text("Gerar até 50")
        }
    }
}

@Composable
fun ResultScreen(primes: String, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text("Números primos:", fontSize = 20.sp)

        Spacer(modifier = Modifier.height(16.dp))

        Text(primes)

        Spacer(modifier = Modifier.height(30.dp))

        Button(onClick = onBack) {
            Text("Voltar")
        }
    }
}

fun gerarPrimos(limite: Int): String {
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

    return primos.joinToString(", ")
}