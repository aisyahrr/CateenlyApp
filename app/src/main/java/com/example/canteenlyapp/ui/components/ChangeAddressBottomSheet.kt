package com.example.canteenlyapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ChangeAddressBottomSheet(
    currentAddress: String,
    onSave: (String) -> Unit
) {

    var address by remember {
        mutableStateOf(currentAddress)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp , horizontal = 20.dp)
    ) {

        Text(
            text = "Delivery Address",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = address,
            onValueChange = {
                address = it
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Address")
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,

                focusedLabelColor = Color(0xFFFF9800),
                unfocusedLabelColor = Color.Gray,

                focusedBorderColor = Color(0xFFFF9800),
                unfocusedBorderColor = Color.Gray,

                cursorColor = Color(0xFFFF9800)
            ),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                onSave(address)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFF9800)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Save Address")
        }
    }
}