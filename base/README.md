# Base Library Module

The base library module contains common utilities, base classes, and shared functionality that can be reused across different modules in the HotNews application.

## Android design

1. MVVM, coroutine, compose UI, NavHost, Hilt, TDD
2. View: use compose UI for View, use NavHost for navigation
3. BaseViewModel:

   1. use StateFlow as uiState, fun updateState to update uiState and view will change accordingly
   2. use SharedFlow as event, fun send to send an event to view
   3. Try to do most of the work in ViewModel, not in View. Make it more reusable and TDD.
   4. navigation flow:

      1. View call navigate function of ViewModel
      2. ViewModel send route event with route string
      3. View receives event and then use navigator to navigate with route string
4. Router: It is base class to generate route string, it is used by ViewModel to generate route string.
5. Navigator: It is base class to navigate with route string, it will use NavHostController to navigate with route string.
6. Each feature module will have its own ViewModel, UI State, UI Events, and Router
