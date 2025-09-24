package com.vladabur.popcorn.data.service

import com.vladabur.popcorn.data.models.movie.MovieListResponse
import com.vladabur.popcorn.data.models.movie.MovieResponse
import com.vladabur.popcorn.data.services.MovieService
import java.io.IOException


class MockMovieService: MovieService {

    val mockMovies = mutableListOf<MovieResponse>()
    var shouldThrow: Boolean = false
    
    override suspend fun getMovies(page: Int): MovieListResponse {
        if (shouldThrow) throw IOException()
        return MovieListResponse(page=1, results = mockMovies)
    }
}