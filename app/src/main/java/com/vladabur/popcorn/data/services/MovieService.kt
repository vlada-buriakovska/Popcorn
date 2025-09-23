package com.vladabur.popcorn.data.services

import com.vladabur.popcorn.data.models.movie.MovieListResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface MovieService {

    @GET("discover/movie?sort_by=primary_release_date.desc&vote_average.gte=7&vote_count.gte=100")
    suspend fun getMovies(
        @Query("page")
        page: Int
    ): MovieListResponse
}