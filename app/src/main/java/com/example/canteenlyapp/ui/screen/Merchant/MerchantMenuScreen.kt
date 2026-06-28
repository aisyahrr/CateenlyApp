package com.example.canteenlyapp.ui.screen.Merchant

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.canteenlyapp.data.model.Menu
import com.example.canteenlyapp.data.repository.AuthRepository
import com.example.canteenlyapp.data.repository.FoodRepository
import com.example.canteenlyapp.ui.components.MerchantMenuCard

@Composable
fun MerchantMenuScreen() {

    val authRepository = remember {
        AuthRepository()
    }

    val foodRepository = remember {
        FoodRepository()
    }

    var menus by remember {
        mutableStateOf<List<Menu>>(emptyList())
    }

    LaunchedEffect(Unit) {

        val merchant =
            authRepository.getCurrentMerchant()

        merchant?.let {

            foodRepository.getMenusByCanteenId(
                it.canteenId
            ) {
                menus = it
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        LazyColumn(
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = 20.dp
            ),

            verticalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {

            items(menus) { menu ->

                MerchantMenuCard(
                    menu = menu,

                    onEditClick = {
                        // TODO
                    },

                    onDeleteClick = {
                        // TODO
                    }
                )
            }
        }

        FloatingActionButton(
            onClick = { },

            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp),

            containerColor = Color(0xFFFF9800),

            shape = CircleShape
        ) {

            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}