package com.jerry.samsung_mvvm2

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.IBinder
import androidx.core.app.NotificationCompat


class JustSomeForegroundService: Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val input = "Just Some Foreground Task"
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        val chan = NotificationChannel(
            "ApplicationConstants.ChannelId", "ApplicationConstants.ChannelId", NotificationManager.IMPORTANCE_DEFAULT
        )
        chan.lightColor = Color.RED
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val manager = (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
        manager.createNotificationChannel(chan)

        val notification = NotificationCompat.Builder(this, "ApplicationConstants.ChannelId")
            .setContentTitle("Box Is Checked")
            .setContentText(input)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(6514, notification)

        return START_NOT_STICKY
    }
}