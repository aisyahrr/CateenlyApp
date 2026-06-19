package com.example.canteenlyapp.data.model

data class MenuOptionGroup(
    val id: String = "",
    val title: String = "",
    val required: Boolean = false,
    val options: Map<String, MenuOptionItem> = emptyMap()
)

data class MenuOptionItem(
    val id: String = "",
    val name: String = "",
    val extraPrice: Double = 0.0
)
