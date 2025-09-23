package com.vladabur.popcorn.presentation.feature.main

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.vladabur.popcorn.data.mappers.toMovie
import com.vladabur.popcorn.domain.models.movie.Movie
import com.vladabur.popcorn.domain.usecases.AddMovieToFavoriteUseCase
import com.vladabur.popcorn.domain.usecases.GetFavoriteMoviesUseCase
import com.vladabur.popcorn.domain.usecases.GetMoviesUseCase
import com.vladabur.popcorn.domain.usecases.RemoveMovieFromFavoriteUseCase
import com.vladabur.popcorn.domain.usecases.base.ResultCallbacks
import com.vladabur.popcorn.presentation.common.base.BaseViewModel
import com.vladabur.popcorn.presentation.feature.main.MainUiEvent.AddToFavorite
import com.vladabur.popcorn.presentation.feature.main.MainUiEvent.Consume
import com.vladabur.popcorn.presentation.feature.main.MainUiEvent.RemoveFromFavorite
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
    private val getMoviesUseCase: GetMoviesUseCase,
    private val getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase,
    private val addMovieToFavoriteUseCase: AddMovieToFavoriteUseCase,
    private val removeMovieFromFavoriteUseCase: RemoveMovieFromFavoriteUseCase
) :
    BaseViewModel<MainUiEvent>() {

    private val managerUiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = managerUiState.asStateFlow()

    init {
        getMovies()
        getFavoriteMovies()
    }

    override fun onEvent(event: MainUiEvent) {
        when (event) {
            Consume -> consumeError()
            Retry -> retry()
            is AddToFavorite -> {
                addMovieToFavorite(event.movie)
            }

            is RemoveFromFavorite -> {
                removeMovieFromFavorite(event.movie)
            }
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

    private fun getFavoriteMovies() {
        getFavoriteMoviesUseCase(
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
                        it.copy(favoriteMovies = movies)
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
                        getFavoriteMovies()
                    }
                }
            )
        )
    }

    private fun addMovieToFavorite(movie: Movie) {
        addMovieToFavoriteUseCase(
            coroutineScope = viewModelScope,
            params = AddMovieToFavoriteUseCase.Params(movie),
            result = ResultCallbacks(
                onLoading = {
                    handleOnLoading(it)
                },
                onError = {
                    handleOnError(it)
                },
                onConnectionError = {
                    handleOnConnectionError {
                        addMovieToFavorite(movie)
                    }
                }
            )
        )
    }

    private fun removeMovieFromFavorite(movie: Movie) {
        removeMovieFromFavoriteUseCase(
            coroutineScope = viewModelScope,
            params = RemoveMovieFromFavoriteUseCase.Params(movie),
            result = ResultCallbacks(
                onLoading = {
                    handleOnLoading(it)
                },
                onError = {
                    handleOnError(it)
                },
                onConnectionError = {
                    handleOnConnectionError {
                        removeMovieFromFavorite(movie)
                    }
                }
            )
        )
    }
}

sealed class MainUiEvent {
    data object Retry : MainUiEvent()
    data object Consume : MainUiEvent()
    data class AddToFavorite(val movie: Movie) : MainUiEvent()
    data class RemoveFromFavorite(val movie: Movie) : MainUiEvent()
}

data class MainUiState(
    val movies: Flow<PagingData<Movie>> = emptyFlow(),
    val favoriteMovies: Flow<PagingData<Movie>> = emptyFlow()
)