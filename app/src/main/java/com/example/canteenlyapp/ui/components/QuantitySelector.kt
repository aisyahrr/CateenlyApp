package com.example.canteenlyapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun QuantitySelector(
    quantity: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit
) {

    Row(
        modifier = Modifier
            .padding(horizontal = 6.dp, vertical = 2.dp)
            .height(30.dp)
            .border(
                width = 1.dp,
                color = Color(0xFFFF7A00),
                shape = RoundedCornerShape(6.dp)
            )
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(14.dp)
                .background(
                    Color(0xFFFF7A00),
                    RoundedCornerShape(3.dp)
                )
                .clickable {
                    onDecrease()
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "-",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Text(
            text = quantity.toString(),
            color = Color.White,
            fontSize = 10.sp,
            modifier = Modifier.padding(horizontal = 8.dp),
            fontWeight = FontWeight.SemiBold
        )

        Box(
            modifier = Modifier
                .size(14.dp)
                .background(
                    Color(0xFFFF7A00),
                    RoundedCornerShape(3.dp)
                )
                .clickable {
                    onIncrease()
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "+",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}