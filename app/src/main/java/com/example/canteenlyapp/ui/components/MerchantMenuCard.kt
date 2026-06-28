package com.example.canteenlyapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.canteenlyapp.data.model.Menu
import com.example.canteenlyapp.utils.getMenuImage

@Composable
fun MerchantMenuCard(
    menu: Menu,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {

    val context = LocalContext.current
    val imageRes = getMenuImage(context, menu.imageKey)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0B1220)
        ),

        shape = RoundedCornerShape(14.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),

            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(id = imageRes),
                contentDescription = menu.name,

                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(12.dp)),

                contentScale = ContentScale.Crop
            )

            Spacer(
                modifier = Modifier.width(12.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                // ---- name + status badge ----
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = menu.name,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )

                    Box(
                        modifier = Modifier
                            .background(
                                if (menu.isAvailable)
                                    Color(0xFF1E3A2F)
                                else
                                    Color(0xFF3A1E1E),

                                RoundedCornerShape(20.dp)
                            )
                            .padding(
                                horizontal = 8.dp,
                                vertical = 3.dp
                            )
                    ) {

                        Text(
                            text =
                                if (menu.isAvailable)
                                    "Available"
                                else
                                    "Out of Stock",

                            color =
                                if (menu.isAvailable)
                                    Color(0xFF4CAF50)
                                else
                                    Color(0xFFE53935),

                            fontSize = 10.sp,
                            maxLines = 1
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = menu.description,
                    color = Color.LightGray,
                    fontSize = 13.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.SpaceBetween,

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Text(
                        text = "$${menu.price}",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        Text(
                            text = "Edit",
                            color = Color.White,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .background(
                                    Color(0xFF2563EB),
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable {
                                    onEditClick()
                                }
                                .padding(
                                    horizontal = 12.dp,
                                    vertical = 6.dp
                                )
                        )

                        Text(
                            text = "Delete",
                            color = Color.White,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .background(
                                    Color(0xFFE53935),
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable {
                                    onDeleteClick()
                                }
                                .padding(
                                    horizontal = 12.dp,
                                    vertical = 6.dp
                                )
                        )
                    }
                }
            }
        }
    }
}