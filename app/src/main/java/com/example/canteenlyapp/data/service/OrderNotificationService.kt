package com.example.canteenlyapp.data.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.canteenlyapp.data.repository.NotificationRepository
import com.example.canteenlyapp.utils.NotificationHelper

class OrderNotificationService : Service() {

    private val knownIds = mutableSetOf<String>()
    private var firstLoad = true

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val canteenId = intent?.getStringExtra("canteenId") ?: return START_NOT_STICKY

        NotificationHelper.createChannel(this)

        // notif tetap (WAJIB buat foreground service)
        val ongoing = NotificationCompat.Builder(this, NotificationHelper.CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_menu_my_calendar)
            .setContentTitle("Canteenly aktif")
            .setContentText("Memantau order masuk...")
            .setOngoing(true)
            .build()
        startForeground(1001, ongoing)

        listenOrders(canteenId)

        return START_STICKY   // coba hidup lagi kalau dimatikan sistem
    }

    private fun listenOrders(canteenId: String) {
        NotificationRepository.listenNotifications(canteenId) { list ->
            if (!firstLoad) {
                list.filter { it.id !in knownIds }.forEach { n ->
                    NotificationHelper.show(
                        context = this,
                        id = n.id.hashCode(),
                        title = n.title,
                        message = n.message
                    )
                }
            }
            knownIds.clear()
            knownIds.addAll(list.map { it.id })
            firstLoad = false
        }
    }
}