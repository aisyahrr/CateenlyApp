package com.example.canteenlyapp.ui.screen.Merchant

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.canteenlyapp.data.model.AppNotification
import com.example.canteenlyapp.data.model.Order
import com.example.canteenlyapp.data.repository.AuthRepository
import com.example.canteenlyapp.data.repository.NotificationRepository
import com.example.canteenlyapp.data.repository.OrderRepository
import com.example.canteenlyapp.ui.components.MerchantOrderCard
import com.example.canteenlyapp.ui.navigation.Screen

@Composable
fun MerchantOrdersScreen(navController: NavController) {

    val authRepository = remember { AuthRepository() }
    var orders by remember { mutableStateOf<List<Order>>(emptyList()) }
    var filter by remember { mutableStateOf("Active") }   // default tampilkan yang aktif

    LaunchedEffect(Unit) {
        val merchant = authRepository.getCurrentMerchant()
        merchant?.let { user ->
            OrderRepository.getOrdersByCanteenId(user.canteenId) { result ->
                orders = result
            }
        }
    }

    val activeStatuses = listOf("Preparing", "Ready Pickup", "On The Way")

    // saring + urutkan terbaru di atas
    val shown = remember(orders, filter) {
        orders
            .filter {
                when (filter) {
                    "Active" -> it.status in activeStatuses
                    "Done" -> it.status == "Done"
                    else -> true   // All
                }
            }
            .sortedByDescending { it.createdAt }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0E1116))
    ) {

        // ---------- filter chips ----------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            listOf("All","Active", "Done").forEach { f ->
                val selected = filter == f

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { filter = f }
                        .padding(vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = f,
                        color = if (selected) Color(0xFFFF9800) else Color(0xFF9AA4B2),
                        fontSize = 14.sp,
                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
                    )

                    Spacer(Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .height(2.dp)
                            .width(28.dp)
                            .clip(RoundedCornerShape(50))
                            .background(
                                if (selected) Color(0xFFFF9800) else Color.Transparent
                            )
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color(0xFF1E293B))
        )

        // ---------- list ----------
        if (shown.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "No orders here",
                    color = Color(0xFF9AA4B2),
                    fontSize = 14.sp
                )
            }
        } else {
            Spacer(Modifier.height(8.dp))
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(shown) { order ->
                    MerchantOrderCard(
                        order = order,
                        onCardClick = {
                            navController.navigate(
                                Screen.MerchantOrderDetail.createRoute(order.id)
                            )
                        },
                        onActionClick = {
                            val nextStatus = when {
                                order.status == "Preparing" && order.deliveryMethod == "Pickup" -> "Ready Pickup"
                                order.status == "Preparing" && order.deliveryMethod == "Delivery" -> "On The Way"
                                order.status == "Ready Pickup" -> "Done"
                                order.status == "On The Way" -> "Done"
                                else -> order.status
                            }

                            OrderRepository.updateOrderStatus(order.id, nextStatus) {
                                orders = orders.map {
                                    if (it.id == order.id) it.copy(status = nextStatus) else it
                                }
                                NotificationRepository.addNotification(
                                    ownerId = order.userId,
                                    notif = AppNotification(
                                        title = "Update Order",
                                        message = "Order #${order.orderNumber} now: $nextStatus",
                                        orderId = order.id,
                                        type = "status_update"
                                    )
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}