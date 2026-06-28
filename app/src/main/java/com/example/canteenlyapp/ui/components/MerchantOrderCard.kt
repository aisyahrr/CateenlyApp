package com.example.canteenlyapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.canteenlyapp.data.model.Order
import com.example.canteenlyapp.utils.getMenuImage

@Composable
fun MerchantOrderCard(
    order: Order,
    onCardClick: () -> Unit = {},
    onActionClick: () -> Unit
) {

    val context = LocalContext.current

    val actionText = when {

        order.status == "Preparing"
                && order.deliveryMethod == "Pickup" ->
            "Ready Pickup"

        order.status == "Preparing"
                && order.deliveryMethod == "Delivery" ->
            "Send Order"

        else ->
            null
    }

    Card(
        onClick = onCardClick,
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                Color(0xFF2D3748),
                RoundedCornerShape(8.dp)
            ),

        colors = CardDefaults.cardColors(
            containerColor = Color(0x00111827)
        ),

        shape = RoundedCornerShape(12.dp)
    ) {

        Column(
            modifier = Modifier.padding(12.dp)
        ) {

            // HEADER
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        Color(0xFF2D3748),
                        RoundedCornerShape(8.dp)
                    )
                    .padding(10.dp),
                horizontalArrangement =
                    Arrangement.SpaceBetween
            ) {

                Column {

                    Text(
                        text = "Order #${order.orderNumber}",

                        color = Color.White,

                        fontWeight =
                            FontWeight.SemiBold
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector =
                                if (order.deliveryMethod == "Pickup")
                                    Icons.Default.Store
                                else
                                    Icons.Default.DeliveryDining,

                            contentDescription = null,

                            tint = Color(0xFFFF9800),

                            modifier = Modifier.size(14.dp)
                        )

                        Spacer(
                            modifier = Modifier.width(4.dp)
                        )

                        Text(
                            text =
                                if (order.deliveryMethod == "Pickup")
                                    "Pickup"
                                else
                                    "Delivery",

                            color = Color(0xFFFF9800),

                            fontSize = 11.sp,

                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                StatusBadge(
                    status = order.status
                )
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            // ITEMS
            order.items.take(2)
                .forEachIndexed { index, item ->

                    Row(
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Image(
                            painter = painterResource(
                                id = getMenuImage(
                                    context,
                                    item.imageKey
                                )
                            ),

                            contentDescription = null,

                            modifier = Modifier
                                .size(50.dp)
                                .clip(
                                    RoundedCornerShape(8.dp)
                                ),

                            contentScale =
                                ContentScale.Crop
                        )

                        Spacer(
                            modifier =
                                Modifier.width(10.dp)
                        )

                        Column(
                            modifier =
                                Modifier.weight(1f)
                        ) {

                            Text(
                                text = item.name,
                                color = Color.White,
                                fontSize = 13.sp
                            )
                            if (item.selectedOptions.isNotEmpty()) {

                                Spacer(
                                    modifier = Modifier.height(4.dp)
                                )

                                Text(
                                    text = item.selectedOptions.joinToString(" • ") { option ->
                                        if (option.extraPrice > 0)
                                            "${option.name} (+$${option.extraPrice})"
                                        else
                                            option.name
                                    },
                                    color = Color.Gray,
                                    fontSize = 11.sp,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Spacer(
                                modifier = Modifier.height(8.dp)
                            )

                            Text(
                                text =
                                    "$${item.price} • Qty: ${item.quantity}",

                                color = Color.Gray,

                                fontSize = 11.sp
                            )
                        }
                    }

                    if (index <
                        order.items.take(2).lastIndex
                    ) {

                        HorizontalDivider(
                            modifier = Modifier
                                .padding(vertical = 10.dp),

                            color = Color(0xFF1E293B)
                        )
                    }
                }

            Spacer(
                modifier = Modifier.height(12.dp)
            )
            // FOOTER
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 10.dp),
                color = Color(0xFF1E293B)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column {

                    Text(
                        text = "Total",
                        color = Color.Gray,
                        fontSize = 11.sp
                    )

                    Text(
                        text = "$${"%.2f".format(order.total)}",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(14.dp)
                    )

                    Spacer(
                        modifier = Modifier.width(4.dp)
                    )

                    Text(
                        text = "Ready in ${order.estimatedMinutes} min",
                        color = Color.Gray,
                        fontSize = 11.sp
                    )
                }
            }
            Spacer(
                modifier = Modifier.height(12.dp)
            )
            actionText?.let {

                Button(
                    onClick = onActionClick,

                    modifier = Modifier
                        .height(32.dp)
                        .fillMaxWidth(),

                    contentPadding = PaddingValues(
                        horizontal = 14.dp,
                        vertical = 0.dp
                    ),

                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF9800)
                    ),

                    shape = RoundedCornerShape(8.dp)
                ) {

                    Text(
                        text = it,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusBadge(
    status: String
) {

    val bgColor = when (status) {
        "Preparing" -> Color(0xFFFFE0B2)
        "On The Way" -> Color(0xFFC8E6C9)
        "Ready Pickup" -> Color(0xFFBBDEFB)
        "Done" -> Color(0xFFC8E6C9)
        else -> Color.LightGray
    }

    val textColor = when (status) {
        "Preparing" -> Color(0xFFFF9800)
        "On The Way" -> Color(0xFF4CAF50)
        "Ready Pickup" -> Color(0xFF2196F3)
        "Done" -> Color(0xFF4CAF50)
        else -> Color.Gray
    }

    Box(
        modifier = Modifier
            .background(
                bgColor,
                RoundedCornerShape(6.dp)
            )
            .padding(
                horizontal = 10.dp,
                vertical = 4.dp
            )
    ) {

        Text(
            text = status,
            color = textColor,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}