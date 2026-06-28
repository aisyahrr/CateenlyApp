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
        onComplete: (Boolean, String) -> Unit
    ) {

        val orderId = database.push().key ?: return

        database
            .child(orderId)
            .setValue(
                order.copy(id = orderId)
            )
            .addOnSuccessListener {

                onComplete(
                    true,
                    orderId
                )
            }
            .addOnFailureListener {

                onComplete(
                    false,
                    ""
                )
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
                    ) {}
                }
            )
    }
    fun getOrdersByCanteenId(
        canteenId: String,
        onResult: (List<Order>) -> Unit
    ) {

        FirebaseDatabase
            .getInstance()
            .getReference("orders")
            .orderByChild("canteenId")
            .equalTo(canteenId)
            .addValueEventListener(

                object : ValueEventListener {

                    override fun onDataChange(
                        snapshot: DataSnapshot
                    ) {

                        val orders =
                            mutableListOf<Order>()

                        snapshot.children.forEach {

                            it.getValue(Order::class.java)
                                ?.let(orders::add)
                        }

                        onResult(orders)
                    }

                    override fun onCancelled(
                        error: DatabaseError
                    ) {
                        onResult(emptyList())
                    }
                }
            )
    }
    fun getOrderById(
        orderId: String,
        onResult: (Order?) -> Unit
    ) {

        database
            .child(orderId)
            .addListenerForSingleValueEvent(

                object : ValueEventListener {

                    override fun onDataChange(
                        snapshot: DataSnapshot
                    ) {

                        onResult(
                            snapshot.getValue(
                                Order::class.java
                            )
                        )
                    }

                    override fun onCancelled(
                        error: DatabaseError
                    ) {

                        onResult(null)
                    }
                }
            )
    }
    fun updateOrderStatus(
        orderId: String,
        status: String,
        onComplete: (Boolean) -> Unit
    ) {

        database
            .child(orderId)
            .child("status")
            .setValue(status)
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener {
                onComplete(false)
            }
    }
    fun getTotalOrdersByUser(
        userId: String,
        onResult: (Int) -> Unit
    ) {
        FirebaseDatabase.getInstance()
            .getReference("orders")
            .orderByChild("userId")
            .equalTo(userId)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {
                        onResult(snapshot.childrenCount.toInt())
                    }

                    override fun onCancelled(error: DatabaseError) {
                        onResult(0)
                    }
                }
            )
    }
}
