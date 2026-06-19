package com.example.canteenlyapp.ui.screen.checkout

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.canteenlyapp.data.model.Canteen
import com.example.canteenlyapp.data.model.User
import com.example.canteenlyapp.data.repository.AuthRepository
import com.example.canteenlyapp.data.repository.CartRepository
import com.example.canteenlyapp.data.repository.FoodRepository
import com.example.canteenlyapp.ui.components.CheckoutItemCard
import com.example.canteenlyapp.ui.components.CheckoutShopCard
import com.example.canteenlyapp.ui.components.DashedDivider
import com.example.canteenlyapp.ui.components.DeliveryMethodSection
import com.example.canteenlyapp.ui.components.OrderSummarySection
import com.example.canteenlyapp.ui.components.PaymentMethodSection
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import com.example.canteenlyapp.data.model.Order
import com.example.canteenlyapp.data.repository.OrderRepository
import com.example.canteenlyapp.ui.navigation.Screen

@Composable
fun CheckoutScreen(
    navController: NavController
) {
    CheckoutContent(navController)
}

@Composable
fun CheckoutContent(
    navController: NavController
) {
    val authRepository = remember {
        AuthRepository()
    }
    val groupedItems =
        CartRepository
            .getSelectedItems()
            .groupBy {
                it.canteenId
            }
    val selectedItems =
        CartRepository.getSelectedItems()
    val foodRepository = remember {
        FoodRepository()
    }
    var currentUser by remember {
        mutableStateOf<User?>(null)
    }
    var showAddressSheet by remember {
        mutableStateOf(false)
    }
    var canteens by remember {
        mutableStateOf<List<Canteen>>(emptyList())
    }
    val deliveryFee =
        groupedItems.keys.sumOf { canteenId ->

            canteens.find {
                it.id == canteenId
            }?.deliveryFee ?: 0.0
        }
    val subtotal =
        selectedItems.sumOf {
            it.price * it.quantity
        }

    val discount = 0.0

    val tax = subtotal * 0.1

    val grandTotal =
        subtotal -
                discount +
                deliveryFee +
                tax
    LaunchedEffect(Unit) {

        foodRepository.getCanteens {
            canteens = it
        }
    }

    LaunchedEffect(Unit) {
        currentUser = authRepository.getCurrentUser()
    }

    var deliveryMethod by remember {
        mutableStateOf("Delivery")
    }
    var paymentMethod by remember {
        mutableStateOf("Cash")
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0E1116))
            .navigationBarsPadding()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFF9800))
                .padding(top = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp)
            ) {

                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .size(20.dp)
                        .clickable {
                            navController.popBackStack()
                        }
                )

                Text(
                    text = "Checkout",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            DashedDivider()
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF111827)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Text(
                                text = "📍 Delivery to",
                                color = Color.White,
                                fontSize = 14.sp
                            )

                            Text(
                                text = "Edit",
                                color = Color(0xFFFF7A00),
                                modifier = Modifier.clickable {
                                    showAddressSheet = true
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = currentUser?.address
                                ?.takeIf { it.isNotBlank() }
                                ?: "Set delivery address",
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodySmall,
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Icon(
                                imageVector = Icons.Default.AccessTime,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )

                            Spacer(modifier = Modifier.width(6.dp))

                            Text(
                                text = "Est. 30 min",
                                color = Color.Gray,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Items",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(12.dp))
                groupedItems.forEach { (_, items) ->

                    CheckoutShopCard(
                        items = items
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )
                }
            }
            item {

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Order Summary",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                OrderSummarySection(
                    subtotal = subtotal,
                    discount = discount,
                    deliveryFee = deliveryFee,
                    tax = tax
                )

            }
            item {
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Delivery Methods",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                DeliveryMethodSection(
                    selectedMethod = deliveryMethod,
                    onMethodSelected = {
                        deliveryMethod = it
                    }
                )

            }
            item {
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Payment Methods",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                PaymentMethodSection(
                    selectedMethod = paymentMethod,
                    onMethodSelected = {
                        paymentMethod = it
                    }
                )
            }

        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF111827))
                .padding(18.dp),
        ) {
            val context = LocalContext.current
            Button(
                onClick = {

                    groupedItems.forEach { (canteenId, items) ->

                        val firstItem = items.first()

                        val orderSubtotal =
                            items.sumOf {
                                it.price * it.quantity
                            }

                        val orderTax =
                            orderSubtotal * 0.1

                        val order = Order(
                            userId = currentUser?.uid ?: "",

                            canteenId = canteenId,
                            canteenName = firstItem.canteenName,
                            canteenImageKey = firstItem.canteenImageKey,

                            items = items,

                            subtotal = orderSubtotal,
                            deliveryFee = 0.0,
                            tax = orderTax,
                            total = orderSubtotal + orderTax,

                            deliveryMethod = deliveryMethod,
                            paymentMethod = paymentMethod
                        )

                        OrderRepository.createOrder(order) { success ->

                            if (success) {

                                CartRepository.clearSelectedItems()

                                navController.navigate(
                                    Screen.OrderSuccess.route
                                )

                            } else {

                                Toast.makeText(
                                    context,
                                    "Failed to place order",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),

                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF9800)
                ),

                shape = RoundedCornerShape(12.dp)
            ) {

                Text(
                    text = "Pay $${"%.2f".format(grandTotal)}"
                )
            }
        }
    }
}