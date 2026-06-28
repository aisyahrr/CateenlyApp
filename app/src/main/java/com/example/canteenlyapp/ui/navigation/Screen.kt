package com.example.canteenlyapp.ui.navigation

sealed class Screen(val route: String) {

    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")

    object Login : Screen("login")
    object Register : Screen("register")

    object Main : Screen("main")
    object Merchant : Screen("merchant")
    object CanteenDetail :
        Screen("canteen_detail/{canteenId}") {

        fun createRoute(
            canteenId: String
        ) = "canteen_detail/$canteenId"
    }
    object Checkout : Screen("checkout")
    object OrderSuccess :
        Screen("order_success/{orderId}") {

        fun createRoute(
            orderId: String
        ) = "order_success/$orderId"
    }
    object Orders : Screen("orders")
    object CustomerOrderDetail :
        Screen("customer_order_detail/{orderId}") {

        fun createRoute(
            orderId: String
        ) = "customer_order_detail/$orderId"
    }

    object MerchantOrderDetail :
        Screen("merchant_order_detail/{orderId}") {

        fun createRoute(
            orderId: String
        ) = "merchant_order_detail/$orderId"
    }
    object QrisPayment : Screen("qris_payment/{orderId}") {
        fun createRoute(orderId: String) = "qris_payment/$orderId"
    }


}