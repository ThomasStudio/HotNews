# Android Test-Driven Development (TDD) Best Practices

with MVVM, Jetpack Compose, Coroutines, and Hilt

## 1. Introduction

Test-Driven Development (TDD) is a software development practice where tests are written before the production code. When combined with modern Android architectural components — MVVM, Jetpack Compose, Kotlin Coroutines, and Hilt — TDD becomes a powerful tool for building robust, maintainable, and testable applications.

This document provides a comprehensive guide to applying TDD in Android projects using these technologies. It covers core principles, practical implementation steps, testing strategies for each layer, and common pitfalls with solutions.

## 2. TDD Fundamentals (Red-Green-Refactor)

TDD follows a simple, iterative cycle:

| Phase       | Action                                                                    |
| ----------- | ------------------------------------------------------------------------- |
| 🔴 Red      | Write a test that fails (because the feature doesn't exist yet)           |
| 🟢 Green    | Write the minimal code required to make the test pass                     |
| 🔵 Refactor | Improve the code structure without changing behavior, keeping tests green |

Repeat this cycle for each small piece of functionality.

### Key Benefits for Android

- **Testable architecture from day one** – TDD forces you to design loosely coupled components
- **Instant feedback** – Fast unit tests catch regressions immediately
- **Living documentation** – Tests describe how the code should behave
- **Fearless refactoring** – A passing test suite gives confidence to change code

## 3. MVVM Architecture for Testability

MVVM (Model-View-ViewModel) is the recommended architecture pattern for Android. It naturally supports TDD because it separates concerns clearly.

```
┌─────────────────────────────────────────────────────────┐
│                         VIEW                             │
│           (Activity / Fragment / Composable)             │
│              - Observes ViewModel state                  │
│              - Sends user events to ViewModel            │
└───────────────────────────┬─────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────┐
│                       VIEWMODEL                          │
│              - Exposes StateFlow / LiveData              │
│              - Contains UI logic & event handling        │
│              - Uses UseCases / Repositories              │
└───────────────────────────┬─────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────┐
│                        MODEL                             │
│           (Repository / UseCase / DataSource)            │
│              - Business logic & data operations          │
│              - Abstracts data sources (local/remote)     │
└─────────────────────────────────────────────────────────┘
```

### Why MVVM works well with TDD

- **ViewModel is platform‑independent** (no Android framework dependencies if you avoid AndroidViewModel). It can be unit‑tested with JUnit
- **Repository classes are plain Kotlin objects** – easy to test with fakes or mocks
- **View (Compose UI) is tested separately** with Compose testing APIs, focusing on state observation and event emission

## 4. Setting Up Dependencies with Hilt for Testability

Hilt is the standard DI library for Android. It makes testing easier because you can replace production bindings with test‑specific ones.

### 4.1 Basic Hilt Setup for Production

```kotlin
// Application class
@HiltAndroidApp
class MyApp : Application()

// ViewModel
@HiltViewModel
class MyViewModel @Inject constructor(
    private val repository: MyRepository
) : ViewModel() { ... }

// Module
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    fun provideMyRepository(api: ApiService, dao: MyDao): MyRepository {
        return MyRepositoryImpl(api, dao)
    }
}
```

### 4.2 Hilt Testing Setup

For instrumented tests (AndroidTest), Hilt provides HiltAndroidRule and test components.

**build.gradle (app level)**

```kotlin
androidTestImplementation "com.google.dagger:hilt-android-testing:2.48"
androidTestImplementation "androidx.test:runner:1.5.2"
kaptAndroidTest "com.google.dagger:hilt-android-compiler:2.48"
```

**Test Base Class Example**

```kotlin
@HiltAndroidTest
@UninstallModules(RepositoryModule::class) // Replace module for tests
abstract class HiltTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun init() {
        hiltRule.inject()
    }
}
```

**Replace production bindings with fakes**

```kotlin
@TestModule
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)
object TestRepositoryModule {
    @Provides
    fun provideTestRepository(): MyRepository = FakeMyRepository()
}
```

## 5. Unit Testing ViewModels with Coroutines

ViewModels typically expose StateFlow or SharedFlow for UI state. Testing them requires coroutine test utilities and proper Dispatcher management.

### 5.1 Example ViewModel

```kotlin
data class MyUiState(
    val isLoading: Boolean = false,
    val data: List<String> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class MyViewModel @Inject constructor(
    private val repository: MyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyUiState())
    val uiState: StateFlow<MyUiState> = _uiState.asStateFlow()

    fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val result = repository.fetchData()
                _uiState.update { it.copy(isLoading = false, data = result) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}
```

### 5.2 Testing ViewModel with Coroutines

Key tools:

- `runTest` from kotlinx-coroutines-test
- `UnconfinedTestDispatcher` or `StandardTestDispatcher`
- `advanceUntilIdle()` to force coroutines to complete

**Test Class Example**

```kotlin
class MyViewModelTest {

    private lateinit var viewModel: MyViewModel
    private val repository: MyRepository = mockk()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = MyViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadData emits loading then success state`() = runTest {
        // Given
        val fakeData = listOf("Item1", "Item2")
        coEvery { repository.fetchData() } returns fakeData

        // When
        viewModel.loadData()
        advanceUntilIdle()

        // Then
        val states = viewModel.uiState.toList()
        assertThat(states[0].isLoading).isTrue()
        assertThat(states[1].isLoading).isFalse()
        assertThat(states[1].data).isEqualTo(fakeData)
    }

    @Test
    fun `loadData handles error state`() = runTest {
        // Given
        coEvery { repository.fetchData() } throws IOException("Network error")

        // When
        viewModel.loadData()
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertThat(state.isLoading).isFalse()
        assertThat(state.error).isEqualTo("Network error")
    }
}
```

### 5.3 Best Practices for Coroutine Testing

| Practice                                                                                            | Reason                                                                  |
| --------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------- |
| Use `Dispatchers.setMain(testDispatcher)` in `@Before`                                          | Avoids IllegalStateException when ViewModel uses viewModelScope         |
| Prefer `UnconfinedTestDispatcher` for simple tests                                                | Runs coroutines eagerly, reducing need for `advanceUntilIdle`         |
| Use `StandardTestDispatcher` + `advanceUntilIdle` when you need to control execution order      | Simulates real coroutine scheduling                                     |
| Inject CoroutineDispatcher as a dependency (optional)                                               | Makes testing even more flexible, but often not required for ViewModels |
| Use `coEvery { }` (MockK) or `whenever { }.thenReturn()` (Mockito Kotlin) for suspend functions | Regular mocks don't work with suspend functions                         |

## 6. Testing Repositories and Use Cases

Repositories often combine multiple data sources (network, database, cache). Use fakes (simple in‑memory implementations) instead of mocks to keep tests reliable.

### 6.1 Example Repository

```kotlin
interface MyRepository {
    suspend fun fetchData(): List<String>
}

class MyRepositoryImpl(
    private val api: ApiService,
    private val dao: MyDao
) : MyRepository {
    override suspend fun fetchData(): List<String> {
        val remote = api.getData()
        dao.insertAll(remote)
        return remote
    }
}
```

### 6.2 Testing with Fakes

**Fake API Service**

```kotlin
class FakeApiService : ApiService {
    var shouldThrowError = false
    var dataToReturn = listOf("Default")

    override suspend fun getData(): List<String> {
        if (shouldThrowError) throw IOException("Fake error")
        return dataToReturn
    }
}
```

**Fake DAO**

```kotlin
class FakeMyDao : MyDao {
    private val storage = mutableListOf<String>()

    override suspend fun insertAll(items: List<String>) {
        storage.addAll(items)
    }

    override suspend fun getAll(): List<String> = storage.toList()
}
```

**Repository Test**

```kotlin
class MyRepositoryTest {
    private lateinit var fakeApi: FakeApiService
    private lateinit var fakeDao: FakeMyDao
    private lateinit var repository: MyRepository

    @Before
    fun setup() {
        fakeApi = FakeApiService()
        fakeDao = FakeMyDao()
        repository = MyRepositoryImpl(fakeApi, fakeDao)
    }

    @Test
    fun `fetchData returns data from API and saves to DAO`() = runTest {
        // Given
        fakeApi.dataToReturn = listOf("A", "B")

        // When
        val result = repository.fetchData()

        // Then
        assertThat(result).containsExactly("A", "B")
        assertThat(fakeDao.getAll()).containsExactly("A", "B")
    }

    @Test
    fun `fetchData propagates API errors`() = runTest {
        // Given
        fakeApi.shouldThrowError = true

        // When / Then
        assertThrows<IOException> {
            runBlocking { repository.fetchData() }
        }
    }
}
```

### 6.3 Why Fakes over Mocks?

- **Less brittle** – Fakes behave like real implementations but simpler
- **Reusable** – Same fake can be used across many tests
- **No stubbing boilerplate** – No need to mock every method call

## 7. Testing Jetpack Compose UI

Compose UI testing uses `ComposeTestRule` or `createComposeRule()`. With TDD, you write UI tests before building the composable.

### 7.1 Basic Compose Test Setup

```kotlin
@RunWith(AndroidJUnit4::class)
class MyScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `displays loading indicator when isLoading is true`() {
        // Given
        val uiState = MyUiState(isLoading = true)

        // When
        composeTestRule.setContent {
            MyScreen(uiState = uiState, onEvent = {})
        }

        // Then
        composeTestRule.onNodeWithTag("loading_indicator")
            .assertIsDisplayed()
    }
}
```

### 7.2 Testing Interaction with ViewModel (Hilt + Compose)

For integration tests, use Hilt to inject a real or fake ViewModel.

```kotlin
@HiltAndroidTest
@UninstallModules(RepositoryModule::class)
class MyScreenIntegrationTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun `clicking refresh button loads new data`() {
        // Replace module to provide fake repository
        val fakeRepo = FakeMyRepository()
        // (Assume Hilt module provides fakeRepo)

        composeTestRule.setContent {
            MyScreenRoot() // Screen with injected ViewModel
        }

        composeTestRule.onNodeWithText("Refresh").performClick()

        composeTestRule.onNodeWithText("Loaded Item")
            .assertIsDisplayed()
    }
}
```

### 7.3 Compose Testing Best Practices

| Practice                                                                  | Description                                                                 |
| ------------------------------------------------------------------------- | --------------------------------------------------------------------------- |
| Use test tags                                                             | `Modifier.testTag("id")` makes nodes easy to find without relying on text |
| Prefer `assertExists` / `assertIsDisplayed`                           | Avoid checking implementation details                                       |
| Test only what the user sees/interacts with                               | Don't test internal composable state directly                               |
| Use `SemanticsMatcher` for custom assertions                            | Extend when default matchers are insufficient                               |
| Keep UI tests minimal – test most logic in ViewModel unit tests          | UI tests are slower; use them for critical user journeys                    |
| For screenshot testing – use libraries like Paparazzi (not covered here) | Complementary to functional UI tests                                        |

## 8. Putting It All Together: TDD Workflow with MVVM + Compose + Coroutines + Hilt

Let's walk through a realistic TDD cycle for a small feature: fetch and display a list of notes.

**Step 1: Write a failing ViewModel unit test (Red)**

```kotlin
@Test
fun `uiState initially shows loading then notes`() = runTest {
    val viewModel = NotesViewModel(fakeRepository)
    assertEquals(NotesUiState(isLoading = true), viewModel.uiState.value)

    advanceUntilIdle()
    assertEquals(
        NotesUiState(notes = listOf(Note("Title 1"))),
        viewModel.uiState.value
    )
}
```

**Step 2: Implement minimal ViewModel (Green)**

```kotlin
class NotesViewModel(
    private val repository: NotesRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(NotesUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val notes = repository.getNotes()
            _uiState.value = NotesUiState(notes = notes)
        }
    }
}
```

**Step 3: Refactor** (extract logic, improve naming) – tests stay green.

**Step 4: Write a failing Compose UI test (Red)**

```kotlin
@Test
fun `displays list of notes when loaded`() {
    composeTestRule.setContent {
        NotesScreen(viewModel = fakeViewModelWithNotes)
    }

    composeTestRule.onNodeWithText("Title 1").assertIsDisplayed()
}
```

**Step 5: Implement Compose UI (Green)**

```kotlin
@Composable
fun NotesScreen(viewModel: NotesViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LazyColumn {
        items(uiState.notes) { note ->
            Text(text = note.title)
        }
    }
}
```

**Step 6: Inject Hilt dependencies** – write a test module for fake repository.

```kotlin
@TestInstallIn(components = [SingletonComponent::class], replaces = [RepositoryModule::class])
@TestModule
object TestRepositoryModule {
    @Provides
    fun provideNotesRepository(): NotesRepository = FakeNotesRepository()
}
```

**Step 7: Run integration test with Hilt and Compose to ensure DI works.**

```kotlin
@HiltAndroidTest
class NotesScreenIntegrationTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    @Test
    fun fullFlow_showsNotesAfterLoading() {
        composeTestRule.setContent {
            NotesScreen() // Hilt injects fake repo
        }
        composeTestRule.onNodeWithText("Sample Note").assertIsDisplayed()
    }
}
```

## 9. Common Pitfalls and Solutions

| Pitfall                                                   | Solution                                                                                  |
| --------------------------------------------------------- | ----------------------------------------------------------------------------------------- |
| Using `AndroidViewModel` with Application context       | Avoid unless necessary; use `ViewModel` with injected dependencies. Test becomes harder |
| Hardcoded `Dispatcher.Main` in coroutines               | Inject `CoroutineDispatcher` or use `Dispatchers.setMain` in tests                    |
| Testing internal ViewModel functions                      | Test only public API (uiState emissions and event methods)                                |
| Mocking everything                                        | Use fakes for data layer; mock only boundaries (e.g., interfaces to external libraries)   |
| Forgetting to `advanceUntilIdle()` in coroutine tests   | Always call after launching coroutines in `runTest`                                     |
| UI tests that depend on real network                      | Use fake repository + Hilt test module to provide deterministic data                      |
| Not using `@VisibleForTesting` for exposed test helpers | Keep test-only functions annotated to signal intent                                       |
| Slow test execution                                       | Prefer unit tests (JVM) over instrumented tests. Use Robolectric sparingly                |

## 10. Test Pyramid for This Stack

| Layer              | Test Type                                  | Tools                               | Speed   | Proportion |
| ------------------ | ------------------------------------------ | ----------------------------------- | ------- | ---------- |
| Domain / Use Cases | Unit tests                                 | JUnit, MockK, runTest               | Fast    | 60%        |
| Repository / Data  | Unit tests with fakes                      | JUnit, Kotlin Coroutines Test       | Fast    | 20%        |
| ViewModel          | Unit tests with runTest, StateFlow testing | JUnit, Turbine                      | Fast    | 10%        |
| Compose UI         | Instrumented UI tests                      | ComposeTestRule, Hilt               | Slow    | 5%         |
| End-to-end         | Instrumented (full app)                    | ComposeTestRule + real Hilt modules | Slowest | 5%         |

## 11. Sample Project Structure

```
app/
├── src/
│   ├── main/
│   │   ├── kotlin/com/example/
│   │   │   ├── di/                 # Hilt modules
│   │   │   ├── data/               # Repository, data sources, models
│   │   │   ├── domain/             # Use cases
│   │   │   ├── presentation/
│   │   │   │   ├── ui/             # Compose screens
│   │   │   │   └── viewmodel/      # ViewModels
│   │   └── res/
│   ├── test/                       # Unit tests (JVM)
│   │   ├── kotlin/com/example/
│   │   │   ├── data/               # Fake implementations & repository tests
│   │   │   ├── domain/             # Use case tests
│   │   │   └── presentation/viewmodel/
│   └── androidTest/                # Instrumented tests
│       ├── kotlin/com/example/
│       │   ├── di/                 # Test Hilt modules
│       │   └── ui/                 # Compose UI tests & integration tests
│       └── res/
```

## 12. Recommended Libraries and Versions

```kotlin
// Unit testing
testImplementation "junit:junit:4.13.2"
testImplementation "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3"
testImplementation "io.mockk:mockk:1.13.8"
testImplementation "app.cash.turbine:turbine:1.0.0"   // StateFlow testing

// Instrumented tests
androidTestImplementation "androidx.test.ext:junit:1.1.5"
androidTestImplementation "androidx.test.espresso:espresso-core:3.5.1"
androidTestImplementation "androidx.compose.ui:ui-test-junit4:1.6.0"
debugImplementation "androidx.compose.ui:ui-test-manifest:1.6.0"

// Hilt testing
androidTestImplementation "com.google.dagger:hilt-android-testing:2.48"
kaptAndroidTest "com.google.dagger:hilt-android-compiler:2.48"

// Optional: Robolectric for ViewModel tests that need Android resources
testImplementation "org.robolectric:robolectric:4.11.1"
```

## 13. Summary of Best Practices

| Aspect           | Recommendation                                                                                             |
| ---------------- | ---------------------------------------------------------------------------------------------------------- |
| TDD Cycle        | Write test → fail → minimal implementation → pass → refactor. Repeat                                   |
| MVVM             | Keep ViewModel free of Android framework classes. Test with plain JUnit                                    |
| Coroutines       | Use `runTest` + `Dispatchers.setMain` in unit tests. Prefer `UnconfinedTestDispatcher`               |
| Hilt             | Use `@UninstallModules` and test modules to inject fakes. Write instrumented integration tests sparingly |
| Compose UI       | Write small UI tests with `ComposeTestRule`. Use test tags. Keep most logic in ViewModel unit tests      |
| Fakes over mocks | Implement in‑memory fakes for repositories, databases, and APIs. Reuse across tests                       |
| Test naming      | Use backticks with descriptive phrases: "loadData shows error on failure"                                  |
| Code coverage    | Aim for 80%+ on critical business logic. Don't obsess over 100%                                            |

## 14. Conclusion

TDD, when combined with MVVM, Jetpack Compose, Coroutines, and Hilt, provides a robust workflow for building high‑quality Android applications. The separation of concerns enforced by MVVM makes unit testing straightforward; Hilt gives you clean dependency injection that can be swapped out for tests; Compose offers a declarative UI that is easy to test with `ComposeTestRule`; and coroutines, with their test utilities, allow you to write deterministic asynchronous tests.

Adopting TDD requires discipline, especially in the beginning, but the payoff is a more reliable, maintainable, and bug‑free application. Start small — pick one ViewModel and write its tests first — and gradually expand the practice to the whole project.
