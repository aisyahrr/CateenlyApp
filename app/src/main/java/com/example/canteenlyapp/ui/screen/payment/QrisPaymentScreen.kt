package com.example.canteenlyapp.ui.screen.payment

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.canteenlyapp.data.model.Order
import com.example.canteenlyapp.data.repository.OrderRepository
import com.example.canteenlyapp.ui.navigation.Screen
import com.example.canteenlyapp.utils.generateQrBitmap
import kotlinx.coroutines.delay

private val Bg = Color(0xFF0E1116)
private val CardBg = Color(0xFF161B22)
private val Orange = Color(0xFFFF9800)
private val TextPrimary = Color.White
private val TextSecondary = Color(0xFF9AA4B2)

@Composable
fun QrisPaymentScreen(
    navController: NavController,
    orderId: String
) {
    var order by remember { mutableStateOf<Order?>(null) }

    LaunchedEffect(orderId) {
        OrderRepository.getOrderById(orderId) { order = it }
    }

    // countdown 15 menit
    var remaining by remember { mutableIntStateOf(15 * 60) }
    LaunchedEffect(Unit) {
        while (remaining > 0) {
            delay(1000)
            remaining--
        }
    }
    val timeText = "%02d:%02d".format(remaining / 60, remaining % 60)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Bg)
    ) {
        // top bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Orange)
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Text(
                text = "QRIS Payment",
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        val currentOrder = order

        if (currentOrder == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Loading...", color = Orange)
            }
        } else {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(8.dp))

                // timer
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = null,
                        tint = Orange,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "Pay within $timeText",
                        color = Orange,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                }

                Spacer(Modifier.height(20.dp))

                // QR
                val qrContent = """
                    Canteenly Payment
                    Order: #${currentOrder.orderNumber}
                    Store: ${currentOrder.canteenName}
                    Amount: $${"%.2f".format(currentOrder.total)}
                """.trimIndent()

                val qrBitmap = remember(qrContent) { generateQrBitmap(qrContent) }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    Image(
                        bitmap = qrBitmap.asImageBitmap(),
                        contentDescription = "QRIS",
                        modifier = Modifier.size(220.dp)
                    )
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Scan with any QRIS-supported app",
                    color = TextSecondary,
                    fontSize = 13.sp
                )

                Spacer(Modifier.height(20.dp))

                // detail card
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(CardBg)
                        .padding(16.dp)
                ) {
                    PayRow("Order Number", "#${currentOrder.orderNumber}")
                    Spacer(Modifier.height(10.dp))
                    PayRow("Store", currentOrder.canteenName)
                    Spacer(Modifier.height(10.dp))
                    PayRow(
                        "Total",
                        "$${"%.2f".format(currentOrder.total)}",
                        highlight = true
                    )
                }
            }

            // bottom button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CardBg)
                    .navigationBarsPadding()
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Orange, RoundedCornerShape(10.dp))
                        .clickable {
                            // demo: anggap sudah bayar -> ke Order Success
                            navController.navigate(
                                Screen.OrderSuccess.createRoute(currentOrder.id)
                            ) {
                                // buang payment dari backstack biar back gak balik ke sini
                                popUpTo(Screen.Checkout.route) { inclusive = false }
                            }
                        }
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "I've Paid",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun PayRow(
    label: String,
    value: String,
    highlight: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = TextSecondary, fontSize = 13.sp)
        Text(
            text = value,
            color = if (highlight) Orange else TextPrimary,
            fontSize = if (highlight) 16.sp else 13.sp,
            fontWeight = if (highlight) FontWeight.Bold else FontWeight.Medium
        )
    }
}