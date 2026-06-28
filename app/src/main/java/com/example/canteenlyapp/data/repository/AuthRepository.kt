package com.example.canteenlyapp.data.repository

import android.util.Log
import com.example.canteenlyapp.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import android.net.Uri
import com.google.firebase.storage.FirebaseStorage


class AuthRepository {

    private val auth = FirebaseAuth.getInstance()

    private val database =
        FirebaseDatabase
            .getInstance()
            .reference

    suspend fun register(
        fullName: String,
        email: String,
        password: String
    ): Result<Unit> {

        return try {

            val result = auth
                .createUserWithEmailAndPassword(
                    email,
                    password
                )
                .await()

            val uid = result.user?.uid ?: ""

            val user = User(
                uid = uid,
                fullName = fullName,
                email = email,
                createdAt = System.currentTimeMillis()
            )

            database
                .child("users")
                .child(uid)
                .setValue(user)
                .await()

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun login(
        email: String,
        password: String
    ): Result<Unit> {

        return try {

            auth.signInWithEmailAndPassword(
                email,
                password
            ).await()

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun getCurrentUser(): User? {

        val uid = auth.currentUser?.uid ?: return null

        return try {

            val snapshot = database
                .child("users")
                .child(uid)
                .get()
                .await()

            snapshot.getValue(User::class.java)

        } catch (e: Exception) {
            null
        }
    }

    suspend fun getCurrentMerchant(): User? {

        val user = getCurrentUser()

        return if (user?.role == "merchant") {
            user
        } else {
            null
        }
    }

    fun getCurrentUid(): String? {
        return auth.currentUser?.uid
    }
    suspend fun updateFullName(
        fullName: String
    ): Result<Unit> {

        return try {

            val uid = auth.currentUser?.uid
                ?: return Result.failure(
                    Exception("User not logged in")
                )

            database
                .child("users")
                .child(uid)
                .child("fullName")
                .setValue(fullName)
                .await()

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun updateAddress(
        address: String
    ): Result<Unit> {

        return try {

            val uid = auth.currentUser?.uid
                ?: return Result.failure(
                    Exception("User not logged in")
                )

            database
                .child("users")
                .child(uid)
                .child("address")
                .setValue(address)
                .await()

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        FirebaseAuth.getInstance().signOut()
    }
}