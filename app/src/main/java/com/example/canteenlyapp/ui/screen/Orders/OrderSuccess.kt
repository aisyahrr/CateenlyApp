package com.example.canteenlyapp.ui.screen.order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.canteenlyapp.ui.navigation.Screen

@Composable
fun OrderSuccessScreen(
    navController: NavController
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0E1116))
            .padding(24.dp),

        verticalArrangement =
            Arrangement.Center,

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = Color(0xFF4CAF50),
            modifier = Modifier.size(120.dp)
        )

        Text(
            text = "Order Placed Successfully!",
            color = Color.White,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF111827)
            ),
            shape = RoundedCornerShape(16.dp)
        ) {

            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Order ID",
                    color = Color.Gray
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = "#ORD-29481",
                    color = Color(0xFFFF9800),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Text(
                    text = "Estimated Time",
                    color = Color.Gray
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = "15 Minutes",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Text(
            text = "Your order has been received and is now being prepared.",
            color = Color.Gray,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(
            modifier = Modifier.height(40.dp)
        )

        Button(
            onClick = {

                navController.navigate(
                    Screen.Orders.route
                ) {
                    popUpTo(
                        Screen.Checkout.route
                    ) {
                        inclusive = true
                    }
                }
            },

            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),

            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFF9800)
            ),

            shape = RoundedCornerShape(14.dp)
        ) {

            Text(
                text = "Track Order",
                color = Color.White
            )
        }
    }
}