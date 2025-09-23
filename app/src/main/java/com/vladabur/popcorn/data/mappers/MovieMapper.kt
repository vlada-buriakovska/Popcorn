package com.vladabur.popcorn.data.mappers

import com.vladabur.popcorn.data.database.entities.MovieEntity
import com.vladabur.popcorn.data.models.movie.MovieResponse
import com.vladabur.popcorn.domain.models.movie.Movie

fun MovieResponse.toMovieEntity(): MovieEntity {
    return MovieEntity(
        id = this.id,
        title = this.title,
        overview = this.overview,
        posterPath = this.posterPath,
        releaseDate = this.releaseDate,
        voteAverage = this.voteAverage,
        voteCount = this.voteCount,
        isFavorite = false
    )
}

fun MovieEntity.toMovie(): Movie {
    return Movie(
        localId = this.localId,
        id = this.id,
        title = this.title,
        overview = this.overview,
        posterPath = this.posterPath,
        releaseDate = this.releaseDate?.toDate(),
        voteAverage = this.voteAverage,
        voteCount = this.voteCount,
        isFavorite = this.isFavorite,
    )
} 

fun Movie.toMovieEntity(): MovieEntity {
    return MovieEntity(
        localId = this.localId,
        id = this.id,
        title = this.title,
        overview = this.overview,
        posterPath = this.posterPath,
        releaseDate = this.releaseDate?.toText(),
        voteAverage = this.voteAverage,
        voteCount = this.voteCount,
        isFavorite = this.isFavorite,
    )
} 