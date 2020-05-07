# Notification Scheduler
A sample app using `JobScheduler` according to this Google [guide](https://codelabs.developers.google.com/codelabs/android-training-job-scheduler/index.html#0).
It is a demonstration of the `JobScheduler` framework by allowing the user to select constraints and schedule a job.
For example, job parameters can include the persistence of the job across reboots, whether the device is plugged in, or whether the device is idle.

- Pending implementation of [jobFinished()](https://codelabs.developers.google.com/codelabs/android-training-job-scheduler/index.html#5). Do consider this medium [article](https://medium.com/google-developers/scheduling-jobs-like-a-pro-with-jobscheduler-286ef8510129).

- What is covered:
    - How to implement a JobService.
    - How to construct a JobInfo object with specific constraints.
    - How to schedule a JobService based on the JobInfo object.

- What is done:
    - Implement a JobService that delivers a simple notification to let the user know the job is running.
    - Get user input to configure constraints (such as waiting until the device is charging) on the JobService.
    - Schedule the job using JobScheduler.

- JobScheduler.
    - It provides a flexible framework to intelligently accomplish background services.
    - It calculates the best time to schedule the execution of the job.
    - It is only available on devices running `API 21 and higher`, and is currently not available in the support library.
    - To use the it, you need two parts: `JobService` and `JobInfo`.
    - It is the manager class responsible for scheduling the task.
    - It batches tasks to maximize the efficiency of system resources, which means that you do not have exact control of when tasks are executed.

- JobInfo: an object containing the set of conditions that trigger a job to run.

- JobService: the implementation of the job that runs under the conditions set in the JobInfo object.
    - JobService is processed on the main thread, so you should avoid lengthy calculations or I/O.

- You only have to implement the `onStartJob()` and `onStopJob()` callback methods, which you do in your `JobService`.
    - The `implementation` of your job occurs, or is `started`, in `onStartJob()`.
    - The onStartJob() method returns a boolean value that indicates whether the service needs to process the work in a separate thread.
    - If onStartJob() returns true, you must explicitly call jobFinished().
    - If onStartJob() returns false, the runtime calls jobFinished() on your behalf.
