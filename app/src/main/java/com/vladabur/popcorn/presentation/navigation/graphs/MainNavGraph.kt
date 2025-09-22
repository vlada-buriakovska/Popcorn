package com.vladabur.popcorn.presentation.navigation.graphs

import kotlinx.serialization.Serializable

@Serializable
sealed class MainNavGraph {
    @Serializable
    data object Main : MainNavGraph()
}