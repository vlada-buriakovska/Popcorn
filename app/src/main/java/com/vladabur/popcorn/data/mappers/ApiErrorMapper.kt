package com.vladabur.popcorn.data.mappers

import com.vladabur.popcorn.data.models.error.ApiErrorResponse
import com.vladabur.popcorn.domain.models.errors.ApiError

fun ApiErrorResponse.toApiError(): ApiError {
    return ApiError(
        error = this.error
    )
} 