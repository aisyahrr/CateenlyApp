package com.example.canteenlyapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CouponSection(
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(5.dp))
                .background(Color(0xFF5C5C5C))
                .clickable { onClick() }
                .padding(horizontal = 24.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Apply Coupon Code",
                color = Color.White,
                fontSize = 13.sp,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.White
            )
        }

        Box(
            modifier = Modifier
                .size(20.dp)
                .offset(x = (-7).dp, y = -1.dp)
                .background(
                    Color(0xFF0E1116),
                    CircleShape
                )
                .align(Alignment.CenterStart)
        )

        Box(
            modifier = Modifier
                .size(20.dp)
                .offset(x = 7.dp, y = -1.dp)
                .background(
                    Color(0xFF0E1116),
                    CircleShape
                )
                .align(Alignment.CenterEnd)
        )
    }
}