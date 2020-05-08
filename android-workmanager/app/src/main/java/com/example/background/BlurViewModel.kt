/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.background

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.work.*
import com.example.background.workers.BlurWorker
import com.example.background.workers.CleanupWorker
import com.example.background.workers.SaveImageToFileWorker


class BlurViewModel(application: Application) : AndroidViewModel(application) {

    internal var imageUri: Uri? = null
    internal var outputUri: Uri? = null
    private val workManager = WorkManager.getInstance(application)
    internal val outputWorkInfos: LiveData<List<WorkInfo>>

    init {
        // Ensures that whenever the current work Id changes the WorkInfo, the UI is listening to changes
        outputWorkInfos = workManager.getWorkInfosByTagLiveData(TAG_OUTPUT)
    }

    /**
     * Creates the input data bundle which includes the Uri to operate on
     * Data objects are lightweight containers for key/value pairs.
     * They are meant to store a small amount of data that might pass into and out from WorkRequests.
     *
     * @return Data which contains the Image Uri as a String
     */
    private fun createInputDataForUri(): Data {
        val builder = Data.Builder()

        // Pass in the URI for the user's image into a bundle.
        // That URI is stored in a variable called imageUri.
        // If imageUri is a non-null URI, add it to the Data object above called builder.
        imageUri?.let {
            builder.putString(KEY_IMAGE_URI, imageUri.toString())
        }

        // make your Data object, and return it
        return builder.build()
    }

    /**
     * Create the WorkRequest to apply the blur and save the resulting image
     * This method is called when the Go button is clicked, so create a OneTimeWorkRequest.
     *
     * There are two types of WorkRequests:
     * - OneTimeWorkRequest: A WorkRequest that will only execute once.
     * - PeriodicWorkRequest: A WorkRequest that will repeat on a cycle.
     * We only want the image to be blurred when the Go button is clicked, hence OneTimeWorkRequest.
     *
     * Then, tell WorkManager to run it using your WorkManager instance to enqueue your WorkRequest.
     *
     * @param blurLevel The amount to blur the image
     */
    internal fun applyBlur(blurLevel: Int) {
        // Add WorkRequest to Cleanup temporary images
        var continuation = workManager
                // .beginWith(OneTimeWorkRequest
                //        .from(CleanupWorker::class.java))
                // To only run one chain of work to run at a time
                .beginUniqueWork(
                        IMAGE_MANIPULATION_WORK_NAME,
                        ExistingWorkPolicy.REPLACE,
                        OneTimeWorkRequest.from(CleanupWorker::class.java)
                )

        // Add WorkRequests to blur the image the number of times requested
        for (i in 0 until blurLevel) {
            val blurBuilder = OneTimeWorkRequestBuilder<BlurWorker>()

            // Input the Uri if this is the first blur operation
            // After the first blur operation the input will be the output of previous
            // blur operations.
            if (i == 0) {
                blurBuilder.setInputData(createInputDataForUri())
            }

            continuation = continuation.then(blurBuilder.build())
        }

        // Add WorkRequest to save the image to the filesystem
        val save = OneTimeWorkRequestBuilder<SaveImageToFileWorker>()
                .addTag(TAG_OUTPUT)
                .build()

        continuation = continuation.then(save)

        // Actually start the work
        continuation.enqueue()
    }

    private fun uriOrNull(uriString: String?): Uri? {
        return if (!uriString.isNullOrEmpty()) {
            Uri.parse(uriString)
        } else {
            null
        }
    }

    /**
     * Setters
     */
    internal fun setImageUri(uri: String?) {
        imageUri = uriOrNull(uri)
    }

    internal fun setOutputUri(outputImageUri: String?) {
        outputUri = uriOrNull(outputImageUri)
    }
}
