package com.vladabur.popcorn.presentation.feature.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.vladabur.popcorn.R
import com.vladabur.popcorn.presentation.common.base.BaseUiState
import com.vladabur.popcorn.presentation.common.ui.components.ConnectionError
import com.vladabur.popcorn.presentation.common.ui.components.ErrorSnackBar
import com.vladabur.popcorn.presentation.extensions.getMessage
import com.vladabur.popcorn.presentation.extensions.hasConnectionError
import com.vladabur.popcorn.presentation.feature.main.tabs.AllTab
import kotlin.Int
import kotlin.String
import kotlin.Unit

enum class MainTabs(val nameResource: Int) {
    ALL(R.string.tab_all),
    FAVORITE(R.string.tab_favorites)

}

@Composable
fun MainRoute(
    viewModel: MainVM = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val baseState = viewModel.baseUiState.collectAsStateWithLifecycle()
    MainScreen(
        uiState = uiState.value,
        baseUiState = baseState.value,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun MainScreen(
    uiState: MainUiState,
    baseUiState: BaseUiState,
    onEvent: (MainUiEvent) -> Unit,
) {
    var selectedTab by remember { mutableStateOf(MainTabs.ALL) }
    val listState = rememberLazyListState()
    val movieListItems = uiState.movies.collectAsLazyPagingItems()

    Column {
        AnimatedVisibility(baseUiState.isConnectionError == true || movieListItems.hasConnectionError()) {
            ConnectionError(
                onRetry = {
                    onEvent(MainUiEvent.Retry)
                    movieListItems.retry()
                }
            )
        }
        TabRow(
            selectedTabIndex = MainTabs.entries.indexOf(selectedTab),
            modifier = Modifier
                .padding(vertical = 4.dp, horizontal = 8.dp)
                .clip(RoundedCornerShape(50))
                .padding(1.dp),
            indicator = {
                Box { }
            }
        ) {
            MainTabs.entries.forEach { tab ->
                val selected = selectedTab == tab
                val backgroundColor = if (selected)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.surface
                val contentColor = if (selected)
                    MaterialTheme.colorScheme.onPrimary
                else
                    MaterialTheme.colorScheme.onSurface

                Tab(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(
                            backgroundColor
                        ),
                    selected = selected,
                    onClick = { selectedTab = tab },
                    text = { Text(text = stringResource(tab.nameResource), color = contentColor) }
                )
            }
        }
        when (selectedTab) {
            MainTabs.ALL -> {
                AllTab(
                    uiState = uiState,
                    listState = listState
                )
            }

            MainTabs.FAVORITE -> {
                //TODO
            }
        }
    }
    val errorMessage = when {
        movieListItems.loadState.refresh is LoadState.Error -> {
            val e = movieListItems.loadState.refresh as LoadState.Error
            e.error.getMessage()
        }

        movieListItems.loadState.append is LoadState.Error -> {
            val e = movieListItems.loadState.append as LoadState.Error
            e.error.getMessage()
        }

        else -> {
            null
        }
    }
    if (baseUiState.error != null || errorMessage != null) {
        ErrorSnackBar(
            error = baseUiState.error
                ?: errorMessage ?: String(),
            onDismissed = {
                onEvent(MainUiEvent.Consume)
            }
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun MainScreenPreview() {
    MainScreen(
        uiState = MainUiState(),
        baseUiState = BaseUiState(),
        onEvent = {}
    )
}