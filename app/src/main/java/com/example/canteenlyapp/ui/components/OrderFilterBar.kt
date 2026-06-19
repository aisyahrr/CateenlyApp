package com.example.canteenlyapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun OrderFilterBar(
    selectedTab: String,
    onTabSelected: (String) -> Unit
) {

    Row(
        modifier = Modifier
            .background(
                Color(0xFF1F2637),
                RoundedCornerShape(30.dp)
            )
            .padding(4.dp)
    ) {

        FilterButton(
            text = "Active",
            selected = selectedTab == "Active"
        ) {
            onTabSelected("Active")
        }

        FilterButton(
            text = "History",
            selected = selectedTab == "History"
        ) {
            onTabSelected("History")
        }
    }
}
@Composable
fun FilterButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(24.dp))
            .background(
                if (selected)
                    Color(0xFFFF9800)
                else
                    Color.Transparent
            )
            .clickable {
                onClick()
            }
            .padding(
                horizontal = 24.dp,
                vertical = 10.dp
            ),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = text,
            color = Color.White
        )
    }
}