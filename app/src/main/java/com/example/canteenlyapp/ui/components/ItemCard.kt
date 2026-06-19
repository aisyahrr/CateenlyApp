package com.example.canteenlyapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.canteenlyapp.data.model.CartItem
import com.example.canteenlyapp.utils.getMenuImage


@Composable
fun CartItemCard(
    item: CartItem,
    onCheckedChange: (Boolean) -> Unit,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF141A22)
        ),

        shape = RoundedCornerShape(12.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(18.dp)
                    .border(
                        1.dp,
                        if (item.isSelected)
                            Color(0xFFFF8C00)
                        else
                            Color(0xFF3A4452),
                        RoundedCornerShape(4.dp)
                    )
                    .background(
                        if (item.isSelected)
                            Color(0xFFFF8C00)
                        else
                            Color.Transparent,
                        RoundedCornerShape(4.dp)
                    )
                    .clickable {
                        onCheckedChange(!item.isSelected)
                    },
                contentAlignment = Alignment.Center
            ) {
                if (item.isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))
            Row(
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = Color(0xFF3A4452),
                        shape = RoundedCornerShape(6.dp)
                    )
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically

            ) {

                Image(
                    painter = painterResource(
                        id = getMenuImage(
                        context,
                        item.imageKey
                    )),
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = item.name,
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )

                    if (item.selectedOptions.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(2.dp))

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

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "$${"%.2f".format(item.totalPrice)}",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                    )
                }

                QuantitySelector(
                    quantity = item.quantity,
                    onIncrease = onIncrease,
                    onDecrease = onDecrease
                )
            }
        }
    }

}

