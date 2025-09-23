package com.vladabur.popcorn.data.services

import com.vladabur.popcorn.data.models.movie.MovieListResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface MovieService {

    @GET("discover/movie?sort_by=primary_release_date.desc")
    suspend fun getMovies(
        @Query("page")
        page: Int
    ): MovieListResponse
}