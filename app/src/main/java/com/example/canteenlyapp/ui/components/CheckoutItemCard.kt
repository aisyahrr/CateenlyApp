package com.example.canteenlyapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.canteenlyapp.data.model.CartItem
import com.example.canteenlyapp.utils.getMenuImage

@Composable
fun CheckoutItemCard(
    item: CartItem
) {

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth(),
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
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
                    .size(70.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(
                modifier = Modifier.width(12.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = item.name,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    maxLines = 2
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
                    text = "$${"%.2f".format(item.price)}",
                    color = Color(0xFFFF9800),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Box(
                modifier = Modifier
                    .padding(
                        horizontal = 10.dp,
                        vertical = 6.dp
                    )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "Qty : ",
                        color = Color.White,
                        fontSize = 11.sp
                    )

                    Text(
                        text = "${item.quantity}",
                        color = Color.White,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}