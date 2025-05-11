package com.allitian.splash.navigation

sealed class Routes(val routes:String) {

    object Home : Routes("home")
    object Search : Routes("search")
    object AddSplash : Routes("addSplash")
    object Splash : Routes("splash")
    object Notification : Routes("notification")
    object Profile : Routes("profile")
    object SplashScreen : Routes("splashScreen")
    object BottomNav : Routes("bottomNav")
    object Login : Routes("login")
    object Signup : Routes ("signup")
    object UsernameSelectionScreen : Routes ("username")
    object OtherUser : Routes ("other_users/{data}")


}