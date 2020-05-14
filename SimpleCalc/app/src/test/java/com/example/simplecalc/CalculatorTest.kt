/*
 * Copyright 2018, Google Inc.
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
package com.example.simplecalc

//import androidx.test.filters.SmallTest
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * JUnit4 unit tests for the calculator logic. These are local unit tests; no device needed.
 */
@RunWith(JUnit4::class)
//@SmallTest
class CalculatorTest {
    // member variables
    private var mCalculator: Calculator? = null

    /**
     * Set up the environment for testing
     */
    @Before
    fun setUp() {
        // create an instance of the Calculator class
        mCalculator = Calculator()
    }

    /**
     * Test for simple addition
     *
     * Only methods in a test class that have an @Test annotation are considered tests to the test runner.
     * Note that by convention test methods do not include the word "test."
     */
    @Test
    fun addTwoNumbers() {
        val resultAdd = mCalculator!!.add(1.0, 1.0)
        Assert.assertThat(
            resultAdd,
            CoreMatchers.`is`(CoreMatchers.equalTo(2.0))
        )
    }
}
