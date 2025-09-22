package com.vladabur.popcorn.presentation.feature.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
    
}


@Preview(showSystemUi = true)
@Composable
private fun MainScreenPreview() {
   MainScreen(
       uiState = MainUiState(),
       baseUiState = BaseUiState(),
       onEvent={}
   )
}