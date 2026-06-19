package com.example.canteenlyapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun OrderSummarySection(
    subtotal: Double,
    discount: Double,
    deliveryFee: Double,
    tax: Double
) {

    Column {

        DashedDivider()

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        SummaryRow(
            title = "Subtotal",
            value = "$${"%.2f".format(subtotal)}"
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        SummaryRow(
            title = "Discount",
            value = "$${"%.2f".format(discount)}"
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        SummaryRow(
            title = "Delivery Fee",
            value = "$${"%.2f".format(deliveryFee)}"
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        SummaryRow(
            title = "Tax",
            value = "$${"%.2f".format(tax)}"
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        DashedDivider()
    }
}

@Composable
fun SummaryRow(
    title: String,
    value: String
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement =
            Arrangement.SpaceBetween
    ) {

        Text(
            text = title,
            color = Color.White,
        )

        Text(
            text = value,
            color = Color.White
        )
    }
}