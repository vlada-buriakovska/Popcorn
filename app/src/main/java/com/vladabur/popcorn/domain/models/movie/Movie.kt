package com.vladabur.popcorn.domain.models.movie

import java.util.Date

data class Movie(
    val id: Int,
    val title: String?,
    val overview: String?,
    val posterPath: String?,
    val releaseDate: Date?,
    val voteAverage: Double?,
    val voteCount: Int?
)