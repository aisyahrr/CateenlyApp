package com.example.canteenlyapp.ui.screen.Merchant

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.canteenlyapp.data.model.Canteen
import com.example.canteenlyapp.data.model.User
import com.example.canteenlyapp.data.repository.AuthRepository
import com.example.canteenlyapp.data.repository.FoodRepository
import com.example.canteenlyapp.data.service.OrderNotificationService
import com.example.canteenlyapp.ui.components.ChangeAddressBottomSheet
import com.example.canteenlyapp.ui.navigation.Screen
import kotlinx.coroutines.launch

/* ---------- palette ---------- */
private val Bg = Color(0xFF0E1116)
private val CardBg = Color(0xFF161B22)
private val Orange = Color(0xFFFF9800)
private val TextPrimary = Color(0xFFF2F4F7)
private val TextSecondary = Color(0xFF9AA4B2)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MerchantProfileScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val authRepository = remember { AuthRepository() }
    val foodRepository = remember { FoodRepository() }
    var user by remember { mutableStateOf<User?>(null) }

    // toggle notifikasi (disimpan biar pilihan-nya nempel)
    val prefs = remember {
        context.getSharedPreferences("canteenly_prefs", Context.MODE_PRIVATE)
    }
    var notifEnabled by remember {
        mutableStateOf(prefs.getBoolean("notif_enabled", true))
    }
    var isOpen by remember { mutableStateOf(false) }

    var currentCanteen  by remember {
        mutableStateOf<Canteen?>(null)
    }
    var showAddressSheet by remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()
    LaunchedEffect(user?.canteenId) {
        user?.canteenId?.let { cid ->
            foodRepository.getCanteenById(cid) { canteen ->
                currentCanteen = canteen
                isOpen = canteen?.isAvailable ?: false
            }
        }
    }
    LaunchedEffect(Unit) {
        user = authRepository.getCurrentUser()
    }

    LaunchedEffect(user?.canteenId) {
        user?.canteenId?.let { cid ->
            if (cid.isNotEmpty()) {
                foodRepository.getCanteenById(cid) { canteen ->
                    isOpen = canteen?.isAvailable ?: false
                }
            }
        }
    }

    fun applyNotif(enabled: Boolean) {
        notifEnabled = enabled
        prefs.edit().putBoolean("notif_enabled", enabled).apply()

        val canteenId = user?.canteenId ?: ""
        val intent = Intent(context, OrderNotificationService::class.java).apply {
            putExtra("canteenId", canteenId)
        }
        if (enabled) {
            if (canteenId.isNotEmpty()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(intent)
                } else {
                    context.startService(intent)
                }
            }
        } else {
            context.stopService(intent)
        }
    }
    fun applyOpen(open: Boolean) {
        isOpen = open
        val cid = user?.canteenId ?: ""
        if (cid.isNotEmpty()) {
            foodRepository.setCanteenAvailability(cid, open) { success ->
                if (!success) isOpen = !open
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Bg)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding( horizontal = 20.dp)
    ) {

        /* ---------- header ---------- */
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Orange),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Store,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Hello ${user?.fullName ?: "Merchant"}!",
                    color = TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = user?.email ?: "",
                    color = TextSecondary,
                    fontSize = 13.sp
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        /* ---------- section Akun ---------- */
        SectionTitle("Account")
        Spacer(Modifier.height(12.dp))

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

            // Edit Address (tombol)
            ProfileRow(
                icon = Icons.Default.LocationOn,
                title = "Edit Address",
                subtitle = "Update your store address",
                onClick = {
                    showAddressSheet = true
                }
            )
            // Toggle notifikasi
            ProfileToggleRow(
                icon = Icons.Default.Notifications,
                title = "Notifikasi Order",
                subtitle = if (notifEnabled) "Aktif" else "Nonaktif",
                checked = notifEnabled,
                onCheckedChange = { applyNotif(it) }
            )
            ProfileToggleRow(
                icon = Icons.Default.Store,
                title = "Store Status",
                subtitle = if (isOpen) "Open — accepting orders" else "Closed",
                checked = isOpen,
                onCheckedChange = { applyOpen(it) }
            )

            // Settings (opsional)
            ProfileRow(
                icon = Icons.Default.Settings,
                title = "Setting",
                subtitle = "App preferences and configuration",
                onClick = {
                    // navController.navigate("merchant_settings")
                }
            )
        }

        Spacer(Modifier.height(32.dp))

        /* ---------- logout ---------- */
        Button(
            onClick = {
                // matikan service notifikasi saat logout
                context.stopService(
                    Intent(context, OrderNotificationService::class.java)
                )
                authRepository.logout()
                navController.navigate(Screen.Login.route) {
                    popUpTo(0)
                    launchSingleTop = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 12.dp
                ),

            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE53935)
            ),

            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Logout,
                contentDescription = null,
                tint = Color.White
            )
            Spacer(Modifier.width(8.dp))
            Text(text = "Logout", color = Color.White, fontWeight = FontWeight.SemiBold)
        }

        Spacer(Modifier.height(16.dp))
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
                    currentCanteen?.address ?: "",

                onSave = { address ->

                    scope.launch {

                        user?.canteenId?.let { cid ->

                            val result =
                                foodRepository.updateAddress(
                                    canteenId = cid,
                                    address = address
                                )

                            if (result.isSuccess) {

                                foodRepository.getCanteenById(cid) {
                                    currentCanteen = it
                                }

                                showAddressSheet = false
                            }
                        }
                    }
                }
            )
        }
    }
}

/* ---------- reusable ---------- */
@Composable
private fun InfoLine(
    label: String,
    value: String,
    valueColor: Color = TextPrimary
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = TextSecondary, fontSize = 13.sp)
        Text(text = value, color = valueColor, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        color = TextPrimary,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
private fun ProfileRow(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(CardBg)
            .clickable { onClick() }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Orange.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = Orange, modifier = Modifier.size(20.dp))
        }

        Spacer(Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Medium)
            if (subtitle != null) {
                Spacer(Modifier.height(2.dp))
                Text(text = subtitle, color = TextSecondary, fontSize = 12.sp)
            }
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = TextSecondary,
            modifier = Modifier.size(22.dp)
        )
    }
}

@Composable
private fun ProfileToggleRow(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(CardBg)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Orange.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = Orange, modifier = Modifier.size(20.dp))
        }

        Spacer(Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Medium)
            if (subtitle != null) {
                Spacer(Modifier.height(2.dp))
                Text(text = subtitle, color = TextSecondary, fontSize = 12.sp)
            }
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Orange,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFF3A4250)
            )
        )
    }
}