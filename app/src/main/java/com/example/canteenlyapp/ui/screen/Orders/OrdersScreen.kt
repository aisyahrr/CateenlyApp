package com.example.canteenlyapp.ui.screen.Orders

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.canteenlyapp.data.model.Order
import com.example.canteenlyapp.data.repository.AuthRepository
import com.example.canteenlyapp.data.repository.OrderRepository
import com.example.canteenlyapp.ui.components.DashedDivider
import com.example.canteenlyapp.ui.components.OrderCard
import com.example.canteenlyapp.ui.components.OrderFilterBar

@Composable
fun OrdersScreen(
    navController: NavController
) {

    var selectedTab by remember {
        mutableStateOf("Active")
    }

    val authRepository = remember {
        AuthRepository()
    }

    var orders by remember {
        mutableStateOf<List<Order>>(emptyList())
    }

    val activeOrders =
        orders.filter {
            it.status in listOf(
                "Preparing",
                "On The Way",
                "Ready Pickup"
            )
        }

    val historyOrders =
        orders.filter {
            it.status in listOf(
                "Done",
                "Cancelled"
            )
        }

    LaunchedEffect(Unit) {

        val currentUser =
            authRepository.getCurrentUser()

        if (currentUser != null) {

            OrderRepository.getOrdersByUserId(
                currentUser.uid
            ) {
                orders = it
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0E1116))
            .navigationBarsPadding()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFF9800))
                .padding(top = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 20.dp
                    )
            ) {

                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .size(20.dp)
                        .clickable {
                            navController.popBackStack()
                        }
                )

                Text(
                    text = "Orders",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.align(
                        Alignment.Center
                    )
                )
            }
        }

        DashedDivider()

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(
                horizontal = 16.dp,
                vertical = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            if (
                selectedTab == "Active" &&
                activeOrders.isEmpty()
            ) {

                item {

                    Box(
                        modifier = Modifier
                            .fillParentMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = "No active orders",
                            color = Color.Gray
                        )
                    }
                }

            } else if (
                selectedTab == "History" &&
                historyOrders.isEmpty()
            ) {

                item {

                    Box(
                        modifier = Modifier
                            .fillParentMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = "No order history",
                            color = Color.Gray
                        )
                    }
                }

            } else {

                if (selectedTab == "Active") {

                    items(activeOrders) { order ->

                        OrderCard(
                            order = order,
                            isHistory = false,
                            onClick = {
                                // TODO:
                                // navigate detail order
                            }
                        )
                    }

                } else {

                    items(historyOrders) { order ->

                        OrderCard(
                            order = order,
                            isHistory = true,
                            onClick = {
                                // TODO:
                                // review order
                            }
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            contentAlignment = Alignment.Center
        ) {

            OrderFilterBar(
                selectedTab = selectedTab,
                onTabSelected = {
                    selectedTab = it
                }
            )
        }
    }
}