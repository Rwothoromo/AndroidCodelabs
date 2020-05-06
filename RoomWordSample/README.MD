# RoomWordSample

A sample Google [guide](https://codelabs.developers.google.com/codelabs/android-room-with-a-view-kotlin/#0) on how to use Room. It covers:
- DAO (data access objects).
    - Must be an interface or abstract class.
    - You specify SQL queries and associate them with method calls.
    - The compiler checks the SQL and generates queries from convenience annotations for common queries, such as @Insert.
    - Here, the DAO is used for `Getting all words ordered alphabetically`, `Inserting a word` and `Deleting all words`.
    - DAO maps method calls to database queries, so that when the Repository calls a method such as getAlphabetizedWords(),
    Room can execute `SELECT * from words ORDER BY word ASC`.

- Room. Room is a database layer on top of an SQLite database.
    - Room takes care of mundane tasks that you used to handle with an SQLiteOpenHelper.
    - By default, to avoid poor UI performance, Room doesn't allow you to issue queries on the main thread.
    When Room queries return LiveData, the queries are automatically run asynchronously on a background thread.
    - Room provides compile-time checks of SQLite statements.
    - Room uses the DAO to issue queries to its database.
    - Room uses the DAO to create a clean API for your code.
    - By default, all queries must be executed on a separate thread.
    - Room has coroutines support, allowing your queries to be annotated with the suspend modifier
    and then called from a coroutine or from another suspension function.
    - Room is a wrapper `around` and implements a SQLite database.
    - Room does a lot of work for you that you used to have to do yourself.

- LiveData.
    - When data changes you usually want to take some action, such as displaying the updated data in the UI.
    This means you have to observe the data so that when it changes, you can react.
    Depending on how the data is stored, this can be tricky.
    Observing changes to data across multiple components of your app can create explicit, rigid dependency paths between the components.
    This makes testing and debugging difficult, among other things.
    - LiveData, a lifecycle library class for data observation, solves this problem.
    Use a return value of type LiveData in your method description,
    and Room generates all necessary code to update the LiveData when the database is updated.
    - In the ViewModel, use LiveData for changeable data that the UI will use or display. Using LiveData has several benefits:
        - You can put an observer on the data (instead of polling for changes) and only update the the UI when the data actually changes.
        - The Repository and the UI are completely separated by the ViewModel.
        - There are no database calls from the ViewModel (this is all handled in the Repository), making the code more testable.

- Migrations are not covered here but do see this [guide](https://medium.com/androiddevelopers/understanding-migrations-with-room-f01e04b07929).

- Repository.
    - This class abstracts access to multiple data sources.
    - It is not part of the Architecture Components libraries, but is a suggested best practice for code separation and architecture.
    - It provides a clean API for data access to the rest of the application.
        - Repository -> Dao
        - Repository -> Network
    - A Repository manages queries and allows you to use multiple backends. In the most common example,
    the Repository implements the logic for deciding whether to fetch data from a network or use results cached in a local database.
    - Repositories are meant to mediate between different data sources. Here, we only have one data source.
    For a more complex implementation, see this [example](https://github.com/android/architecture-components-samples/tree/master/BasicSample).
    - The Repository exposes methods for the ViewModel to interact with the underlying data provider. In this app, that backend is a Room database.

- ViewModel. It holds your app's UI data in a lifecycle-conscious way that survives configuration changes.
    - It's role is to provide data to the UI and survive configuration changes.
    - A ViewModel acts as a communication center between the Repository and the UI.
    - You can also use a ViewModel to share data between fragments.
    - The ViewModel is part of the lifecycle library.
    - Separating your app's UI data from your Activity and Fragment classes lets you better follow the single responsibility principle:
    Your activities and fragments are responsible for drawing data to the screen,
    while your ViewModel can take care of holding and processing all the data needed for the UI.

- viewModelScope.
    - In Kotlin, all coroutines run inside a CoroutineScope.
    - A scope controls the lifetime of coroutines through its job.
    - When you cancel the job of a scope, it cancels all coroutines started in that scope.
    - The AndroidX lifecycle-viewmodel-ktx library adds a viewModelScope as an extension function of the ViewModel class,
    enabling you to work with scopes.

- RecyclerView. MainActivity displays words in a list using a RecyclerView and the WordListAdapter.

- ViewModelProvider.
    - When the `activity` first `starts`, the ViewModelProviders will `create the ViewModel`.
    - When the activity is `destroyed`, for example through a configuration change, `the ViewModel persists`.
    - When the activity is `re-created`, the ViewModelProviders `return the existing ViewModel`.
