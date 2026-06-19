package com.example.canteenlyapp.data.repository

import com.example.canteenlyapp.data.model.Order
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

object OrderRepository {

    private val database =
        FirebaseDatabase
            .getInstance()
            .getReference("orders")

    fun createOrder(
        order: Order,
        onComplete: (Boolean) -> Unit
    ) {

        val orderId = database.push().key ?: return

        database
            .child(orderId)
            .setValue(
                order.copy(id = orderId)
            )
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener {
                onComplete(false)
            }
    }
    fun getOrdersByUserId(
        userId: String,
        onResult: (List<Order>) -> Unit
    ) {

        database
            .orderByChild("userId")
            .equalTo(userId)
            .addValueEventListener(
                object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {

                        val orders =
                            mutableListOf<Order>()

                        snapshot.children.forEach {

                            it.getValue(Order::class.java)
                                ?.let { order ->
                                    orders.add(order)
                                }
                        }

                        onResult(
                            orders.sortedByDescending {
                                it.createdAt
                            }
                        )
                    }

                    override fun onCancelled(
                        error: DatabaseError
                    ) {
                    }
                }
            )
    }

}
