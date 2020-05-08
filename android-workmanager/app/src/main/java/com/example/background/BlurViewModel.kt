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
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.background.workers.BlurWorker


class BlurViewModel(application: Application) : AndroidViewModel(application) {

    internal var imageUri: Uri? = null
    internal var outputUri: Uri? = null
    private val workManager = WorkManager.getInstance(application)

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
        // workManager.enqueue(OneTimeWorkRequest.from(BlurWorker::class.java))

        val blurRequest = OneTimeWorkRequestBuilder<BlurWorker>()
                // pass in the result from createInputDataForUri
                .setInputData(createInputDataForUri())
                // build the OneTimeWorkRequest
                .build()

        // enqueue the request using WorkManager
        workManager.enqueue(blurRequest)
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
