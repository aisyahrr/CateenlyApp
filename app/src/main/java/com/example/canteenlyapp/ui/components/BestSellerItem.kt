package com.example.canteenlyapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.canteenlyapp.R
import com.example.canteenlyapp.data.model.Menu
import com.example.canteenlyapp.ui.theme.OrangePrimary
import com.example.canteenlyapp.utils.getMenuImage


@Composable
fun BestSellerItem(
    menu: Menu,
    rank: Int,
    onAddToCart: () -> Unit
) {
    val context = LocalContext.current
    val imageRes = getMenuImage(context, menu.imageKey)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),

        horizontalArrangement =
            Arrangement.SpaceBetween
    ) {

        Column(
            modifier = Modifier.weight(1.5f)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$rank.",
                    color = Color(0xFFFF9800),
                    fontWeight = FontWeight.Bold,
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = menu.name,
                    color = Color.White
                )
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = menu.description,
                color = Color.Gray,
                maxLines = 2
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Row(
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Text(
                        text = "$${menu.price}",
                        color = OrangePrimary
                    )

                }
                Button(
                    onClick = {
                        onAddToCart()
                    },

                    modifier = Modifier
                        .height(30.dp),

                    contentPadding = PaddingValues(
                        horizontal = 12.dp,
                        vertical = 0.dp
                    ),

                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF7A00)
                    ),

                    shape = RoundedCornerShape(6.dp)
                ) {

                    Text(
                        text = "Buy Now",
                        fontSize = 11.sp
                    )
                }
            }

        }

        Spacer(
            modifier = Modifier.width(16.dp)
        )

        Column(
            horizontalAlignment =
                Alignment.End
        ) {

            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                modifier = Modifier
                    .size(105.dp)
                    .clip(
                        RoundedCornerShape(12.dp)
                    ),
                contentScale =
                    ContentScale.Crop
            )
        }
    }
}