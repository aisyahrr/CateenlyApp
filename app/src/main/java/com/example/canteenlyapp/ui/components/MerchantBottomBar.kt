package com.example.canteenlyapp.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.canteenlyapp.ui.theme.NavyDark

data class MerchantNavItem(
    val title: String,
    val icon: ImageVector
)

@Composable
fun MerchantBottomBar(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
) {

    val items = listOf(
        MerchantNavItem("Dashboard", Icons.Default.Home),
        MerchantNavItem("Orders", Icons.Default.ReceiptLong),
        MerchantNavItem("Menu", Icons.Default.RestaurantMenu),
        MerchantNavItem("Profile", Icons.Default.Person)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 20.dp,
                end = 20.dp,
                bottom = 16.dp
            )
            .navigationBarsPadding()
            .height(64.dp)
            .clip(RoundedCornerShape(50))
            .background(NavyDark)
            .padding(horizontal = 16.dp),

        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        items.forEachIndexed { index, item ->

            val selected = selectedIndex == index

            val bgColor by animateColorAsState(
                targetValue =
                    if (selected)
                        Color(0xFFFF7A00)
                    else
                        Color.Transparent,
                label = ""
            )

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(bgColor)
                    .clickable {
                        onItemSelected(index)
                    }
                    .padding(
                        horizontal = 14.dp,
                        vertical = 10.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title,
                    tint = Color.White
                )

                AnimatedVisibility(selected) {

                    Text(
                        text = item.title,
                        color = Color.White,
                        modifier = Modifier.padding(start = 6.dp)
                    )
                }
            }
        }
    }
}