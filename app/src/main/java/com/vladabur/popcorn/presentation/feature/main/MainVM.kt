package com.vladabur.popcorn.presentation.feature.main

import com.vladabur.popcorn.presentation.common.base.BaseViewModel
import com.vladabur.popcorn.presentation.feature.main.MainUiEvent.Consume
import com.vladabur.popcorn.presentation.feature.main.MainUiEvent.Retry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class MainVM @Inject constructor() : BaseViewModel<MainUiEvent>() {
    
    private val managerUiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = managerUiState.asStateFlow()

    override fun onEvent(event: MainUiEvent) {
        when (event) {
            Consume -> consumeError()
            Retry -> retry()
        }
    }
}

sealed class MainUiEvent {
    data object Retry : MainUiEvent()
    data object Consume : MainUiEvent()
}

class MainUiState