package com.vladabur.popcorn.presentation.feature.main.tabs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.vladabur.popcorn.R
import com.vladabur.popcorn.presentation.common.ui.components.ConnectionError
import com.vladabur.popcorn.presentation.common.ui.components.MoviesLazyPagingList
import com.vladabur.popcorn.presentation.common.ui.theme.AppTypography
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
        },
        emptyListPlaceholder = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier.size(120.dp),
                    painter = painterResource(R.drawable.ic_empty_popcorn),
                    contentDescription = null
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    text = stringResource(R.string.all_empty),
                    style = AppTypography.titleMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    )
}