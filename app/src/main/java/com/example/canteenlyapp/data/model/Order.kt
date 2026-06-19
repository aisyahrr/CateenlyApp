package com.example.canteenlyapp.data.model

data class Order(
    val id: String = "",
    val userId: String = "",

    val canteenId: String = "",
    val canteenName: String = "",
    val canteenImageKey: String = "",

    val items: List<CartItem> = emptyList(),

    val subtotal: Double = 0.0,
    val deliveryFee: Double = 0.0,
    val tax: Double = 0.0,
    val total: Double = 0.0,

    val deliveryMethod: String = "",
    val paymentMethod: String = "",

    val status: String = "Preparing",
    val estimatedMinutes: Int = 15,

    val createdAt: Long = System.currentTimeMillis()
)