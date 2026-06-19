package com.example.canteenlyapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.canteenlyapp.R
import com.example.canteenlyapp.data.model.Canteen
import com.example.canteenlyapp.data.model.CartItem

fun getCanteenImage(imageKey: String): Int {
    return when (imageKey) {
        "palette_gelato" -> R.drawable.palette_gelato
        "crumb_crust" -> R.drawable.crumb_crust
        "leaf_brew" -> R.drawable.leaf_brew
        "urban_bites" -> R.drawable.urban_bites
        else -> R.drawable.palette_gelato
    }
}
@Composable
fun CheckoutShopCard(
    items: List<CartItem>
){
    var canteen by remember {
        mutableStateOf<Canteen?>(null)
    }

    val firstItem = items.first()
    Card(
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
        shape = RoundedCornerShape(14.dp)
    ) {

        Column(
            modifier = Modifier
                .padding(12.dp)
        ) {

            // Header Kantin
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        Color(0xFF2D3748),
                        RoundedCornerShape(8.dp)
                    )
                    .padding(10.dp)
            ) {

                Image(
                    painter = painterResource(
                        getCanteenImage(
                            firstItem.canteenImageKey
                        )
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(6.dp))
                )

                Spacer(
                    modifier = Modifier.width(10.dp)
                )

                Column {

                    Text(
                        text = firstItem.canteenName,
                        color = Color.White
                    )

                    Text(
                        text = "Official",
                        color = Color(0xFFFF9800),
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )
            Column {

                items.forEachIndexed { index, item ->

                    CheckoutItemCard(
                        item = item
                    )

                    if (index != items.lastIndex) {

                        HorizontalDivider(
                            modifier = Modifier.padding(
                                vertical = 12.dp
                            ),
                            color = Color(0xFF1E293B),
                            thickness = 1.dp
                        )
                    }
                }
            }
        }
    }
}