package com.example.canteenlyapp.ui.screen.Merchant

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.canteenly.merchant.ui.dashboard.MerchantDashboardContainer
import com.canteenly.merchant.ui.dashboard.MerchantDashboardScreen
import com.example.canteenlyapp.data.repository.CartRepository
import com.example.canteenlyapp.ui.components.CustomBottomBar
import com.example.canteenlyapp.ui.components.MerchantBottomBar
import com.example.canteenlyapp.ui.screen.account.AccountScreen
import com.example.canteenlyapp.ui.screen.cart.CartScreen
import com.example.canteenlyapp.ui.screen.home.HomeScreen
import com.example.canteenlyapp.ui.theme.DarkGray

@Composable
fun MerchantScreen(
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
            MerchantBottomBar  (
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

                0 -> MerchantDashboardContainer( navController = navController)

                1 -> MerchantOrdersScreen( navController = navController)

                2 -> MerchantMenuScreen()

                3 -> MerchantProfileScreen(
                    navController = navController
                )
            }
        }
    }
}
