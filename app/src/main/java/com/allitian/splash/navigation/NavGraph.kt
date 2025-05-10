package com.allitian.splash.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.allitian.splash.screens.AddSplash
import com.allitian.splash.screens.BottomNav
import com.allitian.splash.screens.Home
import com.allitian.splash.screens.Login
import com.allitian.splash.screens.Notification
import com.allitian.splash.screens.OtherUser
import com.allitian.splash.screens.Profile
import com.allitian.splash.screens.Search
import com.allitian.splash.screens.Signup
import com.allitian.splash.screens.Splash
import com.allitian.splash.screens.SplashScreen

@Composable
fun NavGraph(navController: NavHostController) {

    NavHost(navController = navController, startDestination = Routes.SplashScreen.routes) {
        composable(Routes.SplashScreen.routes) {
            SplashScreen(navController)
        }
        composable(Routes.Home.routes) {
            Home(navController)
        }
        composable(Routes.Search.routes) {
            Search(navController)
        }
        composable(Routes.AddSplash.routes) {
            AddSplash(navController)
        }
        composable(Routes.Splash.routes) {
            Splash()
        }
        composable(Routes.Notification.routes) {
            Notification()
        }
        composable(Routes.Profile.routes) {
            Profile(navController)
        }
        composable(Routes.BottomNav.routes){
            BottomNav(navController)

        }
        composable(Routes.Login.routes) {
            Login(navController)
        }
        composable(Routes.Signup.routes){
            Signup(navController)

        }
        composable(Routes.OtherUser.routes){
            val data =it.arguments!!.getString("data")
            OtherUser(navController,data!!)

        }




    }
}