package com.example.notificationscheduler

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat


class NotificationJobService: JobService() {
    var mNotifyManager: NotificationManager? = null

    /**
     * This method is called if the system has determined that you must stop execution of your job
     * even before you've had a chance to call [.jobFinished].
     *
     * If the conditions described in the JobInfo are no longer met,
     * the job must be stopped, and the system calls onStopJob().
     * The onStopJob() callback returns a boolean that determines what to do if the job is not finished.
     *
     * If the return value is true, the job is rescheduled; otherwise, the job is dropped.
     *
     * @param params The parameters identifying this job, as supplied to
     * the job in the [.onStartJob] callback.
     * @return `true` to indicate to the JobManager whether you'd like to reschedule
     * this job based on the retry criteria provided at job creation-time; or `false`
     * to end the job entirely.  Regardless of the value returned, your job must stop executing.
     */
    override fun onStopJob(params: JobParameters?): Boolean {
        // return true because if the job fails, you want the job to be rescheduled instead of dropped
        return true
    }

    /**
     * Like all other component lifecycle callbacks, this method executes
     * on your application's main thread.
     *
     * Called when the system determines that your task should be run.
     * In this method, you implement the job to be done.
     *
     * If returned boolean is true, the work is offloaded to a different thread,
     * and your app must call jobFinished() explicitly in that thread to indicate that the job is complete.
     *
     * If false, the system knows that the job is completed by the end of onStartJob(),
     * and the system calls jobFinished() on your behalf.
     *
     * @param params Parameters specifying info about this job, including the optional
     * extras configured with
     *
     * @return a boolean indicating whether the job needs to continue on a separate thread.
     */
    override fun onStartJob(params: JobParameters?): Boolean {
        //Create the notification channel
        createNotificationChannel()

        val builder = getNotificationBuilder()
        mNotifyManager!!.notify(NOTIFICATION_ID, builder!!.build())

        return false
    }

    /**
     * Creates a Notification channel, for OREO and higher.
     */
    private fun createNotificationChannel() {

        // Define notification manager object.
        mNotifyManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Notification channels are only available in OREO and higher.
        // So, add a check on SDK version.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // Create the NotificationChannel with all the parameters.
            val notificationChannel = NotificationChannel(
                PRIMARY_CHANNEL_ID,
                "Job Service notification",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Notifications from Job Service"
            mNotifyManager!!.createNotificationChannel(notificationChannel)
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
        val contentPendingIntent = PendingIntent.getActivity(
            this,
            NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        // To specify the UI and actions for a notification, use NotificationCompat.Builder.
        val notifyBuilder = NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
            .setContentTitle("Job Service!")
            .setContentText("Your Job is running!")
            .setSmallIcon(R.drawable.ic_job_running)
            .setContentIntent(contentPendingIntent)
            // close the notification when the user taps on it
            .setAutoCancel(true)
            // set a high priority for the notification
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            // Set the sound, vibration, and LED-color pattern (if the user's device has an LED indicator) to the default values.
            .setDefaults(NotificationCompat.DEFAULT_ALL)

        return notifyBuilder
    }

}
