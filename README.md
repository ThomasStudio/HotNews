# AcFun

[https://www.acfun.cn/rest/pc-direct/rank/channel?subChannelId=&amp;rankLimit=50&amp;](https://www.acfun.cn/rest/pc-direct/rank/channel?subChannelId=&rankLimit=50&)

[https://www.acfun.cn/rest/pc-direct/rank/channel?channelId=&amp;subChannelId=&amp;rankLimit=50&amp;rankPeriod=DAY](https://www.acfun.cn/rest/pc-direct/rank/channel?channelId=&subChannelId=&rankLimit=50&rankPeriod=DAY)

[https://www.acfun.cn/rest/pc-direct/rank/channel?channelId=&amp;subChannelId=&amp;rankLimit=50&amp;rankPeriod=THREE_DAYS](https://www.acfun.cn/rest/pc-direct/rank/channel?channelId=&subChannelId=&rankLimit=50&rankPeriod=THREE_DAYS)

[https://www.acfun.cn/rest/pc-direct/rank/channel?channelId=&amp;subChannelId=&amp;rankLimit=50&amp;rankPeriod=WEEK](https://www.acfun.cn/rest/pc-direct/rank/channel?channelId=&subChannelId=&rankLimit=50&rankPeriod=WEEK)

# Bilibili

[https://api.bilibili.com/x/web-interface/ranking?type=1&amp;arc_type=0&amp;jsonp=jsonp](https://api.bilibili.com/x/web-interface/ranking?type=1&arc_type=0&jsonp=jsonp)

# Weibo

api: [https://v2.xxapi.cn/api/weibohot](https://v2.xxapi.cn/api/weibohot)

webpage: [https://s.weibo.com/top/summary?cate=realtimehot](https://s.weibo.com/top/summary?cate=realtimehot)

# Zhihu

[https://www.zhihu.com/api/v3/feed/topstory/hot-lists/total?limit=50](https://www.zhihu.com/api/v3/feed/topstory/hot-lists/total?limit=50)


## Recommended Folder Structure

**text**

```
app/src/main/java/com/yourapp/
├── data/                      # Model layer (data sources, repositories, domain models)
│   ├── models/                # Domain entities (e.g. data classes)
│   ├── repositories/          # Repository implementations
│   └── network/               # Retrofit interfaces, API clients
├── ui/                        # Presentation layer
│   ├── theme/                 # Compose theme, colors, typography
│   ├── components/            # Reusable composables (buttons, dialogs, etc.)
│   ├── home/                  # Feature: Home
│   │   ├── HomeScreen.kt      # View (Composables)
│   │   ├── HomeViewModel.kt   # ViewModel (StateFlow, SharedFlow)
│   │   ├── HomeUiState.kt     # UI state model (data class)
│   │   └── HomeUiEvent.kt     # Event model (sealed class)
│   ├── detail/                # Feature: Detail
│   │   ├── DetailScreen.kt
│   │   ├── DetailViewModel.kt
│   │   ├── DetailUiState.kt
│   │   └── DetailUiEvent.kt
│   └── navigation/            # Navigation (router)
│       ├── NavGraph.kt        # NavHost with all destinations
│       ├── Routes.kt          # Route constants
│       └── NavigationEvent.kt # Shared navigation events (optional)
└── MainActivity.kt            # Entry point
```

 **Key idea** : Each feature (e.g. `home`, `detail`) has its own  **View** ,  **ViewModel** ,  **UI State** , and **UI Events** in the same package. The `navigation` folder contains the router logic and route definitions, which are used by the ViewModel to emit navigation events and by the View to perform actual navigation.

---

## Detailed File Breakdown

### 1. Model (Data Layer)

* **Domain models** – plain data classes representing business objects.
* **Repositories** – sources of truth (local database, network).
* **Data sources** – Retrofit services, Room DAOs.

Example: `data/models/Item.kt`

**kotlin**

```
data class Item(val id: String, val name: String)
```

### 2. UI State (within feature folder)

Define the state that drives a screen.

**`ui/home/HomeUiState.kt`**

**kotlin**

```
data class HomeUiState(
    val isLoading: Boolean = false,
    val items: List<Item> = emptyList(),
    val error: String? = null
)
```

### 3. UI Events (within feature folder)

Define one‑time events like navigation, toasts, etc.

**`ui/home/HomeUiEvent.kt`**

**kotlin**

```
sealed class HomeUiEvent {
    data class NavigateToDetail(val itemId: String) : HomeUiEvent()
    data class ShowToast(val message: String) : HomeUiEvent()
}
```

### 4. ViewModel

Manages UI state (`StateFlow`) and emits events (`SharedFlow`). Contains business logic and navigation commands.

**`ui/home/HomeViewModel.kt`**

**kotlin**

```
class HomeViewModel(
    private val repository: ItemRepository
) : ViewModel() {

    // UI state
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    // Events (one-time)
    private val _eventFlow = MutableSharedFlow<HomeUiEvent>()
    val eventFlow: SharedFlow<HomeUiEvent> = _eventFlow.asSharedFlow()

    // Update UI state safely
    private fun updateState(block: HomeUiState.() -> HomeUiState) {
        _uiState.update(block)
    }

    // Send an event to the View
    private fun sendEvent(event: HomeUiEvent) {
        viewModelScope.launch { _eventFlow.emit(event) }
    }

    // Called by View to load data
    fun loadData() {
        viewModelScope.launch {
            updateState { copy(isLoading = true, error = null) }
            try {
                val items = repository.fetchItems()
                updateState { copy(isLoading = false, items = items) }
            } catch (e: Exception) {
                updateState { copy(isLoading = false, error = e.message) }
                sendEvent(HomeUiEvent.ShowToast("Failed to load items"))
            }
        }
    }

    // Navigation – ViewModel sends an event; View handles actual navigation
    fun onItemClick(itemId: String) {
        sendEvent(HomeUiEvent.NavigateToDetail(itemId))
    }
}
```

### 5. View (Composables)

Observes `uiState` and `eventFlow` from the ViewModel, renders UI, and performs navigation.

**`ui/home/HomeScreen.kt`**

**kotlin**

```
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToDetail: (String) -> Unit  // Navigation callback
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Collect events
    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is HomeUiEvent.NavigateToDetail -> onNavigateToDetail(event.itemId)
                is HomeUiEvent.ShowToast -> {
                    // Show toast (using Context)
                    val context = LocalContext.current
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // UI content based on uiState
    when {
        uiState.isLoading -> CircularProgressIndicator()
        uiState.error != null -> ErrorView(uiState.error) { viewModel.loadData() }
        else -> ItemList(uiState.items, viewModel::onItemClick)
    }

    // Load data when screen first appears
    LaunchedEffect(Unit) {
        viewModel.loadData()
    }
}

@Composable
private fun ItemList(items: List<Item>, onItemClick: (String) -> Unit) {
    LazyColumn {
        items(items) { item ->
            Text(
                text = item.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onItemClick(item.id) }
                    .padding(16.dp)
            )
        }
    }
}

@Composable
private fun ErrorView(message: String, onRetry: () -> Unit) {
    Column {
        Text("Error: $message")
        Button(onClick = onRetry) { Text("Retry") }
    }
}
```

### 6. Navigation (Router)

Define routes and set up `NavHost`.

**`ui/navigation/Routes.kt`**

**kotlin**

```
object Routes {
    const val HOME = "home"
    const val DETAIL = "detail/{itemId}"
    fun detail(itemId: String) = "detail/$itemId"
}
```

**`ui/navigation/NavGraph.kt`**

**kotlin**

```
@Composable
fun AppNavHost(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    detailViewModel: DetailViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
        composable(Routes.HOME) {
            HomeScreen(
                viewModel = homeViewModel,
                onNavigateToDetail = { itemId ->
                    navController.navigate(Routes.detail(itemId))
                }
            )
        }
        composable(Routes.DETAIL) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId") ?: ""
            DetailScreen(
                viewModel = detailViewModel,
                itemId = itemId
            )
        }
    }
}
```

**`MainActivity.kt`**

**kotlin**

```
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YourAppTheme {
                val navController = rememberNavController()
                // Provide ViewModels via hilt or viewModel()
                val homeViewModel: HomeViewModel = viewModel()
                val detailViewModel: DetailViewModel = viewModel()
                AppNavHost(
                    navController = navController,
                    homeViewModel = homeViewModel,
                    detailViewModel = detailViewModel
                )
            }
        }
    }
}
```
