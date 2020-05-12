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

import android.content.SharedPreferences
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

    // Reference to a SharedPreferences object
    // conventionally, it has the same name as the package name of your app
    // but you can rename it to anything you want to
    private var mPreferences: SharedPreferences? = null

    // File for storing shared preferences
    private val sharedPrefFile = "com.example.hellosharedprefs"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views, color
        mShowCountTextView = findViewById(R.id.count_textview)
        mColor = ContextCompat.getColor(this, R.color.default_background)

        // Initialize the shared preferences
        // The getSharedPreferences() method (from the activity Context)
        // opens the file at the given filename (sharedPrefFile) with the mode MODE_PRIVATE.
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE)

        // Restore preferences
        mCount = mPreferences!!.getInt(COUNT_KEY, 0)
        mColor = mPreferences!!.getInt(COLOR_KEY, mColor);

        // Update values with the pref values
        mShowCountTextView!!.text = String.format("%s", mCount)
        mShowCountTextView!!.setBackgroundColor(mColor)
    }

    /**
     * Saving preferences is a lot like saving the instance state;
     * both operations set aside the data to a Bundle object as a key/value pair.
     *
     * For shared preferences, however, you save that data in the onPause() lifecycle callback,
     * and you need a shared editor object (SharedPreferences.Editor) to write to the shared preferences object.
     */
    override fun onPause() {
        super.onPause()

        val preferencesEditor = mPreferences!!.edit()

        // write the changes
        preferencesEditor.putInt(COUNT_KEY, mCount)
        preferencesEditor.putInt(COLOR_KEY, mColor)

        // The apply() method saves the preferences asynchronously, off of the UI thread.
        // The shared preferences editor also has a commit() method to synchronously save the preferences.
        // NOTE: The commit() method is discouraged as it can block other operations!

        // save the changes
        preferencesEditor.apply()
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

        // Get an editor for the SharedPreferences object
        val preferencesEditor = mPreferences!!.edit()

        // Delete all the shared preferences
        preferencesEditor.clear()

        // Save
        preferencesEditor.apply()
    }
}
