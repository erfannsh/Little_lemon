package com.example.littlelemonclean

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LittleLemonApp(this)
        }
    }
}

@Composable
fun LittleLemonApp(context: Context) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "onboarding") {

        composable("onboarding") {
            OnboardingScreen(context) {
                navController.navigate("home") {
                    popUpTo("onboarding") { inclusive = true }
                }
            }
        }

        composable("home") {
            HomeScreen {
                navController.navigate("profile")
            }
        }

        composable("profile") {
            ProfileScreen(context) {
                navController.navigate("onboarding") {
                    popUpTo("home") { inclusive = true }
                }
            }
        }
    }
}

@Composable
fun OnboardingScreen(context: Context, onDone: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    val prefs = context.getSharedPreferences("user", Context.MODE_PRIVATE)

    Column(Modifier.padding(16.dp)) {

        Text("Onboarding", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") }
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )

        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            prefs.edit()
                .putString("name", name)
                .putString("email", email)
                .apply()

            onDone()
        }) {
            Text("Register")
        }
    }
}

@Composable
fun HomeScreen(goProfile: () -> Unit) {

    val menu = listOf("Pizza", "Pasta", "Salad", "Dessert")

    Column(Modifier.padding(16.dp)) {

        Text("Little Lemon", style = MaterialTheme.typography.headlineMedium)

        Text("Welcome to our restaurant!")

        Spacer(Modifier.height(16.dp))

        Button(onClick = goProfile) {
            Text("Go to Profile")
        }

        Spacer(Modifier.height(16.dp))

        Text("Menu:")

        LazyColumn {
            items(menu) {
                Text("- $it", Modifier.padding(8.dp))
            }
        }
    }
}

@Composable
fun ProfileScreen(context: Context, onLogout: () -> Unit) {

    val prefs = context.getSharedPreferences("user", Context.MODE_PRIVATE)
    val name = prefs.getString("name", "")
    val email = prefs.getString("email", "")

    Column(Modifier.padding(16.dp)) {

        Text("Profile", style = MaterialTheme.typography.headlineMedium)

        Text("Name: $name")
        Text("Email: $email")

        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            prefs.edit().clear().apply()
            onLogout()
        }) {
            Text("Logout")
        }
    }
}