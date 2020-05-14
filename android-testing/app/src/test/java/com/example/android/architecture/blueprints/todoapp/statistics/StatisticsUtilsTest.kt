package com.example.android.architecture.blueprints.todoapp.statistics

import com.example.android.architecture.blueprints.todoapp.data.Task
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

class StatisticsUtilsTest {

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun getActiveAndCompletedStats_noCompleted_returnsHundredZero() {
        // Create an active task
        val tasks = listOf<Task>(
                Task("title", "desc", isCompleted = false)
        )

        // Call your function
        val result = getActiveAndCompletedStats(tasks)

        // Check the result
        assertEquals(result.completedTasksPercent, 0f)
        assertEquals(result.activeTasksPercent, 100f)
    }
}
