package com.example.canteenlyapp.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun CheckoutButton(
    navController: NavController
) {

    Button(
        onClick = {
            navController.navigate("checkout")
        },

        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(50.dp),

        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFFF8C00)
        ),

        shape = RoundedCornerShape(12.dp)
    ) {

        Text(
            text = "Checkout",
            color = Color.White
        )
    }
}
