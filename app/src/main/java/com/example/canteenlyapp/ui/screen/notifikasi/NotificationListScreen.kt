package com.example.canteenlyapp.ui.screen.notifikasi

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.canteenlyapp.data.model.AppNotification
import com.example.canteenlyapp.data.repository.AuthRepository
import com.example.canteenlyapp.data.repository.NotificationRepository
import java.util.concurrent.TimeUnit

private object NColors {
    val Background = Color(0xFF0E1116)
    val Surface = Color(0xFF161B22)
    val Primary = Color(0xFFFF9800)
    val PrimaryDim = Color(0x1FFF9800)
    val TextPrimary = Color(0xFFF2F4F7)
    val TextSecondary = Color(0xFF9AA4B2)
}

@Composable
fun NotificationListScreen(
    navController: NavController
) {
    val authRepository = remember { AuthRepository() }
    var notifications by remember { mutableStateOf<List<AppNotification>>(emptyList()) }

    LaunchedEffect(Unit) {
        val user = authRepository.getCurrentUser()

        user?.let {
            val ownerId = if (it.role == "merchant") it.canteenId else it.uid

            if (ownerId.isNotEmpty()) {
                NotificationRepository.listenNotifications(ownerId) { list ->
                    notifications = list
                }
                NotificationRepository.markAllRead(ownerId)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NColors.Background)
            .statusBarsPadding()
    ) {
        // top bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFF9800))
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(22.dp)
                    .clickable { navController.popBackStack() }
            )
            Text(
                text = "Notifications",
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        if (notifications.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "You have no notifications yet",
                    color = NColors.TextSecondary,
                    fontSize = 14.sp
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(notifications) { notif ->
                    NotificationRow(notif)
                }
            }
        }
    }
}

@Composable
private fun NotificationRow(notif: AppNotification) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = NColors.Surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ikon bulat sesuai tipe
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(NColors.PrimaryDim),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (notif.type) {
                        "new_order" -> Icons.AutoMirrored.Filled.ReceiptLong
                        "status_update" -> Icons.Default.LocalShipping
                        else -> Icons.Default.Notifications
                    },
                    contentDescription = null,
                    tint = NColors.Primary,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = notif.title,
                    color = NColors.TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = notif.message,
                    color = NColors.TextSecondary,
                    fontSize = 13.sp
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = timeAgo(notif.createdAt),
                    color = NColors.TextSecondary,
                    fontSize = 11.sp
                )
            }

            // titik kalau belum dibaca
            if (!notif.read) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(NColors.Primary)
                )
            }
        }
    }
}

private fun timeAgo(time: Long): String {
    val diff = System.currentTimeMillis() - time
    val mins = TimeUnit.MILLISECONDS.toMinutes(diff)
    val hours = TimeUnit.MILLISECONDS.toHours(diff)
    val days = TimeUnit.MILLISECONDS.toDays(diff)
    return when {
        mins < 1 -> "Just now"
        mins < 60 -> "${mins}m ago"
        hours < 24 -> "${hours}h ago"
        days < 7 -> "${days}d ago"
        else -> "${days / 7}w ago"
    }
}