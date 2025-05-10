package com.allitian.splash.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.allitian.splash.item_view.SplashItem
import com.allitian.splash.utils.SharedPref
import com.allitian.splash.viewmodel.HomeViewModel
import kotlin.time.Duration.Companion.seconds
import androidx.compose.runtime.getValue
import com.google.firebase.auth.FirebaseAuth


@Composable
fun Home(navHostController: NavHostController){

    val context = LocalContext.current

    val homeViewModel: HomeViewModel= viewModel()
   val splashAndUsers by homeViewModel.splashsAndUsers.observeAsState(null)


    LazyColumn {
        items(splashAndUsers ?: emptyList()){ pairs->
            SplashItem(splash = pairs.first, users = pairs.second,navHostController,FirebaseAuth.getInstance().currentUser!!.uid)
        }
    }

}
@Preview(showBackground = true)
@Composable
fun ShomHome(){
//    Home()
}