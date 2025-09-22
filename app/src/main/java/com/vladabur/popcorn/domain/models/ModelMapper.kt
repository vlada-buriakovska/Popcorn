package com.vladabur.popcorn.domain.models

fun interface ModelMapper<in FROM, out INTO> {
    fun map(model: FROM): INTO
}