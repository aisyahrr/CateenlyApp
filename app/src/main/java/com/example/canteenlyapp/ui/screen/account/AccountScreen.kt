package com.example.canteenlyapp.ui.screen.account

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.canteenlyapp.data.model.User
import com.example.canteenlyapp.ui.components.ProfileSection
import androidx.navigation.compose.rememberNavController
import com.example.canteenlyapp.data.repository.AuthRepository
import com.example.canteenlyapp.ui.components.AccountMenu
import com.example.canteenlyapp.ui.components.AccountSection
import com.example.canteenlyapp.ui.components.EditProfileBottomSheet
import com.example.canteenlyapp.ui.components.PointSummaryCard
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import com.example.canteenlyapp.ui.components.ChangeAddressBottomSheet
import com.example.canteenlyapp.ui.navigation.Screen
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.foundation.shape.RoundedCornerShape

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun AccountScreenPreview() {

    val navController = rememberNavController()

    AccountScreen(
        navController = navController
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(navController: NavHostController)
{
    val authRepository = remember { AuthRepository() }
    val scope = rememberCoroutineScope()

    var currentUser by remember {
        mutableStateOf<User?>(null)
    }

    LaunchedEffect(Unit) {
        currentUser = authRepository.getCurrentUser()
    }
    var showEditProfile by remember {
        mutableStateOf(false)
    }
    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    var showAddressSheet by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 20.dp)
            .background(Color(0xFF0E1116))
    ){
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 15.dp)
        ) {
            item {
                ProfileSection(
                    user = currentUser,
                    onEditClick = {
                        showEditProfile = true
                    }
                )

            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }

            item {
                PointSummaryCard(
                    points = "Rp. 20.000",
                    totalOrder = 2,
                    totalCoupon = 0
                )
            }
            item {

                Spacer(modifier = Modifier.height(20.dp))

                AccountSection(
                    title = "Akun",
                    items = listOf(
                        AccountMenu("Box", Icons.Default.Inventory2),
                        AccountMenu("Change Address", Icons.Default.LocationOn),
                        AccountMenu("Change Language", Icons.Default.Language),
                        AccountMenu("Setting", Icons.Default.Settings)
                    ),

                    onItemClick = { item ->

                        when (item.title) {

                            "Change Address" -> {
                                showAddressSheet = true
                            }

                            "Setting" -> {
                                // nanti setting
                            }

                            "Change Language" -> {
                                // nanti language
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))

                AccountSection(
                    title = "Order",
                    items = listOf(
                        AccountMenu(
                            "Orders History",
                            Icons.Default.ReceiptLong
                        ),
                        AccountMenu(
                            "Payment Method",
                            Icons.Default.CreditCard
                        )
                    ),

                    onItemClick = { item ->

                        when (item.title) {
                            "Orders History" -> {
                                navController.navigate(
                                    Screen.Orders.route
                                )
                            }

                            "Payment Method" -> {
                                // buka payment
                            }
                        }
                    }
                )
            }
            item {
                Button(
                    onClick = {

                        authRepository.logout()

                        navController.navigate(
                            Screen.Login.route
                        ) {
                            popUpTo(0)
                        }
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 16.dp,
                            vertical = 12.dp
                        ),

                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE53935)
                    ),

                    shape = RoundedCornerShape(12.dp)
                ) {

                    Text(
                        text = "Logout",
                        color = Color.White
                    )
                }

            }
            }

    }
    if (showEditProfile) {

        ModalBottomSheet(
            onDismissRequest = {
                showEditProfile = false
            },
            containerColor = Color(0xFF141A22)
        ){

            EditProfileBottomSheet(
                user = currentUser,

                onSave = { fullName ->

                    scope.launch {

                        authRepository.updateFullName(
                            fullName
                        )

                        currentUser =
                            authRepository.getCurrentUser()

                        showEditProfile = false
                    }
                }
            )
        }
    }
    if (showAddressSheet) {

        ModalBottomSheet(
            onDismissRequest = {
                showAddressSheet = false
            },
            containerColor = Color(0xFF141A22)
        ) {

            ChangeAddressBottomSheet(
                currentAddress =
                    currentUser?.address ?: "",

                onSave = { address ->

                    scope.launch {

                        authRepository
                            .updateAddress(address)

                        currentUser =
                            authRepository
                                .getCurrentUser()

                        showAddressSheet = false
                    }
                }
            )
        }
    }

}