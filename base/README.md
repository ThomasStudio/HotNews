# Base Library Module

## High level design
* MVVM, coroutine, compose UI, NavHost, Hilt, TDD

### ViewModel
* ViewModel should be UI state holder, not business logic holder
* ViewModel should not know about the View, it should only expose data and events to the View
* ViewModel should handle business logic and data manipulation, and expose the result to the View through uiState and events
* ViewModel should handle error and expose error state to the View
* ViewModel should be testable, and should not have any Android framework dependencies
* ViewModel should be reusable, and should not have any specific UI logic
* ViewModel should be single source of truth for the UI state, and should not have any side effects
* ViewModel should be lifecycle aware, and should not have any memory leaks
* uiState: StateFlow
    Status: Loading, Success, Error
        Loading: show loading indicator
        Success: show data
        Error: show error message
* event: SharedFlow


## View (composable function):
* Observe uiState from ViewModel and update UI accordingly
* Observe events from ViewModel and handle them accordingly
* Handle user interactions and call function of ViewModel
* Should not have any business logic, and should only be responsible for displaying data and handling user interactions
* Should use NavHost for navigation, and should not have any specific navigation logic
 
## Detail design
### BaseContract
* BaseContract should be an interface that defines the contract between the View and the ViewModel
* BaseContract should be extended by any specific contract class that represents the contract of a specific ViewModel
* BaseContract has uiState and event properties that represent the state and events of the ViewModel
* BaseContract has back() function that represents the back navigation action

### BaseState
* BaseState should be a base class that represents the state of the UI
* BaseState should be extended by any specific state class that represents the state of a specific UI, and should be used as the type of uiState in the ViewModel
* BaseState should have a status property that represents the current status of the UI (Loading, Success, Error)
#### Status
* Status should be a sealed class that represents the current status of the UI (Loading, Success, Error)
* Success status should have a BaseData property that represents the data to be displayed in the UI
* Error status should have an BaseError property that represents the error message to be displayed in the UI
* Status should be used in the BaseState to represent the current status of the UI, and should be used by the View to update the UI accordingly
* Status should be used by the ViewModel to update the uiState with the current status of the UI, and should be used to represent the loading, success, and error states of the UI in a consistent way across the application
#### BaseData
* BaseData should be a base class that represents the data to be displayed in the UI
* BaseData should be extended by any specific data class that represents the data of a specific UI, and should be used as the type of data in the BaseState
#### BaseError
* BaseError should be a base class that represents the error message to be displayed in the UI
* BaseError should have a message property that represents the error message to be displayed in the UI
* BaseError should be extended by any specific error class that represents the error of a specific UI

### BaseEvent:
* BaseEvent should be a base interface that represents the events that can be emitted by the ViewModel
* BaseEvent should be extended by any specific event class that represents the events of a specific ViewModel, and should be used as the type of event in the ViewModel
* BaseEvent should be used by the View to observe events from the ViewModel, and handle them accordingly
* BaseEvent should be used by the ViewModel to emit events to the View, and should not have any specific logic or data associated with it, as it is only a signal for the View to perform a certain action (e.g. navigate to another screen, show a toast message, etc.)
* BaseEvent should be used to represent one-time events that should not be persisted in the uiState, and should not have any side effects on the UI state, as they are only meant to trigger a specific action in the View
* BaseEvent should have navigation event, back event, message event 

### BaseViewModel:
* BaseViewModel should be a base class that extends ViewModel and implements BaseContract
* BaseViewModel should have a uiState property that is a StateFlow of the type of BaseState, and should be initialized with a default state
* BaseViewModel should have an event property that is a SharedFlow of the type of BaseEvent, and should be initialized with a MutableSharedFlow
* BaseViewModel should have a function to update the uiState, and should emit the new state to the uiState StateFlow
* BaseViewModel should have a function to emit events, and should emit the event to the event SharedFlow
* BaseViewModel should be extended by any specific ViewModel that represents the logic of a specific UI, and should be used as the base class for any ViewModel that wants to expose uiState and events to the View
* BaseViewModel should handle the common logic of updating the uiState and emitting events, and should not have any specific logic or data associated with it, as it is only meant to provide a common structure for the ViewModel classes in the application

### AppRoute
* AppRoute should be an data class that generate route string
* AppRoute should have a function to generate route string by a list of path
* AppRoute should have a function to generate route string with parameters
* Concrete route class should implement AppRoute and provide the path and parameters for the route

### Navigator
* Navigator should be an interface that defines the navigation actions in the application
* Navigator.navigate() should be a function that takes a route string and performs the navigation action
* Navigator.back() should be a function that performs the back navigation action
* NavHostController should be used to perform the actual navigation, and should be passed to the Navigator implementation
* NavHostController.asNavigator() should be an extension function that converts a NavHostController to a Navigator, and should be used to create an instance of the Navigator implementation in the ViewModel
