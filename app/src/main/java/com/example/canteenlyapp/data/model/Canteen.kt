package com.example.canteenlyapp.data.model

data class Canteen(
    val id: String = "",
    val title: String = "",
    val category: String = "",
    val imageKey: String = "",
    val description: String = "",
    val priceRange: String = "",
    val deliveryTime: String = "",
    val openTime: String = "",
    val closeTime: String = "",
    val location: String = "",
    val deliveryFee: Double = 0.0,
    val rating: Double = 0.0,
    val reviewCount: Int = 0,
    val isAvailable: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)