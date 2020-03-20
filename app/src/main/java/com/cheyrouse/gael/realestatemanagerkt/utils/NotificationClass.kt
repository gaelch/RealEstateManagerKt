package com.cheyrouse.gael.realestatemanagerkt.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.cheyrouse.gael.realestatemanagerkt.R
import com.cheyrouse.gael.realestatemanagerkt.utils.Constant.ConstantVal.NOTIFICATION_ID

class NotificationClass {

    fun showNotification(context: Context, isEdit: Boolean) {
        val textContent: String = if (isEdit) {
            context.resources.getString(R.string.notification_text_edit)
        } else {
            context.resources.getString(R.string.notification_text_create)
        }
        // 2 - Create a Style for the Notification
        val inboxStyle: NotificationCompat.InboxStyle = NotificationCompat.InboxStyle()
        inboxStyle.setBigContentTitle("Notification")
        inboxStyle.addLine(textContent)
        // 3 - Create a Channel (Android 8)
        val channelId: String = Constant.ConstantVal.CHANEL_ID
        // 4 - Build a Notification object
        val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(
                context,
                channelId
            ).setSmallIcon(R.drawable.ic_real_estate_m)
            .setContentTitle("Real Estate Manager")
            .setContentText(textContent)
            .setAutoCancel(true)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setStyle(inboxStyle)
        // 5 - Add the Notification to the Notification Manager and show it.
        val notificationManager = context
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // 6 - Support Version >= Android 8
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName: CharSequence = "Message come from RealEstateManager"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel =
                NotificationChannel(channelId, channelName, importance)
            notificationManager.createNotificationChannel(mChannel)
            // 7 - Show notification
            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
        }
    }
}