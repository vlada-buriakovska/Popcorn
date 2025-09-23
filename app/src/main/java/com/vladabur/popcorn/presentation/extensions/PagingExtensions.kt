package com.vladabur.popcorn.presentation.extensions

import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems

fun <T : Any> LazyPagingItems<T>.hasConnectionError(): Boolean {
    return loadState.hasError && (loadState.refresh as LoadState.Error).error.isConnectionError()
}
fun <T : Any> LazyPagingItems<T>.error(): Throwable {
    return (loadState.refresh as LoadState.Error).error
}