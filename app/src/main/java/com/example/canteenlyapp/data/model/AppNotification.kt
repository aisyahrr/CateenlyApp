package com.example.canteenlyapp.data.model

data class AppNotification(
    val id: String = "",
    val title: String = "",
    val message: String = "",
    val orderId: String = "",
    val type: String = "",
    val read: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)