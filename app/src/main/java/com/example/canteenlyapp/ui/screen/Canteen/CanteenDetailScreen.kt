package com.example.canteenlyapp.ui.screen.Canteen

import androidx.compose.foundation.BorderStroke
import com.example.canteenlyapp.data.repository.FoodRepository
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.canteenlyapp.ui.theme.DarkGray
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import com.example.canteenlyapp.ui.components.BestSellerItem
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.canteenlyapp.data.model.Canteen
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.mutableStateListOf
import androidx.navigation.NavController
import com.example.canteenlyapp.R
import com.example.canteenlyapp.data.model.Menu
import com.example.canteenlyapp.ui.components.MenuCard
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.canteenlyapp.data.model.CartItem
import com.example.canteenlyapp.data.model.MenuOptionGroup
import com.example.canteenlyapp.data.model.User
import com.example.canteenlyapp.data.repository.AuthRepository
import com.example.canteenlyapp.data.repository.CartRepository
import com.example.canteenlyapp.ui.components.CartBottomSheet
import com.example.canteenlyapp.ui.components.ChangeAddressBottomSheet
import com.example.canteenlyapp.utils.getMenuImage
import kotlinx.coroutines.launch

fun getCanteenImage(imageKey: String): Int {
    return when (imageKey) {
        "palette_gelato" -> R.drawable.palette_gelato
        "crumb_crust" -> R.drawable.crumb_crust
        "leaf_brew" -> R.drawable.leaf_brew
        "urban_bites" -> R.drawable.urban_bites
        else -> R.drawable.palette_gelato
    }
}
fun formatReviewCount(count: Int): String {
    return when {
        count >= 1_000_000 -> String.format("%.1fM", count / 1_000_000f)
        count >= 1_000 -> String.format("%.1fK", count / 1_000f)
        else -> count.toString()
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CanteenDetailScreen(
    navController: NavController,
    canteenId: String
){
    val repository = remember {
        FoodRepository()
    }
    val authRepository = remember {
        AuthRepository()
    }
    val scope = rememberCoroutineScope()
    val canteens =
        remember {
            mutableStateListOf<Canteen>()
        }

    var canteen by remember {
        mutableStateOf<Canteen?>(null)
    }
    var currentUser by remember {
        mutableStateOf<User?>(null)
    }
    var showAddressSheet by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {

        repository.getCanteenById(
            canteenId
        ) {
            canteen = it
        }
        repository.getCanteens { result ->
            canteens.clear()
            canteens.addAll(result)
        }
    }
    LaunchedEffect(Unit) {
        currentUser = authRepository.getCurrentUser()
    }

    val menus = remember {
        mutableStateListOf<Menu>()
    }

    repository.getMenus { result ->
        menus.clear()
        menus.addAll(result)
    }
    val canteenMenus = menus.filter {
        it.canteenId == canteenId
    }

    val bestSellerMenus = canteenMenus
        .filter { it.isBestSeller }
        .sortedByDescending { it.rating }
        .take(3)

    var selectedCategory by remember {
        mutableStateOf("All")
    }

    var expanded by remember { mutableStateOf(false) }

    val categories = listOf("All") +
            canteenMenus
                .map { it.category }
                .distinct()
                .sorted()

    val filteredMenus =
        if (selectedCategory == "All") {

            canteenMenus

        } else {

            canteenMenus.filter {
                it.category == selectedCategory
            }
        }
    val optionGroups = remember {
        mutableStateListOf<MenuOptionGroup>()
    }
    var selectedMenu by remember {
        mutableStateOf<Menu?>(null)
    }
    var showCartSheet by remember {
        mutableStateOf(false)
    }
    if (canteen == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = Color(0xFFFF7A00),
                strokeWidth = 3.dp
            )
        }
        return
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkGray)
            .navigationBarsPadding()
    ) {
        Box {

            Image(
                painter = painterResource(
                    getCanteenImage(
                        canteen?.imageKey ?: "palette_gelato"
                    )
                ),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp),
                contentScale = ContentScale.Crop
            )

            // BACK BUTTON
            Box(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(start = 10.dp, top = 8.dp)
                    .size(35.dp)
                    .background(
                        Color.Black.copy(alpha = 0.9f),
                        CircleShape
                    )
                    .clickable {
                        navController.popBackStack()
                    }
                    .align(Alignment.TopStart),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }

            // MORE BUTTON
            Box(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(end = 10.dp, top = 8.dp)
                    .size(35.dp)
                    .background(
                        Color.Black.copy(alpha = 0.9f),
                        CircleShape
                    )
                    .clickable {
                        // buka menu
                    }
                    .align(Alignment.TopEnd),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkGray)
                .padding(horizontal = 10.dp, vertical = 5.dp)

        ) {

            item {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = canteen?.title ?: "",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    IconButton(
                        onClick = { }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = Color.Red
                        )
                    }
                }

                Spacer(modifier = Modifier.height(3.dp))

                // Rating Row
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFF9800),
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "${canteen?.rating ?: 0} (${formatReviewCount(canteen?.reviewCount ?: 0)})",
                        color = Color.LightGray,
                        style = MaterialTheme.typography.bodySmall,
                    )

                    Spacer(modifier = Modifier.width(7.dp))

                    Text(
                        text = "•",
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.width(7.dp))

                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = null,
                        tint = Color(0xFFFF9800),
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "${canteen?.openTime ?: ""} - ${canteen?.closeTime ?: ""}",
                        color = Color.LightGray,
                        style = MaterialTheme.typography.bodySmall,

                    )

                    Spacer(modifier = Modifier.width(7.dp))

                    Text(
                        text = "•",
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.width(7.dp))

                    Icon(
                        imageVector = Icons.Default.DeliveryDining,
                        contentDescription = null,
                        tint = Color(0xFFFF9800),
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text =
                            if ((canteen?.deliveryFee ?: 0.0) == 0.0)
                                "Free"
                            else
                                "$${canteen?.deliveryFee}",
                        color = Color.LightGray,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }

                Spacer(modifier = Modifier.height(15.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically

                    ) {

                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            tint = Color(0xFFFF9800)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Column {
                            Text(
                                text = "Delivery in ${canteen?.deliveryTime ?: "-"}",
                                color = Color.White,
                                style = MaterialTheme.typography.bodySmall,
                            )

                            Text(
                                text = currentUser?.address
                                    ?.takeIf { it.isNotBlank() }
                                    ?: "Set delivery address",
                                color = Color.Gray,
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                    }

                    Button(
                        onClick = {
                            showAddressSheet = true
                        },

                        modifier = Modifier.height(30.dp),

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
                            text = "Change",
                            fontSize = 11.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(25.dp))

                if (bestSellerMenus.isNotEmpty()) {

                    Text(
                        text = "Best Sellers 🔥",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }

            }

            if (bestSellerMenus.isNotEmpty()) {

                itemsIndexed(bestSellerMenus) { index, menu ->

                    BestSellerItem(
                        menu = menu,
                        rank = index + 1,
                        onAddToCart = {

                            repository.getMenuOptions(
                                menu.id
                            ) { result ->

                                optionGroups.clear()
                                optionGroups.addAll(result)

                                selectedMenu = menu
                                showCartSheet = true
                            }
                        }
                    )

                    HorizontalDivider(
                        color = Color(0xFF1E293B)
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(25.dp))
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "Item Menu",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Box {

                        OutlinedButton(
                            onClick = {
                                expanded = true
                            },
                            shape = RoundedCornerShape(8.dp),

                            border = BorderStroke(
                                1.dp,
                                Color(0xFFFF7A00)
                            ),

                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = Color(0xFF111827),
                                contentColor = Color.White
                            ),

                            contentPadding = PaddingValues(
                                horizontal = 12.dp,
                                vertical = 0.dp
                            ),

                            modifier = Modifier.height(32.dp)
                        ) {

                            Text(
                                text = selectedCategory,
                                fontSize = 12.sp
                            )

                            Spacer(modifier = Modifier.width(4.dp))

                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = {
                                expanded = false
                            }
                        ) {

                            categories.forEach { category ->

                                DropdownMenuItem(
                                    text = {
                                        Text(category)
                                    },
                                    onClick = {
                                        selectedCategory = category
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
            items(filteredMenus) { menu ->
                val context = LocalContext.current
                val imageRes = getMenuImage(context, menu.imageKey)
                MenuCard(
                    menu = menu,
                    onAddToCart = {

                        repository.getMenuOptions(
                            menu.id
                        ) { result ->

                            optionGroups.clear()
                            optionGroups.addAll(result)

                            selectedMenu = menu
                            showCartSheet = true
                        }
                    }
                )
            }
        }
    }
    if (showCartSheet && selectedMenu != null) {

        CartBottomSheet(
            menu = selectedMenu!!,

            optionGroups = optionGroups,

            onDismiss = {
                showCartSheet = false
            },
            onAddToCart = { menu, qty, selectedOptions ->

                val canteen = canteens.find {
                    it.id == menu.canteenId
                }

                CartRepository.addItem(
                    CartItem(
                        id = menu.id,
                        canteenId = menu.canteenId,
                        canteenName = canteen?.title ?: "",
                        canteenImageKey = canteen?.imageKey ?: "",
                        name = menu.name,
                        imageKey = menu.imageKey,
                        price = menu.price,
                        totalPrice = menu.price * qty,
                        quantity = qty,
                        selectedOptions = selectedOptions
                    )
                )

                showCartSheet = false
            }
        )
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