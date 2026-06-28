package com.example.canteenlyapp.ui.screen.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.canteenlyapp.R
import com.example.canteenlyapp.data.model.User
import com.example.canteenlyapp.ui.navigation.Screen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await

@Composable
fun SplashScreen(
    navController: NavController
) {

    LaunchedEffect(Unit) {

        delay(2000)

        val currentUser =
            FirebaseAuth.getInstance()
                .currentUser

        if (currentUser != null) {

            val snapshot =
                FirebaseDatabase
                    .getInstance()
                    .reference
                    .child("users")
                    .child(currentUser.uid)
                    .get()
                    .await()

            if (snapshot.exists()) {

                val user =
                    snapshot.getValue(
                        User::class.java
                    )

                if (user?.role == "merchant") {

                    navController.navigate(
                        Screen.Merchant.route
                    ) {
                        popUpTo(
                            Screen.Splash.route
                        ) {
                            inclusive = true
                        }
                    }

                } else {

                    navController.navigate(
                        Screen.Main.route
                    ) {
                        popUpTo(
                            Screen.Splash.route
                        ) {
                            inclusive = true
                        }
                    }
                }

            } else {

                FirebaseAuth
                    .getInstance()
                    .signOut()

                navController.navigate(
                    Screen.Login.route
                )
            }

        } else {

            navController.navigate(
                Screen.Login.route
            ) {
                popUpTo(
                    Screen.Splash.route
                ) {
                    inclusive = true
                }
            }
        }
    }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Image(
            painter = painterResource(
                id = R.drawable.splash_bg
            ),
            contentDescription = "Splash Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}