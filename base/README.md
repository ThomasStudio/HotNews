# Base Library Module

The base library module contains common utilities, base classes, and shared functionality that can be reused across different modules in the HotNews application.

## Architecture Components

### 1. BaseViewModel
- Uses StateFlow for uiState management with `updateState()` method
- Uses SharedFlow for event handling with `send()` method
- Provides `collectState()` and `collectEvents()` composable functions
- Implements generic State and Event types for flexibility

### 2. Navigation System
- **Router**: Base class to generate route strings used by ViewModel
- **Navigator**: Base class to navigate with route strings using NavController
- **NavGraph**: Base class to generate navigation graphs

### 3. Repository Pattern
- **BaseRepository**: Interface defining common repository operations

### 4. Use Cases
- **BaseUseCase**: Abstract class for implementing clean architecture use cases

### 5. Common Utilities
- **Resource**: Sealed class for handling data loading states (Success, Error, Loading)
- **Exceptions**: Base exception hierarchy for consistent error handling
- **Extensions**: Utility extension functions
- **Constants**: Common constants used throughout the app

## Usage Examples

### ViewModel Implementation
```kotlin
class MyViewModel : BaseViewModel<MyState, MyEvent>(MyState.Initial) {
    fun loadData() {
        updateState(MyState.Loading)
        
        viewModelScope.launch {
            // Perform async operation
            val result = repository.getData()
            
            if (result.isSuccess) {
                updateState(MyState.Success(result.data))
            } else {
                updateState(MyState.Error(result.message))
            }
        }
    }
    
    fun navigateToDetails() {
        send(MyEvent.NavigateTo(DetailsRouter.getRoute()))
    }
}
```

### UI Implementation
```kotlin
@Composable
fun MyScreen(viewModel: MyViewModel = hiltViewModel()) {
    val state by viewModel.collectState()
    
    viewModel.collectEvents { event ->
        when (event) {
            is MyEvent.NavigateTo -> {
                // Use Navigator to navigate
                navigator.navigate(event.route)
            }
        }
    }
    
    when (state) {
        is MyState.Loading -> {
            CircularProgressIndicator()
        }
        is MyState.Success -> {
            Text(state.data)
        }
        // ... other states
    }
}
```

### Navigation Setup
```kotlin
// In MainActivity
setContent {
    val navController = rememberNavController()
    navigator.bind(navController)
    
    NavHost(navController = navController, startDestination = "home") {
        // Add screens using NavGraph implementation
    }
}
```

## Design Principles

1. **MVVM**: Clear separation between View, ViewModel, and Model
2. **Coroutines**: Asynchronous operations using Kotlin Coroutines
3. **Compose UI**: Modern declarative UI framework
4. **Navigation**: Type-safe navigation with NavHost
5. **Hilt**: Dependency injection throughout the application
6. **TDD**: Testable architecture with clear boundaries between components

## Benefits

- Reusable base components across features
- Consistent architecture patterns
- Easy testing with clear separation of concerns
- Scalable modular structure
- Type-safe navigation
- Proper state management
- Clean architecture principles