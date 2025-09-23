package com.vladabur.popcorn.presentation.feature.main.tabs

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.paging.compose.collectAsLazyPagingItems
import com.vladabur.popcorn.presentation.common.ui.components.MoviesLazyPagingList
import com.vladabur.popcorn.presentation.feature.main.MainUiEvent
import com.vladabur.popcorn.presentation.feature.main.MainUiState

@Composable
fun FavoriteTab(
    listState: LazyListState = rememberLazyListState(),
    uiState: MainUiState,
    onEvent: (MainUiEvent) -> Unit,
) {
    val movieListItems = uiState.favoriteMovies.collectAsLazyPagingItems()
    MoviesLazyPagingList(
        movieListItems = movieListItems, listState = listState,
        onFavouriteClicked = {movie->
            onEvent.invoke(MainUiEvent.RemoveFromFavorite(movie = movie))
        }
    )
}