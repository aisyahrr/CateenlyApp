package com.example.canteenlyapp.ui.screen.OrderDetails

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.canteenlyapp.data.model.AppNotification
import com.example.canteenlyapp.data.model.Order
import com.example.canteenlyapp.data.repository.NotificationRepository
import com.example.canteenlyapp.data.repository.OrderRepository

/* ---------- Screen: MERCHANT ---------- */
@Composable
fun MerchantOrderDetailScreen(
    navController: NavController,
    orderId: String
) {
    val context = LocalContext.current
    var order by remember { mutableStateOf<Order?>(null) }

    LaunchedEffect(orderId) {
        OrderRepository.getOrderById(orderId) {
            order = it
        }
    }

    fun nextStatusOf(o: Order): String = when {
        o.status == "Preparing" && o.deliveryMethod.equals("Pickup", true) -> "Ready Pickup"
        o.status == "Preparing" && o.deliveryMethod.equals("Delivery", true) -> "On The Way"
        o.status == "Ready Pickup" -> "Done"
        o.status == "On The Way" -> "Done"
        else -> o.status
    }

    fun actionLabelOf(o: Order): String = when {
        o.status == "Preparing" && o.deliveryMethod.equals("Pickup", true) -> "Mark Ready for Pickup"
        o.status == "Preparing" && o.deliveryMethod.equals("Delivery", true) -> "Send Order"
        o.status == "Ready Pickup" -> "Complete Order"
        o.status == "On The Way" -> "Complete Order"
        o.status == "Done" -> "Completed"
        else -> "Completed"
    }

    fun advance(o: Order) {
        val next = nextStatusOf(o)
        if (next == o.status) return   // sudah final

        OrderRepository.updateOrderStatus(o.id, next) { success ->
            if (success) {
                order = o.copy(status = next)
                // kirim notifikasi ke customer
                NotificationRepository.addNotification(
                    ownerId = o.userId,
                    notif = AppNotification(
                        title = "Update Order",
                        message = "Order #${o.orderNumber} sekarang: $next",
                        orderId = o.id,
                        type = "status_update"
                    )
                )
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0E1116))
    ) {
        // Top app bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFF9800))
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(22.dp)
                    .clickable { navController.popBackStack() }
            )
            Text(
                text = "Order Details",
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        if (order == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Loading...", color = Color(0xFFFF9800))
            }
        } else {
            val currentOrder = order!!

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // status (pakai komponen yang sama)
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.Black)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Order Status",
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            )
                            OrderStatusCard(status = currentOrder.status)
                        }
                    }
                }

                // semua kartu reuse dari file customer
                item { DeliveryInfoCard(order = currentOrder) }
                item { AddressCard(order = currentOrder) }
                item { OrderItemsCard(order = currentOrder) }
                item { PaymentSummaryCard(order = currentOrder) }
                item { OrderInfoCard(order = currentOrder) }
                item { Spacer(Modifier.height(4.dp)) }
            }

            // ---------- action bar MERCHANT ----------
            val isFinal = currentOrder.status == "Done"
            val label = actionLabelOf(currentOrder)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            if (isFinal) Color(0xFF4CAF50) else Color(0xFFFF9800),
                            RoundedCornerShape(8.dp)
                        )
                        .then(
                            if (isFinal) Modifier
                            else Modifier.clickable { advance(currentOrder) }
                        )
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}