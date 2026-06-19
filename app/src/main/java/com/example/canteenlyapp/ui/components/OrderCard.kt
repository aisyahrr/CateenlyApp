package com.example.canteenlyapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.canteenlyapp.data.model.Order
import com.example.canteenlyapp.utils.getMenuImage

@Composable
fun OrderCard(
    order: Order,
    isHistory: Boolean = false,
    onClick: () -> Unit
) {

    val context = LocalContext.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF111827)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {

        Box {

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
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {

                    Row(
                        modifier = Modifier
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Image(
                            painter = painterResource(
                                id = getCanteenImage(
                                    order.canteenImageKey
                                )
                            ),
                            contentDescription = null,
                            modifier = Modifier
                                .size(28.dp)
                                .clip(
                                    RoundedCornerShape(6.dp)
                                ),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(
                            modifier = Modifier.width(8.dp)
                        )

                        Column {

                            Text(
                                text = order.canteenName,
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )

                            Text(
                                text = "Official",
                                color = Color(0xFFFF9800),
                                fontSize = 10.sp
                            )
                        }
                    }
                }

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                // ITEMS
                order.items.take(2).forEachIndexed { index, item ->

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
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
                                    RoundedCornerShape(6.dp)
                                ),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(
                            modifier = Modifier.width(10.dp)
                        )

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {

                            Text(
                                text = item.name,
                                color = Color.White,
                                fontSize = 11.sp
                            )

                            Spacer(
                                modifier = Modifier.height(2.dp)
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Text(
                                    text = "$${"%.2f".format(item.price)}",
                                    color = Color.Gray,
                                    fontSize = 10.sp
                                )

                                Text(
                                    text = "  •  ",
                                    color = Color.Gray,
                                    fontSize = 10.sp
                                )

                                Text(
                                    text = "Qty : ${item.quantity}",
                                    color = Color.Gray,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }

                    if (index < order.items.take(2).lastIndex) {

                        HorizontalDivider(
                            modifier = Modifier.padding(
                                vertical = 10.dp
                            ),
                            color = Color(0xFF1E293B)
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                HorizontalDivider(
                    modifier = Modifier.padding(
                        vertical = 12.dp
                    ),
                    color = Color(0xFF1E293B),
                    thickness = 1.dp
                )

                // FOOTER
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    if (!isHistory) {

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
                            text = "Ready in ",
                            color = Color.Gray,
                            fontSize = 11.sp
                        )

                        Text(
                            text = "${order.estimatedMinutes}",
                            color = Color(0xFFFF9800),
                            fontSize = 11.sp
                        )

                        Text(
                            text = " min",
                            color = Color.Gray,
                            fontSize = 11.sp
                        )

                    } else {

                        Text(
                            text = "Completed",
                            color = Color.Gray,
                            fontSize = 11.sp
                        )
                    }

                    Spacer(
                        modifier = Modifier.weight(1f)
                    )

                    Button(
                        onClick = onClick,
                        modifier = Modifier.height(32.dp),
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
                            text =
                                if (isHistory)
                                    "Review"
                                else
                                    "Order Details",
                            fontSize = 11.sp
                        )
                    }
                }
            }
            StatusBadge(
                status = order.status,
                modifier = Modifier
                    .align(Alignment.TopEnd)
            )

        }
    }
}

@Composable
private fun StatusBadge(
    status: String,
    modifier: Modifier = Modifier
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
        modifier = modifier
            .background(
                bgColor,
                RoundedCornerShape(
                    topEnd = 12.dp,
                    bottomStart = 12.dp
                )
            )
            .padding(
                horizontal = 14.dp,
                vertical = 6.dp
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