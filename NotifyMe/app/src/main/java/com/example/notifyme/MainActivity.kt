package com.example.notifyme

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat


class MainActivity : AppCompatActivity() {

    // member variables
    private var buttonNotify: Button? = null
    private var buttonCancel: Button? = null
    private var buttonUpdate: Button? = null
    private var mNotifyManager: NotificationManager? = null
    private val mReceiver: NotificationReceiver = NotificationReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonNotify = findViewById(R.id.notify)
        buttonNotify?.setOnClickListener { sendNotification() }

        buttonUpdate = findViewById(R.id.update)
        buttonUpdate?.setOnClickListener { updateNotification() }

        buttonCancel = findViewById(R.id.cancel)
        buttonCancel?.setOnClickListener { cancelNotification() }

        createNotificationChannel()

        // enable only the notification button
        setNotificationButtonState(true, false, false)

        // Note: It may seem as if the broadcast sent by the notification only concerns your app
        // and should be delivered with a LocalBroadcastManager.
        // However, using a PendingIntent delegates the responsibility of delivering the notification to the Android framework.
        // Because the Android runtime handles the broadcast, you cannot use LocalBroadcastManager.

        // to receive the ACTION_UPDATE_NOTIFICATION intent, register the broadcast receiver
        registerReceiver(mReceiver, IntentFilter(ACTION_UPDATE_NOTIFICATION))
        registerReceiver(mReceiver, IntentFilter(ACTION_DISMISS_NOTIFICATION))
    }

    override fun onDestroy() {
        unregisterReceiver(mReceiver)
        super.onDestroy()
    }

    private fun sendNotification() {
        // intent with the custom update action ACTION_UPDATE_NOTIFICATION.
        val updateIntent = Intent(ACTION_UPDATE_NOTIFICATION)

        // Use getBroadcast() to get a PendingIntent.
        // To make sure that this pending intent is sent and used only once, set FLAG_ONE_SHOT.
        val updatePendingIntent = PendingIntent.getBroadcast(this, NOTIFICATION_ID, updateIntent, PendingIntent.FLAG_ONE_SHOT)

        val notifyBuilder = getNotificationBuilder()

        // allow the user to click the "Update Notification" action on the intent
        notifyBuilder!!.addAction(R.drawable.ic_update, "Update Notification", updatePendingIntent)

        // to create a notification, use NotificationCompat.Builder.build().
        mNotifyManager!!.notify(NOTIFICATION_ID, notifyBuilder.build())

        // disable only the notification button
        setNotificationButtonState(false, true, true)
    }

    private fun updateNotification() {
        // Expected behaviour:
        // Tap the update button and check the notification again, it now has the image and the updated title!
        // To shrink back to the regular notification style, pinch the extended notification.

        // drawable to bitmap
        val androidImage = BitmapFactory.decodeResource(resources, R.drawable.mascot_1)

        val notifyBuilder = getNotificationBuilder()
        notifyBuilder!!.setStyle(
            NotificationCompat.BigPictureStyle()
                .bigPicture(androidImage)
                .setBigContentTitle("Notification Updated!")
        )

        // To issue a notification, use NotificationManager.notify() to pass the notification object to the Android runtime system.
        // To make it possible to update or cancel a notification, associate a notification ID with the notification.

        // build the notification and call notify() on the NotificationManager
        mNotifyManager!!.notify(NOTIFICATION_ID, notifyBuilder.build())

        // enable only the cancellation button
        setNotificationButtonState(false, false, true)
    }

    private fun cancelNotification() {
        mNotifyManager!!.cancel(NOTIFICATION_ID)

        // enable only the notification button
        setNotificationButtonState(true, false, false)
    }

    /**
     * When your app targets Android 8.0 (API level 26), to display notifications to your users
     * you must implement at least one notification channel.
     *
     * To display notifications on lower-end devices, you're not required to implement notification channels.
     * However, it's good practice to always do the following:
     * - Target the latest available SDK.
     * - Check the device's SDK version in your code. If the SDK version is 26 or higher, build notification channels.
     * If your targetSdkVersion is set to 25 or lower, when your app runs on Android 8.0 (API level 26) or higher,
     * it behaves the same as it would on devices running Android 7.1 (API level 25) or lower.
     */
    private fun createNotificationChannel() {
        mNotifyManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // notification channels are only available in API 26 and higher, so check for the device's API version
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            // Create a NotificationChannel
            val notificationChannel = NotificationChannel(
                PRIMARY_CHANNEL_ID, "Mascot Notification", NotificationManager.IMPORTANCE_HIGH
            )

            // initial settings
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Notification from Mascot"

            mNotifyManager?.createNotificationChannel(notificationChannel)
        }
    }

    /**
     * Helper method for building the notification.
     *
     * @return A Notification Builder
     */
    private fun getNotificationBuilder(): NotificationCompat.Builder? {
        // explicit intent method to launch the MainActivity
        val notificationIntent = Intent(this, MainActivity::class.java)

        // then do something on behalf of this app
        val notificationPendingIntent = PendingIntent.getActivity(
            this,
            NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        // To specify the UI and actions for a notification, use NotificationCompat.Builder.
        val notifyBuilder = NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
            .setContentTitle("You've been notified!")
            .setContentText("This is your notification text.")
            .setSmallIcon(R.drawable.ic_android)
            .setContentIntent(notificationPendingIntent)
            // close the notification when the user taps on it
            .setAutoCancel(true)
            // set a high priority for the notification
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            // Set the sound, vibration, and LED-color pattern (if the user's device has an LED indicator) to the default values.
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            // get notified when the user dismisses the notification
            .setDeleteIntent(getDeleteIntent())

        return notifyBuilder;
    }

    /**
     * Utility method to toggle the button states
     */
    private fun setNotificationButtonState(isNotifyEnabled: Boolean, isUpdateEnabled: Boolean, isCancelEnabled: Boolean) {
        buttonNotify?.setEnabled(isNotifyEnabled)
        buttonUpdate?.setEnabled(isUpdateEnabled)
        buttonCancel?.setEnabled(isCancelEnabled)
    }

    private fun getDeleteIntent(): PendingIntent? {
        // intent with the custom delete action ACTION_DISMISS_NOTIFICATION.
        val deleteIntent = Intent(ACTION_DISMISS_NOTIFICATION)
        return PendingIntent.getBroadcast(this, NOTIFICATION_ID, deleteIntent, PendingIntent.FLAG_CANCEL_CURRENT)
    }

    /**
     * Broadcast receiver
     *
     * Sometimes, a notification requires interaction from the user.
     * For example, the user might snooze an alarm or reply to a text message.
     * When these types of notifications occur, the user might tap the notification to respond to the event.
     * Android then loads the relevant activity in your app.
     *
     * To avoid opening your app, the notification framework lets you embed
     * a notification action button directly in the notification itself.
     */
    inner class NotificationReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent!!.action) {
                ACTION_UPDATE_NOTIFICATION -> {
                    // when the user taps an 'Update Notification' action button inside the notification
                    Toast.makeText(context, "Updated!", Toast.LENGTH_SHORT).show()
                    updateNotification()
                }
                ACTION_DISMISS_NOTIFICATION -> {
                    // enable only the notification button
                    setNotificationButtonState(true, false, false)
                    Toast.makeText(context, "Dismissed!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}
