package com.vladabur.popcorn.presentation.extensions

import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems

fun <T : Any> LazyPagingItems<T>.hasConnectionError(): Boolean {
    return when {
        loadState.refresh is LoadState.Error -> {
            val e = loadState.refresh as LoadState.Error
            e.error.isConnectionError()
        }

        loadState.append is LoadState.Error -> {
            val e = loadState.append as LoadState.Error
            e.error.isConnectionError()
        }

        else -> {
            false
        }
    }
}

fun <T : Any> LazyPagingItems<T>.errorMessage(): String? {
    return when {
        loadState.refresh is LoadState.Error -> {
            val e = loadState.refresh as LoadState.Error
            e.error.getMessage()
        }

        loadState.append is LoadState.Error -> {
            val e = loadState.append as LoadState.Error
            e.error.getMessage()
        }

        else -> {
            null
        }
    }
}

fun <T : Any> LazyPagingItems<T>.error(): Throwable {
    return (loadState.refresh as LoadState.Error).error
}