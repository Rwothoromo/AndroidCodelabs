package com.example.background.workers

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.text.TextUtils
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.background.KEY_IMAGE_URI
import com.example.background.R
import timber.log.Timber

/**
 * Contains the code to blur an image.
 * When the Go button is clicked, a WorkRequest is created and then enqueued by WorkManager.
 *
 * Worker is where you put the code for the actual work you want to perform in the background.
 * We extend Worker and override the doWork() method.
 */
class BlurWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    override fun doWork(): Result {
        // appContext is needed for various bitmap manipulations we are to do
        val appContext = applicationContext

        //  get the URI we passed in from the Data object
        val resourceUri = inputData.getString(KEY_IMAGE_URI)

        makeStatusNotification("Blurring image", appContext)

        // slow down the worker
        sleep()

        return try {

            // Create a Bitmap from the image the user selected
            if (TextUtils.isEmpty(resourceUri)) {
                Timber.e("Invalid input uri")
                throw IllegalArgumentException("Invalid input uri")
            }

            val resolver = appContext.contentResolver

            val picture = BitmapFactory.decodeStream(resolver.openInputStream(Uri.parse(resourceUri)))

            // Create a Bitmap from the test image
            // val picture = BitmapFactory.decodeResource(appContext.resources, R.drawable.test)

            // Get a blurred version of the bitmap
            // by calling the static blurBitmap method from WorkerUtils
            val output = blurBitmap(picture, appContext)

            // Write bitmap to a temporary file
            // by calling the static writeBitmapToFile method from WorkerUtils.
            val outputUri = writeBitmapToFile(appContext, output)

            // Make a Notification displaying the URI
            // by calling the static makeStatusNotification method from WorkerUtils.
            makeStatusNotification("Output is $outputUri", appContext)

            // provide the OutputURI as an output Data
            // to make this temporary image easily accessible to other workers for further operations
            val outputData = workDataOf(KEY_IMAGE_URI to outputUri.toString())

            // Result.success()
            Result.success(outputData)
        } catch (throwable: Throwable) {
            Timber.e(throwable)
            Result.failure()
        }
    }
}
