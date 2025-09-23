package com.vladabur.popcorn.data.models.error

import com.google.gson.annotations.SerializedName

open class ApiErrorResponse(
    @SerializedName("status_message")
    val error: String? = null,
)
