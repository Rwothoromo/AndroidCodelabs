# AndroidCodelabs

A number of Android Code Labs practiced to demonstrate/pick up on/reference some concepts

- [Notifications](NotifyMe).
- [Room](RoomWordSample).
- [JobScheduler](NotificationScheduler).
- [WorkManager](android-workmanager).
- [Unit test](SimpleCalc/app/src/test/java/com/example/simplecalc/CalculatorTest.kt).
- [Testing](android-testing).
	- `implementation` — The dependency is available in `all source sets`, including the test source sets.
	- `testImplementation` — The dependency is only available in the `test source set`.
	- `androidTestImplementation` — The dependency is only available in the `androidTest source set`.
- [Espresso for UI testing](TwoActivitiesLifecycle).
	- Match a View: Find a View.
	Use a ViewMatcher to find a View: `onView(withId(R.id.my_view))`
	- Perform an action: Perform a click or other action that triggers an event with the View.
	Use a ViewAction to perform an action: `.perform(click())`
	- Assert and verify the result:
	Check the state of the View to see if it reflects the expected state or behavior defined by the assertion.
	Use a ViewAssertion to check if the result of the action matches an assertion: `.check(matches(isDisplayed()))`
