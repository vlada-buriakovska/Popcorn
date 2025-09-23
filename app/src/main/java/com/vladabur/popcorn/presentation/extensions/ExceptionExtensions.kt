package com.vladabur.popcorn.presentation.extensions

import com.vladabur.popcorn.data.extensions.mapToApiErrors
import com.vladabur.popcorn.domain.models.exceptions.ApiErrorException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException


fun Throwable.isConnectionError(): Boolean {
    return when (this) {
        is UnknownHostException,
        is SocketTimeoutException,
        is ConnectException,
        is TimeoutException -> true

        else -> false
    }
}

fun Throwable.getMessage(): String? {
    return if (!isConnectionError()) {
        if (this is HttpException) {
            val apiErrorException = this.mapToApiErrors() as ApiErrorException
            apiErrorException.apiError?.error
        } else {
            this.localizedMessage
        }
    } else {
        null
    }
}