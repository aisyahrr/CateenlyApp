package com.example.canteenlyapp.data.model


data class User(
    val uid: String = "",
    val fullName: String = "",
    val email: String = "",
    val photoUrl: String = "",
    val phoneNumber: String = "",
    val address: String = "",
    val role: String = "customer",
    val active: Boolean = true,  // ← ganti dari isActive ke active
    val createdAt: Long = 0L
)
data class UserPoint(
    val point: Int = 0,
    val orderCount: Int = 0,
    val couponCount: Int = 0
)