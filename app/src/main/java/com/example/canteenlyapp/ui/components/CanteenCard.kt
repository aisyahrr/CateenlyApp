package com.example.canteenlyapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.canteenlyapp.data.model.Canteen
import com.example.canteenlyapp.ui.theme.OrangePrimary
import androidx.compose.foundation.clickable
import com.example.canteenlyapp.R


@Composable
fun CanteenCard(
    canteen: Canteen,
    onClick: () -> Unit
) {
    val imageRes = when (canteen.imageKey) {
        "palette_gelato" -> R.drawable.palette_gelato
        "crumb_crust" -> R.drawable.crumb_crust
        "leaf_brew" -> R.drawable.leaf_brew
        else -> R.drawable.urban_bites
    }
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1C2333)
        ),
        modifier = Modifier
            .width(180.dp)
            .border(
                width = 1.dp,
                color = Color(0x2EBDBFBD),
                shape = RoundedCornerShape(10.dp)
            )
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(10.dp),
    ) {
        Column {
            Box {
                Image(
                    painter = painterResource(imageRes),
                    contentDescription = canteen.title,
                    modifier = Modifier
                        .height(150.dp)
                        .clip(
                            RoundedCornerShape(
                                topStart = 10.dp,
                                topEnd = 10.dp
                            )
                        )
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
                val badgeColor =
                    if (canteen.isAvailable)
                        Color(0xFFEAF7F0)
                    else
                        Color(0xFFFDECEC)

                val borderColor =
                    if (canteen.isAvailable)
                        Color(0xFF036C29)
                    else
                        Color(0xFFD32F2F)

                val textColor =
                    if (canteen.isAvailable)
                        Color(0xFF15803D)
                    else
                        Color(0xFFC62828)
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .clip(
                            RoundedCornerShape(
                                topEnd = 10.dp,
                                bottomStart = 10.dp
                            )
                        )
                        .border(
                            width = 1.dp,
                            color = borderColor,
                            shape = RoundedCornerShape(
                                topEnd = 10.dp,
                                bottomStart = 10.dp
                            )
                        )
                        .background(badgeColor)
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = if (canteen.isAvailable) "Open" else "Closed",
                        color = textColor,
                        fontSize = 12.sp,
                    )

                }
            }
            Spacer(modifier = Modifier.height(3.dp))
            Column(
                modifier = Modifier
                    .padding(
                        vertical = 10.dp,
                        horizontal = 5.dp
                    ),

                verticalArrangement =
                    Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = canteen.title,
                    color = Color.White,
                    fontSize = 14.sp,
                )
                Text(
                    text = canteen.category,
                    color = Color.Gray,
                    fontSize = 12.sp,
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = canteen.deliveryTime,
                            color = OrangePrimary,
                            fontSize = 10.sp,
                        )
                    }
                    Text(
                        text = canteen.priceRange,
                        color = Color.White,
                        fontSize = 12.sp,
                    )
                }
            }

        }
    }
}