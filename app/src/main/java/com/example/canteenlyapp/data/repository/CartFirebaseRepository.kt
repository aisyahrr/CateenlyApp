package com.example.canteenlyapp.data.repository

import com.example.canteenlyapp.data.model.CartItem
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.runtime.mutableStateListOf
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

object CartFirebaseRepository {

    private val database =
        FirebaseDatabase
            .getInstance()
            .getReference("users")

    fun saveCart(
        userId: String,
        cartItems: List<CartItem>
    ) {
        database
            .child(userId)
            .child("cart")
            .setValue(cartItems)
    }

    fun loadCart(
        userId: String,
        onResult: (List<CartItem>) -> Unit
    ) {
        database
            .child(userId)
            .child("cart")
            .addListenerForSingleValueEvent(
                object : ValueEventListener {

                    override fun onDataChange(
                        snapshot: DataSnapshot
                    ) {
                        val items =
                            mutableListOf<CartItem>()

                        snapshot.children.forEach {

                            it.getValue(
                                CartItem::class.java
                            )?.let { item ->
                                items.add(item)
                            }
                        }

                        onResult(items)
                    }

                    override fun onCancelled(
                        error: DatabaseError
                    ) {
                        onResult(emptyList())
                    }
                }
            )
    }
}


