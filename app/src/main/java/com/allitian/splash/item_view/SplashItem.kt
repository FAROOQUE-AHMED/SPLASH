package com.allitian.splash.item_view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import coil3.compose.rememberAsyncImagePainter
import com.allitian.splash.model.SplashModel
import com.allitian.splash.model.UserModel
import com.allitian.splash.utils.SharedPref


@Composable
fun SplashItem(
    splash: SplashModel,
    users: UserModel,
    navHostController: NavHostController,
    userId:String
){

    Column {
        ConstraintLayout(modifier = Modifier.fillMaxWidth().padding(16.dp)) {

            val (userImage, username, date, time, title, image) = createRefs()
            Image(
                painter =
                    painterResource(id = com.allitian.splash.R.drawable.logo),
//            rememberAsyncImagePainter(model = users.imageUrl),
                contentDescription = "splashscreen", modifier = Modifier
                    .constrainAs(userImage) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
                    .size(36.dp)
                    .clip(CircleShape), contentScale = ContentScale.Crop)

            Text(
                text = users.username.ifBlank { "Unknown User" }, style = TextStyle(
                    fontSize = 20.sp,
                ), modifier = Modifier.constrainAs(username) {
                    top.linkTo(userImage.top)
                    start.linkTo(userImage.end, margin = 12.dp)
                    bottom.linkTo(userImage.bottom)
                })

            Text(
                text = splash.splash

//            SharedPref.getUserName(context)
                , style = TextStyle(
                    fontSize = 18.sp,
                ), modifier = Modifier.constrainAs(title) {
                    top.linkTo(username.bottom, margin = 8.dp)
                    start.linkTo(username.start)
                })

            if (splash.image != "") {

                Card(
                    modifier = Modifier
                        .constrainAs(image) {
                            top.linkTo(title.bottom, margin = 8.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)

                        }) {
                    Image(
                        painter =
//                            painterResource(id = com.example.splash.R.drawable.sample_img),
            rememberAsyncImagePainter(model = splash.image),
                        contentDescription = "close",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp), contentScale = ContentScale.Crop
                    )

                }
            }


        }

        Divider(color = Color.LightGray, thickness = 1.dp)
    }

}

@Preview(showBackground=true)
@Composable
fun ShowSplashItem(){
//    SplashItem()
}