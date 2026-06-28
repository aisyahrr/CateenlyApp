package com.example.canteenlyapp.data.model

data class Menu(
    val id: String = "",
    val canteenId: String = "",
    val canteenName: String = "",
    val canteenAddress: String = "",
    val name: String = "",
    val category: String = "",
    val description: String = "",
    val imageKey: String = "",
    val price: Double = 0.0,
    val oldPrice: Double = 0.0,
    val isBestSeller: Boolean = false,
    val rating: Double = 0.0,
    val isAvailable: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val optionGroups: List<MenuOptionGroup> = emptyList()
)