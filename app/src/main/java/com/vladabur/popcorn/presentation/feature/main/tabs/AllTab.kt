package com.vladabur.popcorn.presentation.feature.main.tabs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.paging.compose.collectAsLazyPagingItems
import com.vladabur.popcorn.presentation.common.ui.components.ConnectionError
import com.vladabur.popcorn.presentation.common.ui.components.MoviesLazyPagingList
import com.vladabur.popcorn.presentation.extensions.hasConnectionError
import com.vladabur.popcorn.presentation.extensions.share
import com.vladabur.popcorn.presentation.feature.main.MainUiEvent
import com.vladabur.popcorn.presentation.feature.main.MainUiState

@Composable
fun AllTab(
    listState: LazyListState = rememberLazyListState(),
    uiState: MainUiState,
    onEvent: (MainUiEvent) -> Unit,
) {
    val movieListItems = uiState.movies.collectAsLazyPagingItems()
    val context = LocalContext.current
    AnimatedVisibility(
        movieListItems.hasConnectionError()
    ) {
        ConnectionError(
            onRetry = {
                movieListItems.retry()
            }
        )
    }
    MoviesLazyPagingList(
        movieListItems = movieListItems,
        listState = listState,
        onFavouriteClicked = { movie ->
            if (movie.isFavorite) {
                onEvent.invoke(MainUiEvent.RemoveFromFavorite(movie = movie))
            } else {
                onEvent.invoke(MainUiEvent.AddToFavorite(movie = movie))
            }
        },
        onShareClicked = {
            it.share(context)
        }
    )
}