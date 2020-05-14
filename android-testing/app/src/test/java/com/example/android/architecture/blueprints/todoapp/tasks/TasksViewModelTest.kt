package com.example.android.architecture.blueprints.todoapp.tasks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.architecture.blueprints.todoapp.Event
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * There's a default test runner provided by JUnit that you get automatically.
 * @RunWith swaps out that default test runner.
 *
 * The AndroidJUnit4 test runner allows for AndroidX Test to run your test differently
 * depending on whether they are instrumented or local tests.
 */
@RunWith(AndroidJUnit4::class)
class TasksViewModelTest {

    // Subject under test
    private lateinit var tasksViewModel: TasksViewModel

    // InstantTaskExecutorRule is a JUnit Rule.
    // When you use it with the @get:Rule annotation,
    // it causes some code in the InstantTaskExecutorRule class to be run before and after the tests.
    //
    // This rule runs all Architecture Components-related background jobs in the same thread
    // so that the test results happen synchronously, and in a repeatable order.
    //
    // When you write tests that include testing LiveData, use this rule!
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        // Given a fresh TasksViewModel
        tasksViewModel = TasksViewModel(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun addNewTask_setsNewTaskEvent() {
//        // Create observer - no need for it to do anything!
//        val observer = Observer<Event<Unit>> {}
//        try {
//
//            // Observe the LiveData forever
//            tasksViewModel.newTaskEvent.observeForever(observer)
//
//            // When adding a new task
//            tasksViewModel.addNewTask()
//
//            // Then the new task event is triggered
//            val value = tasksViewModel.newTaskEvent.value
//            assertThat(value?.getContentIfNotHandled(), (not(nullValue())))
//
//        } finally {
//            // Whatever happens, don't forget to remove the observer!
//            tasksViewModel.newTaskEvent.removeObserver(observer)
//        }

        // Instead of using the above commented out chunck of code, use a LiveDataTestUtil as shown below
        // When adding a new task
        tasksViewModel.addNewTask()

        // Then the new task event is triggered
        val value = tasksViewModel.newTaskEvent.getOrAwaitValue()

        assertThat(value.getContentIfNotHandled(), not(nullValue()))
    }

    @Test
    fun setFilterAllTasks_tasksAddViewVisible() {
        // When the filter type is ALL_TASKS
        tasksViewModel.setFiltering(TasksFilterType.ALL_TASKS)

        // Then the "Add task" action is visible
        assertThat(tasksViewModel.tasksAddViewVisible.getOrAwaitValue(), `is` (true))
    }
}
