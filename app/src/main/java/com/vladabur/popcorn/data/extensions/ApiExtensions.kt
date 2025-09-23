package com.vladabur.popcorn.data.extensions

import com.google.gson.Gson
import com.vladabur.popcorn.data.mappers.toApiError
import com.vladabur.popcorn.data.models.error.ApiErrorResponse
import com.vladabur.popcorn.domain.models.errors.ApiError
import com.vladabur.popcorn.domain.models.exceptions.ApiErrorException
import com.vladabur.popcorn.domain.models.exceptions.ConnectionErrorException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

fun Throwable.mapToApiErrors(): Throwable {
    when (this) {
        is HttpException -> {
            return if (this.code() == 500) {
                ApiErrorException(apiError = ApiError(error = "Woops! Something happened with server"))
            } else {
                val errorResponse =
                    Gson().fromJson(
                        this.response()?.errorBody()?.string(),
                        ApiErrorResponse::class.java
                    )
                ApiErrorException(errorResponse.toApiError())
            }
        }

        is UnknownHostException,
        is SocketTimeoutException,
        is ConnectException,
        is TimeoutException -> return ConnectionErrorException()

        else -> return this
    }
}
