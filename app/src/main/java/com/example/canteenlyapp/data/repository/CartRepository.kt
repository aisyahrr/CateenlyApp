package com.example.canteenlyapp.data.repository

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import com.example.canteenlyapp.data.model.CartItem
import com.google.firebase.auth.FirebaseAuth

object CartRepository {

    private val _cartItems = mutableStateListOf<CartItem>()

    val cartItems: List<CartItem>
        get() = _cartItems
    private val auth = FirebaseAuth.getInstance()

    private fun syncCart() {

        val uid = auth.currentUser?.uid ?: return

        Log.d("CART", "SYNCING ${_cartItems.size} ITEMS")

        CartFirebaseRepository.saveCart(
            userId = uid,
            cartItems = _cartItems
        )
    }

    fun addItem(item: CartItem) {

        val existing = _cartItems.find {
            it.id == item.id &&
                    it.selectedOptions == item.selectedOptions
        }

        if (existing != null) {

            val index = _cartItems.indexOf(existing)

            _cartItems[index] = existing.copy(
                quantity = existing.quantity + item.quantity
            )

        } else {
            _cartItems.add(item)
        }

        syncCart()
    }

    fun increaseQuantity(item: CartItem) {

        val index = _cartItems.indexOf(item)

        if (index != -1) {

            val newQty = item.quantity + 1

            _cartItems[index] = item.copy(
                quantity = newQty,
                totalPrice = item.price * newQty
            )

            syncCart()
        }
    }

    fun decreaseQuantity(item: CartItem) {

        val index = _cartItems.indexOf(item)

        if (index != -1) {

            if (item.quantity > 1) {

                val newQty = item.quantity - 1

                _cartItems[index] = item.copy(
                    quantity = newQty,
                    totalPrice = item.price * newQty
                )

            } else {

                _cartItems.remove(item)
            }

            syncCart()
        }
    }

    fun removeItem(item: CartItem) {
        _cartItems.remove(item)
        syncCart()
    }

    fun updateSelection(
        item: CartItem,
        isSelected: Boolean
    ) {

        val index = _cartItems.indexOf(item)

        if (index != -1) {
            _cartItems[index] = item.copy(
                isSelected = isSelected
            )

            syncCart()
        }
    }

    fun getSelectedItems(): List<CartItem> {
        return _cartItems.filter {
            it.isSelected
        }
    }

    fun getTotalItemCount(): Int {

        return _cartItems.sumOf {
            it.quantity
        }
    }

    fun getTotalPrice(): Double {
        return _cartItems
            .filter { it.isSelected }
            .sumOf { it.totalPrice }
    }

    fun clearCart() {
        _cartItems.clear()
        syncCart()
    }

    fun setCartItems(items: List<CartItem>) {
        _cartItems.clear()
        _cartItems.addAll(items)
    }
    fun loadCart() {

        val uid = auth.currentUser?.uid ?: return

        CartFirebaseRepository.loadCart(uid) { items ->
            setCartItems(items)
        }
    }
    fun clearSelectedItems() {

        _cartItems.removeAll {
            it.isSelected
        }

        syncCart()
    }
}