package com.example.canteenlyapp.data.model

data class CartItem(
    val id: String = "",
    val canteenId: String = "",
    val canteenName: String = "",
    val canteenImageKey: String = "",
    val canteenAddress : String = "",
    val name: String = "",
    val imageKey: String = "",
    val price: Double = 0.0,
    val totalPrice: Double = 0.0,
    val quantity: Int = 0,
    val isSelected: Boolean = false,
    val selectedOptions: List<MenuOptionItem> = emptyList()
)