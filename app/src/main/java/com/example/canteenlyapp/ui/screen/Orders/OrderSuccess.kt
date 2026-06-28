package com.example.canteenlyapp.ui.screen.order

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Storefront
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.canteenlyapp.data.model.Order
import com.example.canteenlyapp.data.repository.OrderRepository
import com.example.canteenlyapp.ui.navigation.Screen

private val Bg = Color(0xFF0E1116)
private val CardBg = Color(0xFF161B22)
private val Orange = Color(0xFFFF9800)
private val Green = Color(0xFF4CAF50)
private val TextPrimary = Color(0xFFF2F4F7)
private val TextSecondary = Color(0xFF9AA4B2)

@Composable
fun OrderSuccessScreen(
    navController: NavController,
    orderId: String
) {
    var order by remember { mutableStateOf<Order?>(null) }

    LaunchedEffect(orderId) {
        OrderRepository.getOrderById(orderId) { order = it }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Bg)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // success icon with glow ring
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(Green.copy(alpha = 0.12f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(86.dp)
                    .background(Green.copy(alpha = 0.18f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Green,
                    modifier = Modifier.size(64.dp)
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Order Placed Successfully!",
            color = TextPrimary,
            textAlign = TextAlign.Justify,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Your order has been received and is now being prepared.",
            color = TextSecondary,
            fontSize = 14.sp,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Justify,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Spacer(Modifier.height(28.dp))

        // detail card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(CardBg, RoundedCornerShape(18.dp))
                .padding(20.dp)
        ) {
            // estimated time highlight
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Orange.copy(alpha = 0.12f), RoundedCornerShape(12.dp))
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = null,
                    tint = Orange,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Estimated Time",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                    Text(
                        text = "${order?.estimatedMinutes ?: 15} minutes",
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            DetailLine(
                icon = Icons.Default.Receipt,
                label = "Order Number",
                value = order?.let { "#${it.orderNumber}" } ?: "—"
            )
            Spacer(Modifier.height(14.dp))
            DetailLine(
                icon = Icons.Default.Storefront,
                label = "Store",
                value = order?.canteenName ?: "—"
            )
            Spacer(Modifier.height(14.dp))
            DetailLine(
                icon = Icons.Default.Payments,
                label = "Payment",
                value = order?.paymentMethod ?: "—"
            )

            Spacer(Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color(0xFF232A33))
            )

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total Paid",
                    color = TextSecondary,
                    fontSize = 14.sp
                )
                Text(
                    text = order?.let { "$${"%.2f".format(it.total)}" } ?: "—",
                    color = Orange,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(Modifier.height(28.dp))

        // primary button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Orange, RoundedCornerShape(14.dp))
                .clickable {
                    navController.navigate(Screen.Orders.route) {
                        popUpTo(Screen.Checkout.route) { inclusive = true }
                    }
                }
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Track Order",
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp
            )
        }

        Spacer(Modifier.height(12.dp))

        // secondary button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFF2D3748), RoundedCornerShape(14.dp))
                .clickable {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Checkout.route) { inclusive = true }
                    }
                }
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Back to Home",
                color = TextSecondary,
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp
            )
        }
    }
}

@Composable
private fun DetailLine(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = TextSecondary,
            modifier = Modifier.size(18.dp)
        )
        Spacer(Modifier.width(10.dp))
        Text(
            text = label,
            color = TextSecondary,
            fontSize = 13.sp,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            color = TextPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
    }
}