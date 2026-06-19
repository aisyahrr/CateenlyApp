package com.example.canteenlyapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.canteenlyapp.data.model.CartItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeableCartItem(
    item: CartItem,
    onDelete: () -> Unit,
    onCheckedChange: (Boolean) -> Unit,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit
) {

    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.EndToStart) {
                onDelete()
            }
            true
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,

        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .background(
                        Color.Red,
                        RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.CenterEnd
            ) {

                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.padding(end = 24.dp)
                )
            }
        }
    ) {
        CartItemCard(
            item = item,
            onCheckedChange = onCheckedChange,
            onIncrease = onIncrease,
            onDecrease = onDecrease

        )
    }
}