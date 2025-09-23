package com.vladabur.popcorn.data.mappers

import com.vladabur.popcorn.data.models.movie.MovieResponse
import com.vladabur.popcorn.domain.models.movie.Movie

fun MovieResponse.toMovie(): Movie {
    return Movie(
        id = this.id,
        title = this.title,
        overview = this.overview,
        posterPath = this.posterPath,
        releaseDate = this.releaseDate?.toDate(),
        voteAverage = this.voteAverage,
        voteCount = this.voteCount
    )
} 