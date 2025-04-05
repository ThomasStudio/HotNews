package com.hotnews.ui.pages.webview

/**
 * Created by thomas on 3/3/2025.
 */

import androidx.lifecycle.viewModelScope
import com.hotnews.database.entity.Favorites
import com.hotnews.repository.FavoritesRepository
import com.hotnews.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WebViewViewModel @Inject constructor(val repository: FavoritesRepository) :
    BaseViewModel<WebViewViewModel.Event, WebViewViewModel.State>(State.Loading) {

    fun addToFavorites(url: String, title: String) = viewModelScope.launch {
        repository.insert(
            Favorites(
                title = title,
                url = url,
                addedTime = System.currentTimeMillis()
            )
        )

        send(Event.Toast("added to favorites"))
    }

    sealed class Event {
        data object Back : Event()
        data class Toast(val msg: String) : Event()
    }

    sealed class State {
        data object Loading : State()
    }
}