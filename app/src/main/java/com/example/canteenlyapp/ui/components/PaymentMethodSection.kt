package com.example.canteenlyapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PaymentMethodSection(
    selectedMethod: String,
    onMethodSelected: (String) -> Unit
) {

    PaymentMethodItem(
        title = "Cash",
        icon = Icons.Default.Money,
        isSelected = selectedMethod == "Cash",
        onClick = {
            onMethodSelected("Cash")
        }
    )

    HorizontalDivider(
        modifier = Modifier.padding(vertical = 10.dp),
        color = Color(0xFF1E293B)
    )

    PaymentMethodItem(
        title = "Qris",
        icon = Icons.Default.QrCode,
        isSelected = selectedMethod == "Credit Card",
        onClick = {
            onMethodSelected("Credit Card")
        }
    )

    HorizontalDivider(
        modifier = Modifier.padding(vertical = 10.dp),
        color = Color(0xFF1E293B)
    )

    PaymentMethodItem(
        title = "Bank Transfer",
        icon = Icons.Default.AccountBalance,
        isSelected = selectedMethod == "Bank Transfer",
        onClick = {
            onMethodSelected("Bank Transfer")
        }
    )
}
@Composable
fun PaymentMethodItem(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFFFF9800)
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