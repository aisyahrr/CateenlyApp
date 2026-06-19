package com.example.canteenlyapp.ui.screen.cart

import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.canteenlyapp.R
import androidx.navigation.NavController
import com.example.canteenlyapp.data.model.Canteen
import com.example.canteenlyapp.data.model.CartItem
import com.example.canteenlyapp.data.repository.CartRepository
import com.example.canteenlyapp.data.repository.FoodRepository
import com.example.canteenlyapp.ui.components.CartItemCard
import com.example.canteenlyapp.ui.components.CheckoutButton
import com.example.canteenlyapp.ui.components.CouponSection
import com.example.canteenlyapp.ui.components.DashedDivider
import com.example.canteenlyapp.ui.components.SummarySection
import com.example.canteenlyapp.ui.components.SwipeableCartItem



//@Preview(
//    showBackground = true,
//    showSystemUi = true
//)
//@Composable
//fun CartScreenPreview() {
//    CartScreen(
//        cartItems = dummyCart
//    )
//}
@Composable
fun CartScreen(navController: NavController) {
    val repository = remember {
        FoodRepository()
    }
    val cartItems = CartRepository.cartItems

    val canteens =
        remember {
            mutableStateListOf<Canteen>()
        }
    LaunchedEffect(Unit) {

        repository.getCanteens { result ->

            canteens.clear()
            canteens.addAll(result)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0E1116))
    ) {

        if (cartItems.isEmpty()) {

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Icon(
                        imageVector = Icons.Outlined.ShoppingCart,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(80.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Your cart is empty",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Browse the menu and add your favorite items.",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }

        } else {

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 15.dp)
            ) {
                items(cartItems) { item ->

                    SwipeableCartItem(
                        item = item,

                        onDelete = {
                            CartRepository.removeItem(item)
                        },

                        onCheckedChange = { checked ->
                            CartRepository.updateSelection(
                                item,
                                checked
                            )
                        },

                        onIncrease = {
                            CartRepository.increaseQuantity(item)
                        },

                        onDecrease = {
                            CartRepository.decreaseQuantity(item)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            DashedDivider()

            Spacer(modifier = Modifier.height(13.dp))

            CouponSection()

            Spacer(modifier = Modifier.height(16.dp))

            SummarySection(
                canteens = canteens
            )

            CheckoutButton(
                navController = navController
            )
        }
    }
}
