package com.vladabur.popcorn.presentation.feature.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vladabur.popcorn.R
import com.vladabur.popcorn.presentation.common.base.BaseUiState

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
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val tabsList = listOf(
        stringResource(R.string.tab_all),
        stringResource(R.string.tab_favorites)
    )
    TabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clip(RoundedCornerShape(50))
            .padding(1.dp),
        indicator = {
            Box { }
        }
    ) {
        tabsList.forEachIndexed { index, text ->
            val selected = selectedTabIndex == index
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
                onClick = { selectedTabIndex = index },
                text = { Text(text = text, color = contentColor) }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun MainScreenPreview() {
   MainScreen(
       uiState = MainUiState(),
       baseUiState = BaseUiState(),
       onEvent={}
   )
}