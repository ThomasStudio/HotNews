package com.hotnews.ui.pages.favorites

import com.hotnews.MainDispatcherRule
import com.hotnews.database.DBError
import com.hotnews.database.DBException
import com.hotnews.database.DBResult
import com.hotnews.database.entity.Favorites
import com.hotnews.repository.FavoritesRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

@OptIn(ExperimentalCoroutinesApi::class)
class FavoritesViewModelTest2 {

    private fun createTestFavorite(id: Long = 0) = Favorites(
        id = id,
        title = "Test $id",
        url = "https://test.com/$id",
        addedTime = System.currentTimeMillis()
    )

    @JvmField
    @RegisterExtension
    val mainDispatcherRule = MainDispatcherRule()

    private val mockRepository: FavoritesRepository = mockk()
    private lateinit var viewModel: FavoritesViewModel

    @BeforeEach
    fun setup() {
        viewModel = FavoritesViewModel(mockRepository)
    }

    @Test
    fun `handle database errors`() = runTest {
        val error = DBException(DBError.CONNECTION_LOST, "DB locked")
        coEvery { mockRepository.allBooks } returns flow {
            emit(DBResult.Error(error))
        }

        viewModel.getFavorites()
        advanceUntilIdle()

        val result = (viewModel.uiState.value as FavoritesViewModel.State.Data).data
        assertTrue(result is DBResult.Error)
        assertEquals(error, (result as DBResult.Error).exception)
    }

}