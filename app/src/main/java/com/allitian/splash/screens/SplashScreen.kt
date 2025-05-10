package com.allitian.splash.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.allitian.splash.navigation.Routes
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController) {
    var wipeDown by remember { mutableStateOf(false) }
    var showCard by remember { mutableStateOf(false) }
    var animateExit by remember { mutableStateOf(false) }

    // For initial wipe down
    val wipeHeight by animateFloatAsState(
        targetValue = if (wipeDown) 1f else 0f,
        animationSpec = tween(1500),
        label = "wipeDown"
    )

    // For detaching top and bottom
    val transition = updateTransition(targetState = animateExit, label = "detachAnimation")
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    val topOffset by transition.animateDp(
        transitionSpec = { tween(durationMillis = 1400, easing = FastOutSlowInEasing) },
        label = "topOffset"
    ) { if (it) -screenHeight else 0.dp }

    val bottomOffset by transition.animateDp(
        transitionSpec = { tween(durationMillis = 1400, easing = FastOutSlowInEasing) },
        label = "bottomOffset"
    ) { if (it) screenHeight else 0.dp }


    // Animation sequence
    LaunchedEffect(Unit) {
        delay(300)
        wipeDown = true

        delay(1000)
        showCard = true

        delay(800)
        animateExit = true

        delay(1200)
        val isLoggedIn = FirebaseAuth.getInstance().currentUser != null
        navController.navigate(if (isLoggedIn) Routes.BottomNav.routes else Routes.Login.routes) {
            popUpTo(Routes.SplashScreen.routes) { inclusive = true } // Correct route
            launchSingleTop = true
        }

    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .clipToBounds()
    ) {
        // Step 1: White wipe down covering screen
        if (!animateExit) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(wipeHeight)
                    .align(Alignment.TopCenter)
                    .background(Color.White)
            )
        } else {
            // Step 2: White screen splits (top & bottom move away)
            Box(
                modifier = Modifier
                    .offset(y = topOffset)
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
                    .align(Alignment.TopCenter)
                    .background(Color.White)
            )

            Box(
                modifier = Modifier
                    .offset(y = bottomOffset)
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
                    .align(Alignment.BottomCenter)
                    .background(Color.White)
            )
        }

        // Step 3: Center card fades in

        val screenHeight = LocalConfiguration.current.screenHeightDp.dp
        val cardHeight = screenHeight * 0.2f
        AnimatedVisibility(
            visible = showCard,
            enter = fadeIn(tween(500)),
            modifier = Modifier.align(Alignment.Center)

        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(cardHeight)

                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(8.dp) // adjust curve here
                    )
                    .padding(horizontal = 20.dp, vertical = 20.dp),
            contentAlignment = Alignment.Center // Centers the text inside
            )
            {
                val screenWidth = LocalConfiguration.current.screenWidthDp
                val fontSize = (screenWidth * 0.2).sp // This will make font size based on screen width (20% of the width)
                val SerifFont = FontFamily.Serif

                Text(
                    text = "SPLASH",
                    fontSize = fontSize,  // Dynamically scaled font size
                    fontWeight = FontWeight.Bold,
                    fontFamily = SerifFont,
                    color = Color.Black
                )

            }
        }
    }
}
