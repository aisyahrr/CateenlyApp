package com.example.canteenlyapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.canteenlyapp.data.model.Menu
import com.example.canteenlyapp.data.model.MenuOptionGroup
import com.example.canteenlyapp.data.model.MenuOptionItem
import com.example.canteenlyapp.utils.getMenuImage

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun CartBottomSheet(
    menu: Menu,
    optionGroups: List<MenuOptionGroup>,
    onDismiss: () -> Unit,
    onAddToCart: (
        Menu,
        Int,
        List<MenuOptionItem>
    ) -> Unit,
){

    var quantity by remember {
        mutableIntStateOf(1)
    }

    val context = LocalContext.current
    val imageRes = getMenuImage(context, menu.imageKey)
    val selectedOptions =
        remember {
            mutableStateMapOf<String, MenuOptionItem>()
        }

    val extraPrice =
        selectedOptions.values.sumOf {
            it.extraPrice
        }

    val subtotal =
        (menu.price + extraPrice) * quantity
    val totalItemPrice =
        menu.price + selectedOptions.values.sumOf { it.extraPrice }


    ModalBottomSheet(
        onDismissRequest = onDismiss,

        containerColor = Color(0xFF0F172A),

        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .width(40.dp)
                    .height(4.dp)
                    .background(
                        Color.Gray,
                        RoundedCornerShape(100.dp)
                    )
            )
        }

    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)

        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = menu.name,
                    modifier = Modifier
                        .size(90.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(
                ){
                    Text(
                        text = menu.name,
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = "$${menu.price}",
                        color = Color(0xFFFF7A00),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(
                        modifier = Modifier.height(14.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically ,
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween

                        ) {

                            Text(
                                text = "Qty : ",
                                color = Color(0xFFFF7A00),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )

                            Row(
                                modifier = Modifier
                                    .height(36.dp)
                                    .background(
                                        Color(0xFF1E293B),
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(horizontal = 6.dp),

                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Box(
                                    modifier = Modifier
                                        .size(22.dp)
                                        .background(
                                            Color(0xFFFF7A00),
                                            RoundedCornerShape(4.dp)
                                        )
                                        .clickable {
                                            if (quantity > 1) quantity--
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "-",
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Text(
                                    text = quantity.toString(),
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 20.dp),
                                    fontWeight = FontWeight.SemiBold
                                )

                                Box(
                                    modifier = Modifier
                                        .size(22.dp)
                                        .background(
                                            Color(0xFFFF7A00),
                                            RoundedCornerShape(4.dp)
                                        )
                                        .clickable {
                                            quantity++
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "+",
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
            Spacer(
                modifier = Modifier.height(16.dp)
            )
            HorizontalDivider(
                color = Color(0xFF1E293B)
            )

            if (optionGroups.isNotEmpty()) {

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                optionGroups.forEach { group ->

                    Text(
                        text = group.title,
                        color = Color.White,
                        fontSize = 14.sp
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {

                        group.options.values.forEach { option ->

                            OutlinedButton(
                                onClick = {
                                    selectedOptions[group.title] = option
                                },

                                border = BorderStroke(
                                    1.dp,
                                    Color(0xFFFF7A00)
                                ),

                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor =
                                        if (selectedOptions[group.title] == option)
                                            Color(0xFFFF7A00)
                                        else
                                            Color.Transparent,

                                    contentColor = Color.White
                                )
                            ) {
                                Text(option.name)
                            }
                        }
                    }

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )
                }
            }
            HorizontalDivider(
                color = Color(0xFF1E293B)
            )
            Spacer(
                modifier = Modifier.height(10.dp)
            )


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = "Subtotal",
                    color = Color.LightGray
                )

                Text(
                    text = "$${subtotal}",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Button(
                onClick = {
                    onAddToCart(
                        menu.copy(price = totalItemPrice),
                        quantity,
                        selectedOptions.values.toList()
                    )
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),

                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF7A00)
                ),

                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Add to Cart",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
