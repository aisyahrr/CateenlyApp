package com.example.canteenlyapp.ui.screen.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import com.example.canteenlyapp.ui.components.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.canteenlyapp.R
import androidx.navigation.NavController
import com.example.canteenlyapp.data.model.Canteen
import com.example.canteenlyapp.data.repository.FoodRepository
import com.example.canteenlyapp.ui.components.CanteenCard
import com.example.canteenlyapp.ui.theme.DarkGray
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage
import com.example.canteenlyapp.data.dummy.CategoryDummy
import com.example.canteenlyapp.data.model.CartItem
import com.example.canteenlyapp.ui.components.CategoryItem
import com.example.canteenlyapp.ui.components.MenuCard
import com.example.canteenlyapp.data.model.Menu
import com.example.canteenlyapp.data.model.MenuOptionGroup
import com.example.canteenlyapp.data.model.User
import com.example.canteenlyapp.data.repository.AuthRepository
import com.example.canteenlyapp.data.repository.CartRepository
import com.example.canteenlyapp.ui.components.CartBottomSheet
import com.example.canteenlyapp.ui.components.getInitials
import com.example.canteenlyapp.utils.getMenuImage


//@Preview(
//    showBackground = true,
//    showSystemUi = true,
//)
//@Composable
//fun HomePreview() {
//    FoodAppTheme {
//        HomeContent()
//    }
//}

@Composable
fun HomeScreen(
    navController: NavController
) {
    HomeContent(navController)
}

@Composable
fun HomeContent(navController: NavController) {
    val authRepository = remember { AuthRepository() }
    val repository = FoodRepository()
    val canteens =
        remember {
            mutableStateListOf<Canteen>()
        }

    val categories = CategoryDummy.categories
    var selectedCategory by remember {
        mutableStateOf("All")
    }
    val menus = remember {
        mutableStateListOf<Menu>()
    }
    val bestSellerMenus = menus.filter {
        it.isBestSeller
    }
    val filteredMenus = when (selectedCategory) {

        "All" -> bestSellerMenus

        "Main Course" -> bestSellerMenus.filter {
            it.category == "Main Course"
        }

        "Bakery" -> bestSellerMenus.filter {
            it.category == "Bakery"
        }

        "Desserts" -> bestSellerMenus.filter {
            it.category == "Dessert"
        }

        "Drinks" -> bestSellerMenus.filter {
            it.category == "Drinks"
        }

        else -> bestSellerMenus
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
    var user by remember {
        mutableStateOf<User?>(null)
    }
    LaunchedEffect(Unit) {
        repository.getCanteens { result ->
            canteens.clear()
            canteens.addAll(result)
        }

        repository.getMenus { result ->
            menus.clear()
            menus.addAll(result)
        }
    }

    LaunchedEffect(Unit) {
        user = authRepository.getCurrentUser()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkGray)
            .padding(horizontal = 20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!user?.photoUrl.isNullOrBlank()) {

                    AsyncImage(
                        model = user?.photoUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )

                } else {

                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFF9800)),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = getInitials(user?.fullName),
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.width(15.dp))

                Text(
                    text = "Hi, ${user?.fullName ?: "Guest"} 👋",
                    color = Color.White
                )
            }

            Box(
                modifier = Modifier
                    .size(35.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2B313C)),
                contentAlignment = Alignment.Center
            ){
                Icon(
                    imageVector = Icons.Default.NotificationsNone,
                    contentDescription = "Notification",
                    tint = Color.White

                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))

        SearchBar()

        Spacer(modifier = Modifier.height(13.dp))
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {

            item {
                Text(
                    text = "All Canteens",
                    fontSize = 16.sp,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(7.dp))
            }

            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(canteens) { canteen ->
                        CanteenCard(
                            canteen = canteen,
                            onClick = {
                                navController.navigate(
                                    "canteen_detail/${canteen.id}"
                                )
                            }
                        )
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(15.dp))
            }

            item {
                LazyRow(

                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {


                    items(categories) { category ->
                        CategoryItem(
                            category = category,
                            isSelected = selectedCategory == category.name,
                            onClick = {
                                selectedCategory = category.name
                            }
                        )
                    }
                }
            }


            item {
                Spacer(modifier = Modifier.height(15.dp))

                Text(
                    text = "Popular Items",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(7.dp))
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
}
