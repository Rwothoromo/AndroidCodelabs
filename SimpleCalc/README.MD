# SimpleCalc

A simple calculator. Behind the scenes, the focus is on testing.

## Annotations

- `@JUnit4` means `JUnit4` is the runner used.
For tests with more complicated setup or infrastructure requirements (such as Espresso) you'll use different test runners.
- The `@SmallTest` annotation indicates that all the tests in a class are unit tests that have no dependencies, and run in milliseconds.
- The `@SmallTest`, `@MediumTest`, and `@LargeTest` annotations are conventions that make it easier to bundle groups of tests into suites of similar functionality.
