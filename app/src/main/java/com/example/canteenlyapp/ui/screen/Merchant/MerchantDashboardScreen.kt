package com.canteenly.merchant.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.canteenlyapp.data.model.AppNotification
import com.example.canteenlyapp.data.repository.AuthRepository
import com.example.canteenlyapp.data.repository.FoodRepository
import com.example.canteenlyapp.data.repository.NotificationRepository
import com.example.canteenlyapp.data.repository.OrderRepository
import com.example.canteenlyapp.data.service.OrderNotificationService
import java.util.Calendar
import com.example.canteenlyapp.utils.NotificationHelper
import android.content.Intent
import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.NotificationsNone

/* ----------------------------------------------------------------------------
 * Design tokens
 * -------------------------------------------------------------------------- */
private object CanteenlyColors {
    val Background = Color(0xFF0E1116)
    val Surface = Color(0xFF161B22)
    val SurfaceElevated = Color(0xFF1C232C)
    val Primary = Color(0xFFFF9800)
    val PrimaryDim = Color(0x1FFF9800) // 12% orange tint for icon containers

    val TextPrimary = Color(0xFFF2F4F7)
    val TextSecondary = Color(0xFF9AA4B2)
    val Divider = Color(0xFF232A33)

    // Status colors
    val StatusPreparing = Color(0xFFFF9800)
    val StatusOnTheWay = Color(0xFF3B82F6)
    val StatusDone = Color(0xFF22C55E)
    val StatusCancelled = Color(0xFFEF4444)
}

private val CardShape = RoundedCornerShape(16.dp)

/* ----------------------------------------------------------------------------
 * Data models
 * -------------------------------------------------------------------------- */
enum class OrderStatus(val label: String, val color: Color) {
    PREPARING("Preparing", CanteenlyColors.StatusPreparing),
    ON_THE_WAY("On The Way", CanteenlyColors.StatusOnTheWay),
    DONE("Done", CanteenlyColors.StatusDone),
    CANCELLED("Cancelled", CanteenlyColors.StatusCancelled)
}

data class StatItem(
    val label: String,
    val value: String,
    val icon: ImageVector
)

data class RecentOrder(
    val customerName: String,
    val orderId: String,
    val total: String,
    val status: OrderStatus
)

data class QuickAction(
    val label: String,
    val subtitle: String,
    val icon: ImageVector
)

/* ----------------------------------------------------------------------------
 * Sample (realistic) data — replace with your ViewModel state
 * -------------------------------------------------------------------------- */

private val sampleQuickActions = listOf(
    QuickAction("Manage Orders", "Track & update live orders", Icons.Filled.ReceiptLong),
    QuickAction("Manage Menu", "Edit items, prices & stock", Icons.Filled.RestaurantMenu),
    QuickAction("Store Profile", "Hours, info & settings", Icons.Filled.Store)
)

@Composable
fun MerchantDashboardContainer(navController: NavController) {
    val context = LocalContext.current
    val authRepository = remember {
        AuthRepository()
    }

    val foodRepository = remember {
        FoodRepository()
    }
    var notifications by remember { mutableStateOf<List<AppNotification>>(emptyList()) }
    var canteenId by remember { mutableStateOf("") }
    var stats by remember {
        mutableStateOf<List<StatItem>>(emptyList())
    }
    var preparingCount by remember {
        mutableIntStateOf(0)
    }

    var onTheWayCount by remember {
        mutableIntStateOf(0)
    }

    var completedCount by remember {
        mutableIntStateOf(0)
    }
    var recentOrders by remember {
        mutableStateOf<List<RecentOrder>>(emptyList())
    }
    var storeName by remember {
        mutableStateOf("")
    }
    val knownNotifIds = remember { mutableStateListOf<String>() }
    var firstNotifLoad by remember { mutableStateOf(true) }

    val greeting = when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
        in 0..11 -> "Good Morning 👋"
        in 12..16 -> "Good Afternoon ☀️"
        in 17..20 -> "Good Evening 🌇"
        else -> "Good Night 🌙"
    }
    LaunchedEffect(Unit) {

        val merchant =
            authRepository.getCurrentMerchant()

        merchant?.let { user ->
            canteenId = user.canteenId
            foodRepository.getCanteenById(
                user.canteenId
            ) { canteen ->

                storeName =
                    canteen?.title ?: "My Store"
            }

            foodRepository.getMenusByCanteenId(
                user.canteenId
            ) { menus ->

                OrderRepository.getOrdersByCanteenId(
                    user.canteenId
                ) { orders ->


                    val activeOrders =
                        orders.count {
                            it.status in listOf(
                                "Preparing",
                                "On The Way",
                                "Ready Pickup"
                            )
                        }

                    val revenue =
                        orders
                            .filter { it.status == "Done" }
                            .sumOf { it.total }

                    stats = listOf(

                        StatItem(
                            "Today's Orders",
                            orders.size.toString(),
                            Icons.Filled.ShoppingBag
                        ),

                        StatItem(
                            "Revenue Today",
                            "$${"%.2f".format(revenue)}",
                            Icons.Filled.AttachMoney
                        ),
                        StatItem(
                            "Active Orders",
                            activeOrders.toString(),
                            Icons.Filled.DeliveryDining
                        ),

                        StatItem(
                            "Total Menu",
                            menus.size.toString(),
                            Icons.Filled.RestaurantMenu
                        )
                    )
                    recentOrders = orders
                        .sortedByDescending {
                            it.createdAt
                        }
                        .take(5)
                        .map { order ->

                            RecentOrder(

                                customerName =
                                    if (order.deliveryMethod == "Pickup")
                                        "Pickup Order"
                                    else
                                        "Delivery Order",

                                orderId =
                                    "#${order.createdAt.toString().takeLast(6)}",

                                total =
                                    "$${"%.2f".format(order.total)}",

                                status = when (order.status) {

                                    "Preparing" ->
                                        OrderStatus.PREPARING

                                    "On The Way",
                                    "Ready Pickup" ->
                                        OrderStatus.ON_THE_WAY

                                    "Done" ->
                                        OrderStatus.DONE

                                    else ->
                                        OrderStatus.CANCELLED
                                }
                            )
                        }
                    preparingCount =
                        orders.count {
                            it.status == "Preparing"
                        }

                    onTheWayCount =
                        orders.count {
                            it.status in listOf(
                                "On The Way",
                                "Ready Pickup"
                            )
                        }

                    completedCount =
                        orders.count {
                            it.status == "Done"
                        }


                }
            }
            NotificationRepository.listenNotifications(user.canteenId) { list ->

                if (!firstNotifLoad) {
                    list.filter { it.id !in knownNotifIds }.forEach { n ->
                        NotificationHelper.show(
                            context = context,
                            id = n.id.hashCode(),
                            title = n.title,
                            message = n.message
                        )
                    }
                }
                knownNotifIds.clear()
                knownNotifIds.addAll(list.map { it.id })
                firstNotifLoad = false

                notifications = list
            }
        }
    }
    LaunchedEffect(canteenId) {
        if (canteenId.isNotEmpty()) {
            val intent = Intent(context, OrderNotificationService::class.java).apply {
                putExtra("canteenId", canteenId)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }
    }

    val unread = notifications.count { !it.read }

    MerchantDashboardScreen(
        storeName = storeName,
        greeting = greeting,
        unreadNotifications = unread,
        canteenId = canteenId,
        stats = stats,
        recentOrders = recentOrders,
        preparingCount = preparingCount,
        onTheWayCount = onTheWayCount,
        completedCount = completedCount,
        onOpenNotifications = {
            navController.navigate("notifications")
        }
    )
}
/* ----------------------------------------------------------------------------
 * Root screen
 * -------------------------------------------------------------------------- */
@Composable
fun MerchantDashboardScreen(
    storeName: String,
    greeting: String,
    unreadNotifications: Int = 0,
    canteenId: String = "",
    stats: List<StatItem>,
    recentOrders: List<RecentOrder>,
    quickActions: List<QuickAction> = sampleQuickActions,
    preparingCount: Int,
    onTheWayCount: Int,
    completedCount: Int,
    onSeeAllOrders: () -> Unit = {},
    onOrderClick: (RecentOrder) -> Unit = {},
    onQuickAction: (QuickAction) -> Unit = {},
    onOpenNotifications: () -> Unit = {}
) {

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = CanteenlyColors.Background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding( horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            DashboardHeader(
                storeName = storeName,
                greeting = greeting,
                unreadNotifications = unreadNotifications,
                onNotificationsClick = onOpenNotifications
            )

            StatsGrid(stats = stats)

            OrdersOverviewCard(
                preparing = preparingCount,
                onTheWay = onTheWayCount,
                completed = completedCount
            )

            RecentOrdersSection(
                orders = recentOrders,
                onSeeAll = onSeeAllOrders,
                onOrderClick = onOrderClick
            )

//            QuickActionsSection(
//                actions = quickActions,
//                onActionClick = onQuickAction
//            )

            Spacer(Modifier.height(13.dp))
        }
    }
}

/* ----------------------------------------------------------------------------
 * Top header
 * -------------------------------------------------------------------------- */
@Composable
private fun DashboardHeader(
    storeName: String,
    greeting: String,
    unreadNotifications: Int,
    onNotificationsClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Profile avatar — swap the Box for a Coil AsyncImage in production
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        listOf(CanteenlyColors.Primary, Color(0xFFFFB74D))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "Merchant profile",
                tint = Color.White,
                modifier = Modifier.size(26.dp)
            )
        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = greeting,
                color = CanteenlyColors.TextSecondary,
                fontSize = 13.sp
            )
            Text(
                text = storeName,
                color = CanteenlyColors.TextPrimary,
                fontSize = 19.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        BadgedBox(
            badge = {
                if (unreadNotifications > 0) {
                    Badge(
                        containerColor = Color(0xFFFF9800),
                        contentColor = Color.White
                    ) {
                        Text(
                            text = if (unreadNotifications > 9) "9+" else "$unreadNotifications",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        ) {
            Box(
                modifier = Modifier
                    .size(35.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2B313C))
                    .clickable { onNotificationsClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.NotificationsNone,
                    contentDescription = "Notification",
                    tint = Color.White
                )
            }
        }
    }
}

/* ----------------------------------------------------------------------------
 * Statistics — 2x2 grid
 * -------------------------------------------------------------------------- */
@Composable
private fun StatsGrid(stats: List<StatItem>) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        stats.chunked(2).forEach { rowItems ->
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                rowItems.forEach { stat ->
                    StatCard(stat = stat, modifier = Modifier.weight(1f))
                }
                // keep alignment if the last row has a single item
                if (rowItems.size == 1) Spacer(Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun StatCard(stat: StatItem, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = CardShape,
        colors = CardDefaults.cardColors(containerColor = CanteenlyColors.Surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(CanteenlyColors.PrimaryDim),

                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = stat.icon,
                    contentDescription = stat.label,
                    tint = CanteenlyColors.Primary,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(
                modifier = Modifier.width(14.dp)
            )

            Column {

                Text(
                    text = stat.value,
                    color = CanteenlyColors.TextPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(2.dp)
                )

                Text(
                    text = stat.label,
                    color = CanteenlyColors.TextSecondary,
                    fontSize = 12.sp,
                    maxLines = 1
                )
            }
        }
    }
}

/* ----------------------------------------------------------------------------
 * Orders overview
 * -------------------------------------------------------------------------- */
@Composable
private fun OrdersOverviewCard(
    preparing: Int,
    onTheWay: Int,
    completed: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = CardShape,
        colors = CardDefaults.cardColors(containerColor = CanteenlyColors.Surface)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Orders Overview",
                color = CanteenlyColors.TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(18.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OverviewStat("Preparing", preparing, CanteenlyColors.StatusPreparing)
                OverviewDivider()
                OverviewStat("On The Way", onTheWay, CanteenlyColors.StatusOnTheWay)
                OverviewDivider()
                OverviewStat("Completed", completed, CanteenlyColors.StatusDone)
            }
        }
    }
}

@Composable
private fun OverviewStat(label: String, count: Int, accent: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Spacer(Modifier.width(6.dp))
            Text(
                text = "$count",
                color = CanteenlyColors.TextPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text = label,
            color = CanteenlyColors.TextSecondary,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun OverviewDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(40.dp)
            .background(CanteenlyColors.Divider)
    )
}

/* ----------------------------------------------------------------------------
 * Recent orders
 * -------------------------------------------------------------------------- */
@Composable
private fun RecentOrdersSection(
    orders: List<RecentOrder>,
    onSeeAll: () -> Unit,
    onOrderClick: (RecentOrder) -> Unit
) {
    Column {
        SectionHeader(title = "Recent Orders", actionText = "See all", onAction = onSeeAll)
        Spacer(Modifier.height(12.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = CardShape,
            colors = CardDefaults.cardColors(containerColor = CanteenlyColors.Surface)
        ) {
            Column {
                orders.forEachIndexed { index, order ->
                    RecentOrderRow(order = order, onClick = { onOrderClick(order) })
                    if (index != orders.lastIndex) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .height(1.dp)
                                .background(CanteenlyColors.Divider)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RecentOrderRow(order: RecentOrder, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Customer avatar with initials
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(CanteenlyColors.SurfaceElevated),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = order.customerName.initials(),
                color = CanteenlyColors.Primary,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = order.customerName,
                color = CanteenlyColors.TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = order.orderId,
                color = CanteenlyColors.TextSecondary,
                fontSize = 12.sp
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = order.total,
                color = CanteenlyColors.TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(6.dp))
            StatusBadge(status = order.status)
        }
    }
}

@Composable
private fun StatusBadge(status: OrderStatus) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(status.color.copy(alpha = 0.15f))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = status.label,
            color = status.color,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

/* ----------------------------------------------------------------------------
 * Quick actions
 * -------------------------------------------------------------------------- */
@Composable
private fun QuickActionsSection(
    actions: List<QuickAction>,
    onActionClick: (QuickAction) -> Unit
) {
    Column {
        SectionHeader(title = "Quick Actions")
        Spacer(Modifier.height(12.dp))
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            actions.forEach { action ->
                QuickActionButton(action = action, onClick = { onActionClick(action) })
            }
        }
    }
}

@Composable
private fun QuickActionButton(action: QuickAction, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = CardShape,
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = CanteenlyColors.Surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(CanteenlyColors.PrimaryDim),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = action.icon,
                    contentDescription = action.label,
                    tint = CanteenlyColors.Primary,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = action.label,
                    color = CanteenlyColors.TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = action.subtitle,
                    color = CanteenlyColors.TextSecondary,
                    fontSize = 12.sp
                )
            }
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = CanteenlyColors.TextSecondary,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

/* ----------------------------------------------------------------------------
 * Shared helpers
 * -------------------------------------------------------------------------- */
@Composable
private fun SectionHeader(
    title: String,
    actionText: String? = null,
    onAction: () -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            color = CanteenlyColors.TextPrimary,
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold
        )
        if (actionText != null) {
            Text(
                text = actionText,
                color = CanteenlyColors.Primary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clip(RoundedCornerShape(8.dp))
            )
        }
    }
}

private fun String.initials(): String =
    trim().split(" ")
        .filter { it.isNotEmpty() }
        .take(2)
        .joinToString("") { it.first().uppercase() }
