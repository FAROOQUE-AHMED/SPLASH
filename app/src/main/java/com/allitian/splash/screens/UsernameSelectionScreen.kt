package com.allitian.splash.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.allitian.splash.viewmodel.AuthViewModel


@Composable
fun UsernameSelectionScreen(navController: NavHostController, authViewModel: AuthViewModel = viewModel()) {
    var username by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Choose Your Username",
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp)
        )

        Spacer(modifier = Modifier.height(30.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { newUsername ->
                username = newUsername.lowercase()
                // Clear previous error when user starts typing
                errorMessage = validateUsernameFormat(newUsername.lowercase())
            },
            label = { Text("Username") },
            isError = errorMessage != null,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true
        )

        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = Color.Red,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = {
                // Re-validate before checking availability
                errorMessage = validateUsernameFormat(username)
                if (errorMessage == null && username.isNotBlank()) {
                    isLoading = true
                    authViewModel.checkUsernameAvailability(username) { isAvailable ->
                        isLoading = false
                        if (isAvailable) {
                            navController.navigate("Signup/$username")
                        } else {
                            errorMessage = "Username already taken!"
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading && username.isNotBlank()
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White)
            } else {
                Text("Next")
            }
        }
    }
}

private fun validateUsernameFormat(username: String): String? {
    return when {
        username.isEmpty() -> "Username cannot be empty"
        username.length < 5 -> "Must be at least 5 characters"
        username.contains(" ") -> "Spaces are not allowed"
        !username.matches(Regex("^[a-z0-9._]+$")) -> "Only (a-z 0-9 _ and . ) allowed"
        username.startsWith(".")  ->
            "Cannot start with a dot"
        username.endsWith(".") ->
            "Cannot end with a dot"
        username.startsWith("_")  ->
            "Cannot start with an underscore"
        username.endsWith("_") ->
            "Cannot end with an underscore"
        username.contains(Regex("[A-Z]")) -> "Uppercase letters not allowed"
        else -> null
    }
}