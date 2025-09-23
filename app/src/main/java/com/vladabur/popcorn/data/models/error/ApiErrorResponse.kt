package com.vladabur.popcorn.data.models.error

import com.google.gson.annotations.SerializedName
import com.vladabur.popcorn.domain.models.errors.ApiError

open class ApiErrorResponse(
    @SerializedName("error")
    val error: String? = null,
)
