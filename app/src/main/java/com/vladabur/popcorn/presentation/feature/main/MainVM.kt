package com.vladabur.popcorn.presentation.feature.main

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.vladabur.popcorn.data.mappers.toMovie
import com.vladabur.popcorn.domain.models.movie.Movie
import com.vladabur.popcorn.domain.usecases.GetMoviesUseCase
import com.vladabur.popcorn.domain.usecases.base.ResultCallbacks
import com.vladabur.popcorn.presentation.common.base.BaseViewModel
import com.vladabur.popcorn.presentation.feature.main.MainUiEvent.Consume
import com.vladabur.popcorn.presentation.feature.main.MainUiEvent.Retry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class MainVM @Inject constructor(
    private val getMoviesUseCase: GetMoviesUseCase
) :
    BaseViewModel<MainUiEvent>() {

    private val managerUiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = managerUiState.asStateFlow()

    init {
        getMovies()
    }

    override fun onEvent(event: MainUiEvent) {
        when (event) {
            Consume -> consumeError()
            Retry -> retry()
        }
    }

    private fun getMovies() {
        getMoviesUseCase(
            coroutineScope = viewModelScope,
            params = null,
            result = ResultCallbacks(
                onSuccess = { result ->
                    val movies = result
                        .map { pagingData ->
                            pagingData
                                .map { it.toMovie() }
                        }
                        .cachedIn(viewModelScope)
                    managerUiState.update {
                        it.copy(movies = movies)
                    }
                },
                onLoading = {
                    handleOnLoading(it)
                },
                onError = {
                    handleOnError(it)
                },
                onConnectionError = {
                    handleOnConnectionError {
                        getMovies()
                    }
                }
            )
        )
    }
}

sealed class MainUiEvent {
    data object Retry : MainUiEvent()
    data object Consume : MainUiEvent()
}

data class MainUiState(
    val movies: Flow<PagingData<Movie>> = emptyFlow()
)