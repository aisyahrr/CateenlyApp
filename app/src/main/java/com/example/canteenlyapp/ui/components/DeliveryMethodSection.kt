package com.example.canteenlyapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DeliveryMethodSection(
    selectedMethod: String,
    onMethodSelected: (String) -> Unit
) {

    DeliveryMethodItem(
        title = "Delivery",
        isSelected = selectedMethod == "Delivery",
        onClick = {
            onMethodSelected("Delivery")
        }
    )
    HorizontalDivider(
        modifier = Modifier.padding(
            vertical = 10.dp
        ),
        color = Color(0xFF1E293B),
        thickness = 1.dp
    )

    DeliveryMethodItem(
        title = "Pickup",
        isSelected = selectedMethod == "Pickup",
        onClick = {
            onMethodSelected("Pickup")
        }
    )
}

@Composable
private fun DeliveryMethodItem(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector =
                if (title == "Delivery")
                    Icons.Default.DeliveryDining
                else
                    Icons.Default.Storefront,

            contentDescription = null,

            tint = Color(0xFFFF9800),

            modifier = Modifier.size(20.dp)
        )

        Spacer(
            modifier = Modifier.width(12.dp)
        )

        Text(
            text = title,
            color = Color.White,
            fontSize = 13.sp,
            modifier = Modifier.weight(1f)
        )

        Icon(
            imageVector =
                if (isSelected)
                    Icons.Default.RadioButtonChecked
                else
                    Icons.Default.RadioButtonUnchecked,

            contentDescription = null,

            tint =
                if (isSelected)
                    Color(0xFFFF9800)
                else
                    Color.Gray
        )
    }
}