package com.example.canteenlyapp.ui.navigation

sealed class Screen(val route: String) {

    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")

    object Login : Screen("login")
    object Register : Screen("register")

    object Main : Screen("main")
    object CanteenDetail :
        Screen("canteen_detail/{canteenId}") {

        fun createRoute(
            canteenId: String
        ) = "canteen_detail/$canteenId"
    }
    object Checkout : Screen("checkout")
    object OrderSuccess : Screen("order_success")
    object Orders : Screen("orders")


}