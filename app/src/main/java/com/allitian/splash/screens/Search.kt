package com.allitian.splash.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.allitian.splash.item_view.SplashItem
import com.allitian.splash.item_view.UserItem
import com.allitian.splash.viewmodel.HomeViewModel
import com.allitian.splash.viewmodel.SearchViewModel
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun Search(navHostController: NavHostController){

    val searchViewModel: SearchViewModel= viewModel()
    val userList by searchViewModel.usersList.observeAsState(null)
    var search by remember { mutableStateOf("") }

    Column {
        Text(text = "SEARCH", style = TextStyle(
            fontWeight = FontWeight.ExtraBold,
            fontSize = 24.sp,

            ), modifier = Modifier.padding(top=16.dp,start=16.dp)
        )
        OutlinedTextField(value= search, onValueChange = {search=it}, label = {
            Text(text = "Search User")
        }, keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text)
            , singleLine = true, modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "null")
            }
            )


        Box(modifier = Modifier.height(20.dp))

        LazyColumn {
            if(userList!= null && userList!!.isNotEmpty()) {
                val filterItem = userList!!.filter {
                    it.username.contains(search, ignoreCase = true)
                }

                items(filterItem) { pairs ->
                    UserItem(pairs, navHostController)
                }
            }
        }
    }

}