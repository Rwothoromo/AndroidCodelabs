/*
 * Copyright (C) 2016 Google Inc.
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
package com.example.hellosharedprefs

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat


/**
 * HelloSharedPrefs is an adaptation of the HelloToast app from chapter 1.
 * It includes:
 * - Buttons for changing the background color.
 * - Maintenance of instance state.
 * - Themes and styles.
 * - Read and write shared preferences for the current count and the color.
 *
 *
 * This is the starter code for HelloSharedPrefs.
 */
class MainActivity : AppCompatActivity() {
    // Current count
    private var mCount = 0

    // Current background color
    private var mColor = 0

    // Text view to display both count and color
    private var mShowCountTextView: TextView? = null

    // Key for current count
    private val COUNT_KEY = "count"

    // Key for current color
    private val COLOR_KEY = "color"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views, color
        mShowCountTextView = findViewById(R.id.count_textview)
        mColor = ContextCompat.getColor(this, R.color.default_background)

        // Restore the saved instance state.
        if (savedInstanceState != null) {
            mCount = savedInstanceState.getInt(COUNT_KEY)
            if (mCount != 0) {
                mShowCountTextView!!.text = String.format("%s", mCount)
            }
            mColor = savedInstanceState.getInt(COLOR_KEY)
            mShowCountTextView!!.setBackgroundColor(mColor)
        }
    }

    /**
     * Handles the onClick for the background color buttons. Gets background
     * color of the button that was clicked, and sets the TextView background
     * to that color.
     *
     * @param view The view (Button) that was clicked.
     */
    fun changeBackground(view: View) {
        val color: Int = (view.background as ColorDrawable).color
        mShowCountTextView!!.setBackgroundColor(color)
        mColor = color
    }

    /**
     * Handles the onClick for the Count button. Increments the value of the
     * mCount global and updates the TextView.
     *
     * @param view The view (Button) that was clicked.
     */
    fun countUp(view: View?) {
        mCount++
        mShowCountTextView!!.setText(String.format("%s", mCount))
    }

    /**
     * Saves the instance state if the activity is restarted (for example,
     * on device rotation.) Here you save the values for the count and the
     * background color.
     *
     * @param outState The state data.
     */
     override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(COUNT_KEY, mCount)
        outState.putInt(COLOR_KEY, mColor)
    }

    /**
     * Handles the onClick for the Reset button. Resets the global count and
     * background variables to the defaults and resets the views to those
     * default values.
     *
     * @param view The view (Button) that was clicked.
     */
    fun reset(view: View?) {
        // Reset count
        mCount = 0
        mShowCountTextView!!.text = String.format("%s", mCount)

        // Reset color
        mColor = ContextCompat.getColor(this, R.color.default_background)
        mShowCountTextView!!.setBackgroundColor(mColor)
    }
}
