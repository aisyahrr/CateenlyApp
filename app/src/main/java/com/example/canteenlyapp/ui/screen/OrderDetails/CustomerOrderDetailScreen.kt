package com.example.canteenlyapp.ui.screen.OrderDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.canteenlyapp.data.model.AppNotification
import com.example.canteenlyapp.data.model.Order
import com.example.canteenlyapp.data.repository.OrderRepository
import com.example.canteenlyapp.data.model.Canteen
import com.example.canteenlyapp.data.repository.FoodRepository
import com.example.canteenlyapp.data.repository.NotificationRepository

/* ---------- Shopee palette ---------- */
private val ShopeeOrange = Color(0xFFFF9800)
private val ShopeeOrangeLight = Color(0xFFFFA040)
private val CardBg = Color.Black
private val TextPrimary = Color.White
private val TextSecondary = Color(0xFF888888)
private val DividerColor = Color(0x70A09E9E)

private val CardShape = RoundedCornerShape(10.dp)

/* ---------- Status banner (gradient, full width) ---------- */
@Composable
fun OrderStatusCard(
    status: String
) {
    val color = when (status) {
        "Preparing" -> Color(0xFFFF9800)
        "On The Way" -> Color(0xFF2196F3)
        "Ready Pickup" -> Color(0xFF9C27B0)
        "Done" -> Color(0xFF4CAF50)
        else -> Color(0xFF888888)
    }

    Row(
        modifier = Modifier
            .background(color.copy(alpha = 0.12f), RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(color, CircleShape)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = status,
            color = color,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp
        )
    }
}

/* ---------- Delivery info ---------- */
@Composable
fun DeliveryInfoCard(
    order: Order
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = CardShape,
        colors = CardDefaults.cardColors(containerColor = CardBg)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.LocalShipping,
                contentDescription = null,
                tint = ShopeeOrange,
                modifier = Modifier.size(22.dp)
            )

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Delivery Information",
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = order.deliveryMethod,
                    color = TextSecondary,
                    fontSize = 13.sp
                )
            }

            Text(
                text = "${order.estimatedMinutes} min",
                color = ShopeeOrange,
                fontWeight = FontWeight.Medium,
                fontSize = 13.sp
            )
        }
    }
}

/* ---------- Address ---------- */
@Composable
fun AddressCard(
    order: Order,
    canteenAddress: String = ""
) {
    val isPickup = order.deliveryMethod.equals("Pickup", ignoreCase = true)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = CardShape,
        colors = CardDefaults.cardColors(containerColor = CardBg)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = ShopeeOrange,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = if (isPickup) "Pickup Location" else "Delivery Address",
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = if (isPickup) order.canteenName else order.customerName,
                color = TextPrimary,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )
            Text(
                text = if (isPickup) {
                    order.canteenAddress.ifBlank { "Pickup address unavailable" }
                } else {
                    order.customerAddress.ifBlank { "Set delivery address" }
                },
                color = TextSecondary,
                fontSize = 13.sp
            )

            Spacer(Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = "Est. ${order.estimatedMinutes} min",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }
        }
    }
}

/* ---------- Store + items ---------- */
@Composable
fun OrderItemsCard(
    order: Order
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = CardShape,
        colors = CardDefaults.cardColors(containerColor = CardBg)
    ) {
        Column {
            // store header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Storefront,
                    contentDescription = null,
                    tint = ShopeeOrange,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = order.canteenName,
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.size(18.dp)
                )
            }

            HorizontalDivider(color = DividerColor)

            // items
            order.items.forEachIndexed { index, item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.name,
                            color = TextPrimary,
                            fontSize = 14.sp
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "x${item.quantity}",
                            color = TextSecondary,
                            fontSize = 13.sp
                        )
                    }

                    Text(
                        text = "$${item.totalPrice}",
                        color = TextPrimary,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                }

                if (index != order.items.lastIndex) {
                    HorizontalDivider(
                        color = DividerColor,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}

/* ---------- Payment summary ---------- */
@Composable
fun PaymentSummaryCard(
    order: Order
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = CardShape,
        colors = CardDefaults.cardColors(containerColor = CardBg)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Payment Details",
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )

            Spacer(Modifier.height(14.dp))

            SummaryRow("Subtotal", "$${order.subtotal}")
            SummaryRow("Delivery Fee", "$${order.deliveryFee}")
            SummaryRow("Tax","$${order.tax.toCurrency()}")

            HorizontalDivider(
                color = DividerColor,
                modifier = Modifier.padding(vertical = 6.dp)
            )

            SummaryRow(
                title = "Total",
                value = "$${order.total}",
                bold = true,
                highlight = true
            )
        }
    }
}

fun Double.toCurrency(): String {
    return String.format("%.2f", this)
}
@Composable
fun SummaryRow(
    title: String,
    value: String,
    bold: Boolean = false,
    highlight: Boolean = false
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            color = if (bold) TextPrimary else TextSecondary,
            fontWeight = if (bold) FontWeight.SemiBold else FontWeight.Normal,
            fontSize = 13.sp
        )
        Text(
            text = value,
            color = if (highlight) ShopeeOrange else TextPrimary,
            fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
            fontSize = if (bold) 16.sp else 13.sp
        )
    }
}

/* ---------- Order info ---------- */
@Composable
fun OrderInfoCard(
    order: Order
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = CardShape,
        colors = CardDefaults.cardColors(containerColor = CardBg)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Order Information",
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )

            Spacer(Modifier.height(14.dp))

            InfoRow(
                title = "Order Number",
                value = "#${order.orderNumber}",
                copyable = true
            )
            InfoRow(title = "Payment Method", value = order.paymentMethod)
            InfoRow(title = "Delivery Method", value = order.deliveryMethod)
            InfoRow(title = "Status", value = order.status)
        }
    }
}

@Composable
fun InfoRow(
    title: String,
    value: String,
    copyable: Boolean = false
) {
    val clipboard = LocalClipboardManager.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = TextSecondary,
            fontSize = 13.sp
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = value,
                color = TextPrimary,
                fontWeight = FontWeight.Medium,
                fontSize = 13.sp
            )
            if (copyable) {
                Spacer(Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = "Copy",
                    tint = ShopeeOrange,
                    modifier = Modifier
                        .size(15.dp)
                        .clickable {
                            clipboard.setText(AnnotatedString(value))
                        }
                )
            }
        }
    }
}

/* ---------- Screen ---------- */
@Composable
fun CustomerOrderDetailScreen(
    navController: NavController,
    orderId: String
) {
    var order by remember { mutableStateOf<Order?>(null) }

    LaunchedEffect(orderId) {
        OrderRepository.getOrderById(orderId) {
            order = it
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0E1116))
    ) {
        // Top app bar (orange, merges with status banner)
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
                Text(text = "Loading...", color = (Color(0xFFFF9800)) )
            }
        }else {
        val currentOrder = order!!

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = CardShape,
                    colors = CardDefaults.cardColors(containerColor = CardBg)
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
                            color = TextPrimary,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                        OrderStatusCard(status = currentOrder.status)
                    }
                }
            }

            item { DeliveryInfoCard(order = currentOrder) }
            item { AddressCard(order = currentOrder) }
            item { OrderItemsCard(order = currentOrder) }
            item { PaymentSummaryCard(order = currentOrder) }
            item { OrderInfoCard(order = currentOrder) }
            item { Spacer(Modifier.height(4.dp)) }
        }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CardBg)
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .border(
                            1.dp,
                            ShopeeOrange,
                            RoundedCornerShape(6.dp)
                        )
                        .clickable {
                            // TODO: chat merchant
                        }
                        .padding(vertical = 12.dp),

                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = "Contact Store",
                        color = ShopeeOrange,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                }

                if (currentOrder.status == "Ready Pickup") {

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(ShopeeOrange, RoundedCornerShape(6.dp))
                            .clickable {
                                OrderRepository.updateOrderStatus(
                                    currentOrder.id,
                                    "Done"
                                ) { success ->
                                    if (success) {
                                        order = currentOrder.copy(status = "Done")

                                        // notif ke MERCHANT
                                        NotificationRepository.addNotification(
                                            ownerId = currentOrder.canteenId,
                                            notif = AppNotification(
                                                title = "Order Completed",
                                                message = "Order #${currentOrder.orderNumber} has been picked up by the customer",
                                                orderId = currentOrder.id,
                                                type = "status_update"
                                            )
                                        )
                                    }
                                }
                            }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "I've Picked Up",
                            color = Color.White,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    }
                }

                else if (currentOrder.status == "On The Way") {

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(ShopeeOrange, RoundedCornerShape(6.dp))
                            .clickable {
                                OrderRepository.updateOrderStatus(
                                    currentOrder.id,
                                    "Done"
                                ) { success ->
                                    if (success) {
                                        order = currentOrder.copy(status = "Done")

                                        // notif ke MERCHANT
                                        NotificationRepository.addNotification(
                                            ownerId = currentOrder.canteenId,
                                            notif = AppNotification(
                                                title = "Order Completed",
                                                message = "Order #${currentOrder.orderNumber} has been received by the customer",
                                                orderId = currentOrder.id,
                                                type = "status_update"
                                            )
                                        )
                                    }
                                }
                            }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Order Received",
                            color = Color.White,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    }
                }

                else if (currentOrder.status == "Done") {

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                Color(0xFF4CAF50),
                                RoundedCornerShape(6.dp)
                            )
                            .padding(vertical = 12.dp),

                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = "Completed",
                            color = Color.White,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    }
                }
                else {

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                Color.Gray.copy(alpha = 0.2f),
                                RoundedCornerShape(6.dp)
                            )
                            .padding(vertical = 12.dp),

                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = "Preparing...",
                            color = Color.Gray,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}