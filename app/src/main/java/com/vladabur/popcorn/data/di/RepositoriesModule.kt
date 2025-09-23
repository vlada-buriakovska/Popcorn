package com.vladabur.popcorn.data.di

import com.vladabur.popcorn.data.repositories.MovieRepositoryImpl
import com.vladabur.popcorn.domain.repositories.MovieRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoriesModule {
    
    @Binds
    fun bindMovieRepository(
        movieRepositoryImpl: MovieRepositoryImpl
    ): MovieRepository
}