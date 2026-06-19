package com.example.canteenlyapp.ui.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.canteenlyapp.data.repository.CartRepository
import com.example.canteenlyapp.ui.components.CustomBottomBar
import com.example.canteenlyapp.ui.screen.account.AccountScreen
import com.example.canteenlyapp.ui.screen.cart.CartScreen
import com.example.canteenlyapp.ui.screen.home.HomeScreen
import com.example.canteenlyapp.ui.theme.DarkGray

@Composable
fun MainScreen(
    navController: NavController
) {

    var selectedIndex by remember {
        mutableIntStateOf(0)
    }
    LaunchedEffect(Unit) {
        CartRepository.loadCart()
    }

    Scaffold(
        containerColor = DarkGray,
        bottomBar = {
            CustomBottomBar(
                selectedIndex = selectedIndex,
                onItemSelected = {
                    selectedIndex = it
                }
            )
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .padding(paddingValues)
                .background(DarkGray)

        ) {

            when (selectedIndex) {

                0 -> HomeScreen(navController)

                1 -> CartScreen(navController)

                2 -> AccountScreen(navController as NavHostController)
            }
        }
    }
}