package com.example.twoactivitieslifecycle

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ActivityInputOutputTest {

    @get:Rule
    var activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun activityLaunch() {
        // UI Testing Steps:
        // 1. Match a View: Find a View.
        // Use a ViewMatcher to find a View: onView(withId(R.id.my_view))

        // 2. Perform an action: Perform a click or other action that triggers an event with the View.
        // Use a ViewAction to perform an action: .perform(click())

        // 3. Assert and verify the result:
        // Check the state of the View to see if it reflects the expected state or behavior defined by the assertion.
        // Use a ViewAssertion to check if the result of the action matches an assertion: .check(matches(isDisplayed()))

        onView(withId(R.id.button_main)).perform(click())
        onView(withId(R.id.text_header)).check(matches(isDisplayed()))
        onView(withId(R.id.button_second)).perform(click());
        onView(withId(R.id.text_header_reply)).check(matches(isDisplayed()));
    }

    @Test
    fun textInputOutput() {
        onView(withId(R.id.editText_main)).perform(typeText("This is a test."))
        onView(withId(R.id.button_main)).perform(click())
        onView(withId(R.id.text_message)).check(matches(withText("This is a test.")));
    }
}
