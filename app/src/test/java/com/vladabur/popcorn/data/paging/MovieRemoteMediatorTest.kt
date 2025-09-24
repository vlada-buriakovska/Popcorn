package com.vladabur.popcorn.data.paging

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator.MediatorResult
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.vladabur.popcorn.data.database.AppDatabase
import com.vladabur.popcorn.data.database.dao.MovieDao
import com.vladabur.popcorn.data.database.entities.MovieEntity
import com.vladabur.popcorn.data.service.MockMovieService
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.vladabur.popcorn.data.models.movie.MovieResponse

@OptIn(ExperimentalPagingApi::class)
@RunWith(AndroidJUnit4::class)
@SmallTest
class MovieRemoteMediatorTest {
    private val mockMovies = (1..10).map { id ->
        MovieResponse(
            id = id,
            title = "Movie $id",
            overview = "Overview $id",
            posterPath = "Poster $id path",
            releaseDate = "$id",
            voteAverage = id.toDouble(),
            voteCount = id,
        )
    }

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var appDatabase: AppDatabase
    private lateinit var movieDao: MovieDao

    private lateinit var movieService: MockMovieService

    @Before
    fun setUp() {
        appDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        movieDao = appDatabase.movieDao()
        movieService = MockMovieService()
    }

    @After
    fun tearDown() {
        appDatabase.clearAllTables()
        appDatabase.close()
    }

    @Test
    fun refreshLoadReturnsSuccessResultWhenMoreDataIsPresent() = runTest {
        movieService.mockMovies.addAll(mockMovies)
        val mediator = MovieRemoteMediator(movieService, appDatabase)
        val pagingState = PagingState<Int, MovieEntity>(
            listOf(),
            null,
            PagingConfig(20),
            20
        )
        val result = mediator.load(LoadType.REFRESH, pagingState)
        assertThat(result is MediatorResult.Success && !result.endOfPaginationReached).isTrue()
    }

    @Test
    fun refreshLoadSuccessAndEndOfPaginationWhenNoMoreData() = runTest {
        val mediator = MovieRemoteMediator(movieService, appDatabase)
        val pagingState = PagingState<Int, MovieEntity>(
            listOf(),
            null,
            PagingConfig(20),
            20
        )
        val result = mediator.load(LoadType.REFRESH, pagingState)
        assertThat(result is MediatorResult.Success && result.endOfPaginationReached).isTrue()
    }

    @Test
    fun refreshLoadReturnsErrorResultWhenErrorOccurs() = runTest {
        movieService.shouldThrow = true
        val mediator = MovieRemoteMediator(movieService, appDatabase)
        val pagingState = PagingState<Int, MovieEntity>(
            listOf(),
            null,
            PagingConfig(20),
            20
        )
        val result = mediator.load(LoadType.REFRESH, pagingState)
        assertThat(result is MediatorResult.Error).isTrue()
    }
}