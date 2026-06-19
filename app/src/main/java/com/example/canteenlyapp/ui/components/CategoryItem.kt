package com.example.canteenlyapp.ui.components

import android.text.Layout
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import com.example.canteenlyapp.data.model.Category

@Composable
fun CategoryItem(
    category: Category,
    isSelected: Boolean,
    onClick: () -> Unit
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable {
                onClick()
            }
    ) {

        Box(
            modifier = Modifier
                .size(78.dp)
                .clip(CircleShape)
                .border(
                    width = if (isSelected) 2.dp else 0.dp,
                    color = if (isSelected)
                        Color(0xFFFF7A00)
                    else
                        Color.Transparent,
                    shape = CircleShape
                )
                .padding(4.dp)
        ) {

            Box(
                modifier = Modifier
                    .size(82.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1E2940)),
                contentAlignment = Alignment.Center
            ) {

                Image(
                    painter = painterResource(category.image),
                    contentDescription = category.name,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = category.name,
            color =
                if (isSelected)
                    Color.White
                else
                    Color.LightGray,
            fontSize = 12.sp
        )
    }
}