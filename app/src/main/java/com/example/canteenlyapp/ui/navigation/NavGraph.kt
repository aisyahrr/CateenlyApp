package com.example.canteenlyapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.canteenlyapp.ui.screen.Canteen.CanteenDetailScreen
import com.example.canteenlyapp.ui.screen.Orders.OrdersScreen
import com.example.canteenlyapp.ui.screen.auth.LoginScreen
import com.example.canteenlyapp.ui.screen.auth.RegisterScreen
import com.example.canteenlyapp.ui.screen.checkout.CheckoutScreen
import com.example.canteenlyapp.ui.screen.home.HomeScreen
import com.example.canteenlyapp.ui.screen.main.MainScreen
import com.example.canteenlyapp.ui.screen.onboarding.OnboardingScreen
import com.example.canteenlyapp.ui.screen.order.OrderSuccessScreen
import com.example.canteenlyapp.ui.screen.splash.SplashScreen
@Composable
fun NavGraph() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {

        composable(Screen.Splash.route) {
            SplashScreen(navController)
        }

        composable(Screen.Onboarding.route) {
            OnboardingScreen(navController)
        }

        composable(Screen.Login.route) {
            LoginScreen(navController)
        }

        composable(Screen.Register.route) {
            RegisterScreen(navController)
        }

        composable(Screen.Main.route) {
            MainScreen(navController)
        }
        composable(
            route = Screen.CanteenDetail.route
        ) {

            val canteenId =
                it.arguments?.getString("canteenId") ?: ""

            CanteenDetailScreen(
                canteenId = canteenId,
                navController = navController
            )
        }
        composable(
            route = Screen.Checkout.route
        ) {
            CheckoutScreen(
                navController = navController
            )
        }
        composable(
            Screen.OrderSuccess.route
        ) {
            OrderSuccessScreen(
                navController = navController
            )
        }
        composable(
            route = Screen.Orders.route
        ) {
            OrdersScreen(
                navController = navController
            )
        }

    }
}