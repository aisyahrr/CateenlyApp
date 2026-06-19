package com.example.canteenlyapp.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.canteenlyapp.ui.theme.NavyDark

@Composable
fun SearchBar() {

    var search by remember {
        mutableStateOf("")
    }

    TextField(
        value = search,

        onValueChange = {
            search = it
        },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),

        shape = RoundedCornerShape(10.dp),

        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color.Gray
            )
        },

        placeholder = {
            Text(
                text = "Search canteen or menu...",
                color = Color.Gray,
                fontSize = 14.sp
            )
        },

        colors = TextFieldDefaults.colors(

            focusedContainerColor = NavyDark,
            unfocusedContainerColor = NavyDark,

            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,

            cursorColor = Color.White,

            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}