package com.example.canteenlyapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.canteenlyapp.data.model.Canteen
import com.example.canteenlyapp.data.model.CartItem
import com.example.canteenlyapp.data.repository.CartRepository
import java.text.NumberFormat
import java.util.Locale


@Composable

fun SummarySection(
    canteens: List<Canteen>
){

    val cartItems = CartRepository.cartItems

    val selectedItems = cartItems.filter {
        it.isSelected
    }
    val groupedItems =
        selectedItems.groupBy {
            it.canteenId
        }

    val hasSelectedItems = selectedItems.isNotEmpty()

    val deliveryFee =
        groupedItems.keys.sumOf { canteenId ->

            canteens.find {
                it.id == canteenId
            }?.deliveryFee ?: 0.0
        }
    val subtotal = selectedItems.sumOf {
        it.price * it.quantity
    }

    val discount = 0.0

    val tax =
        if (hasSelectedItems)
            subtotal * 0.1
        else
            0.0

    val grandTotal =
        subtotal -
                discount +
                deliveryFee +
                tax
    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {

        SummaryRow(
            title = "Subtotal",
            value = "$${"%.2f".format(subtotal)}"
        )

        Spacer(modifier = Modifier.height(8.dp))

        SummaryRow(
            title = "Discount",
            value = "$${"%.2f".format(discount)}"
        )

        Spacer(modifier = Modifier.height(8.dp))

        SummaryRow(
            title = "Delivery Fee",
            value = "$${"%.2f".format(deliveryFee)}"
        )

        Spacer(modifier = Modifier.height(8.dp))

        SummaryRow(
            title = "Tax",
            value = "$${"%.2f".format(tax)}"
        )

        Spacer(modifier = Modifier.height(15.dp))

        DashedDivider()

        Spacer(modifier = Modifier.height(13.dp))

        SummaryRow(
            title = "Grand Total",
            value = "$${"%.2f".format(grandTotal)}",
            isBold = true
        )
    }
}
@Composable
fun SummaryRow(
    title: String,
    value: String,
    isBold: Boolean = false
) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            title,
            color = Color.White,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
        )

        Text(
            value,
            color = Color.White,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
        )
    }
}