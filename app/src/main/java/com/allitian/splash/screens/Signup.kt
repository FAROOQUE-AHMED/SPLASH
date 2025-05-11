package com.allitian.splash.screens

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.allitian.splash.R
import androidx.navigation.NavHostController
import coil3.compose.rememberAsyncImagePainter
import com.allitian.splash.navigation.Routes
import com.allitian.splash.viewmodel.AuthViewModel
import androidx.compose.foundation.layout.Spacer

@Composable
fun Signup(
    navController: NavHostController,
    usernameFromRoute: String? // This comes from navigation
) {
    var email by remember { mutableStateOf("") }
    // Use the passed username as initial value
    var username by remember { mutableStateOf(usernameFromRoute ?: "") }
    var name by remember { mutableStateOf("Splash User") } // Default name
    var bio by remember { mutableStateOf("Hey hlo, i am new at Splash but i am learning really Fast") } // Default bio
    var password by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val authViewModel: AuthViewModel = viewModel()
    val firebaseUser by authViewModel.firebaseUser.observeAsState(null)

    val permissionToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        android.Manifest.permission.READ_MEDIA_IMAGES
    } else android.Manifest.permission.READ_EXTERNAL_STORAGE

    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            launcher.launch("image/*")
        }
    }

    LaunchedEffect(firebaseUser) {
        if (firebaseUser != null) {
            navController.navigate(Routes.BottomNav.routes) {
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "SIGN-UP",
            style = TextStyle(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp,
            )
        )

        Image(
            painter = if (imageUri == null) {
                painterResource(id = R.drawable.logo)
            } else {
                rememberAsyncImagePainter(model = imageUri)
            },
            contentDescription = "profile",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color.Gray)
                .clickable {
                    val isGranted = ContextCompat.checkSelfPermission(
                        context,
                        permissionToRequest
                    ) == PackageManager.PERMISSION_GRANTED

                    if (isGranted) {
                        launcher.launch("image/*")
                    } else {
                        permissionLauncher.launch(permissionToRequest)
                    }
                },
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Disabled username field (since it was set in previous screen)
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Username",
                style = TextStyle(
                    color = Color.Gray,
                    fontSize = 14.sp
                ),
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = username,
                style = TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))



        name = "Splash User"
        bio = "Hey hlo, i am new at Splash but i am learning really Fast"

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(30.dp))

        ElevatedButton(
            onClick = {
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(context, "Please fill all details", Toast.LENGTH_SHORT).show()
                } else {
                    authViewModel.signup(
                        email = email,
                        password = password,
                        name = name,
                        bio = bio,
                        username = username,
                        imageUri = imageUri ?: Uri.EMPTY,
                        context = context
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Register",
                style = TextStyle(fontWeight = FontWeight.ExtraBold, fontSize = 24.sp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        TextButton(
            onClick = {
                navController.navigate(Routes.Login.routes) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Already registered? Login",
                style = TextStyle(fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
            )
        }
    }
}

