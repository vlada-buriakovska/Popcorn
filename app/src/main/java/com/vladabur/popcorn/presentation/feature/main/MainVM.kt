package com.vladabur.popcorn.presentation.feature.main

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import com.vladabur.popcorn.domain.models.movie.Movie
import com.vladabur.popcorn.domain.usecases.GetMoviesUseCase
import com.vladabur.popcorn.domain.usecases.GetMoviesUseCase.Params
import com.vladabur.popcorn.domain.usecases.base.ResultCallbacks
import com.vladabur.popcorn.presentation.common.base.BaseViewModel
import com.vladabur.popcorn.presentation.feature.main.MainUiEvent.Consume
import com.vladabur.popcorn.presentation.feature.main.MainUiEvent.Retry
import com.vladabur.popcorn.presentation.feature.main.MovieListItem.MovieItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import java.util.Calendar
import java.util.Date
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
            params = Params(pageSize = 10),
            result = ResultCallbacks(
                onSuccess = { result ->
                    val movies = result
                        .map { pagingData ->
                            pagingData
                                .map {
                                    MovieItem(it)
                                }
                        }
                        .map { pagingData ->
                            pagingData.insertSeparators { before: MovieItem?, after: MovieItem? ->
                                val beforeMovie = before?.movie
                                val afterMovie = after?.movie
                                if (after == null) {
                                    return@insertSeparators null
                                }
                                if (before == null) {
                                    return@insertSeparators MovieListItem.DateItem(afterMovie?.releaseDate)
                                }
                                if (beforeMovie?.releaseDate == null || afterMovie?.releaseDate == null) {
                                    return@insertSeparators null
                                }
                                val beforeCalendar = Calendar.getInstance()
                                beforeCalendar.time = beforeMovie.releaseDate
                                val afterCalendar = Calendar.getInstance()
                                afterCalendar.time = afterMovie.releaseDate
                                if (beforeCalendar.get(Calendar.YEAR) == afterCalendar.get(Calendar.YEAR) && beforeCalendar.get(
                                        Calendar.MONTH
                                    ) == afterCalendar.get(Calendar.MONTH)
                                ) {
                                    return@insertSeparators null
                                } else {
                                    return@insertSeparators MovieListItem.DateItem(afterMovie.releaseDate)
                                }
                            }
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
    val movies: Flow<PagingData<MovieListItem>> = emptyFlow()
)

sealed interface MovieListItem {
    data class MovieItem(val movie: Movie) : MovieListItem
    data class DateItem(val date: Date?) : MovieListItem
}