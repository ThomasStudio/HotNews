package com.hotnews.ui.pages.favorites

/**
 * Created by thomas on 3/4/2025.
 */

import androidx.lifecycle.viewModelScope
import com.hotnews.database.DBResult
import com.hotnews.database.entity.Favorites
import com.hotnews.repository.FavoritesRepository
import com.hotnews.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(private val repository: FavoritesRepository) :
    BaseViewModel<FavoritesViewModel.Event, FavoritesViewModel.State>(State.Loading) {

    init {
        getFavorites()
    }

    private fun getFavorites() = viewModelScope.launch {
        repository.allBooks.firstOrNull()?.let {
            change(State.Data(it))
        }
    }

    sealed class Event {
        data object Back : Event()
    }

    sealed class State {
        data object Loading : State()
        data class Data(val data: DBResult<List<Favorites>>) : State()
    }
}