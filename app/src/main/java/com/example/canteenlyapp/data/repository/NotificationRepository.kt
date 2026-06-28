package com.example.canteenlyapp.data.repository

import com.example.canteenlyapp.data.model.AppNotification
import com.google.firebase.database.*

object NotificationRepository {

    private val db = FirebaseDatabase.getInstance().getReference("notifications")

    fun addNotification(ownerId: String, notif: AppNotification) {
        val key = db.child(ownerId).push().key ?: return
        db.child(ownerId).child(key).setValue(notif.copy(id = key))
    }

    fun listenNotifications(
        ownerId: String,
        onResult: (List<AppNotification>) -> Unit
    ) {
        db.child(ownerId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children
                    .mapNotNull { it.getValue(AppNotification::class.java) }
                    .sortedByDescending { it.createdAt }
                onResult(list)
            }
            override fun onCancelled(error: DatabaseError) = onResult(emptyList())
        })
    }

    fun markAllRead(ownerId: String) {
        db.child(ownerId).get().addOnSuccessListener { snap ->
            snap.children.forEach { it.ref.child("read").setValue(true) }
        }
    }
}