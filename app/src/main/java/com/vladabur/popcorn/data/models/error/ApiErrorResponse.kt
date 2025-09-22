package com.vladabur.popcorn.data.models.error

import com.google.gson.annotations.SerializedName
import com.vladabur.popcorn.domain.models.ModelMapper
import com.vladabur.popcorn.domain.models.errors.ApiError

open class ApiErrorResponse(
    @SerializedName("error")
    val error: String? = null,

    ) {

    companion object : ModelMapper<ApiErrorResponse, ApiError> {
        override fun map(model: ApiErrorResponse): ApiError = ApiError(
            error = model.error
        )
    }
}
