package com.allitian.splash.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.allitian.splash.model.BottomNavItem
import com.allitian.splash.navigation.Routes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun BottomNav(navController: NavHostController){

    val navController1= rememberNavController()
    var backPressedTime by remember { mutableStateOf(0L) }
    var lastBackPressedTime by remember { mutableStateOf(0L) }


    Scaffold(bottomBar = {MyBottomBar(navController1)}) { innerPadding ->
        NavHost(navController = navController1, startDestination = Routes.Home.routes,
            modifier = Modifier.padding(innerPadding)){
            composable(route = Routes.Home.routes){
                Home(navController)
                // Handle the back press logic on Home screen
                HandleBackPress(
                    onBackPressed = {
                        val currentTime = System.currentTimeMillis()

                        // If the user presses back once, show a toast
                        if (currentTime - backPressedTime < 2000) {
                            System.exit(0)
                            navController1.popBackStack()
                        } else {
                            // Show "Press back again to exit" toast
                            Toast.makeText(navController1.context, "Press back again to exit", Toast.LENGTH_SHORT).show()
                            backPressedTime = currentTime
                        }
                    }
                )

            }
            composable(route=Routes.Search.routes){
                Search(navController)
            }
            composable(route = Routes.AddSplash.routes){
                AddSplash(navController1)
            }
            composable(route = Routes.Splash.routes){
                Splash()
            }
            composable(route = Routes.Notification.routes){
                Notification()
            }
            composable(route = Routes.Profile.routes){
                Profile(navController)
            }

        }

    }

}
@Composable
fun HandleBackPress(onBackPressed: () -> Unit) {
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val backCallback = rememberUpdatedState(onBackPressed)

    // Capture the back press and call the provided onBackPressed function
    BackHandler {
        backCallback.value()
    }
}


@Composable
fun MyBottomBar(navController1: NavHostController){

    val backStackEntry= navController1.currentBackStackEntryAsState()
    val list = listOf(

        BottomNavItem(
            "Home",
            Routes.Home.routes,
            Icons.Rounded.Home
        ),

        BottomNavItem(
            "Search",
            Routes.Search.routes,
            Icons.Rounded.Search
        ),

        BottomNavItem(
            "AddSplash",
        Routes.AddSplash.routes,
        Icons.Rounded.AddCircle
        ),

    BottomNavItem(
        "Splash",
        Routes.Splash.routes,
        Icons.Rounded.DateRange
    ),

    BottomNavItem(
        "Notification",
        Routes.Notification.routes,
        Icons.Rounded.Notifications
    ),

    BottomNavItem(
        "Profile",
        Routes.Profile.routes,
        Icons.Rounded.AccountCircle
    )

    )

    BottomAppBar {
        list.forEach {
            val selected = it.route==backStackEntry?.value?.destination?.route
            NavigationBarItem(selected= selected, onClick = {
                navController1.navigate(it.route){
                    popUpTo(navController1.graph.findStartDestination().id){
                        saveState=true
                    }
                    launchSingleTop=true
                }
            }
                , icon = {
                    Icon(imageVector = it.icon, contentDescription = it.title)

            })
        }
    }

}