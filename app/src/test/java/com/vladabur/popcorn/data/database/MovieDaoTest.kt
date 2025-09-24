package com.vladabur.popcorn.data.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingSource.LoadParams.Refresh
import androidx.paging.PagingSource.LoadResult.Page
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.vladabur.popcorn.data.database.dao.MovieDao
import com.vladabur.popcorn.data.database.entities.MovieEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class MovieDaoTest {
    private val mockMovies = (1..10).map { id ->
        MovieEntity(
            localId = id,
            id = id,
            title = "Movie $id",
            overview = "Overview $id",
            posterPath = "Poster $id path",
            releaseDate = "$id",
            voteAverage = id.toDouble(),
            voteCount = id,
            isFavorite = id % 2 == 0
        )
    }

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var appDatabase: AppDatabase
    private lateinit var movieDao: MovieDao

    @Before
    fun setUp() {
        appDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        movieDao = appDatabase.movieDao()
    }

    @After
    fun tearDown() {
        appDatabase.clearAllTables()
        appDatabase.close()
    }

    @Test
    fun upsertAll_insertsMovies() = runTest {
        movieDao.upsertAll(mockMovies)

        val loaded1 = movieDao.getMovieById(1)
        val loaded2 = movieDao.getMovieById(2)
        assertThat(loaded1?.id).isEqualTo(1)
        assertThat(loaded2?.id).isEqualTo(2)
    }

    @Test
    fun upsertAll_overwritesOnConflict() = runTest {
        movieDao.upsertAll(mockMovies)
        movieDao.upsertAll(mockMovies.map { movieEntity ->
            if (movieEntity.id == 1)
                movieEntity.copy(
                    title = "New Movie 1"
                )
            else movieEntity
        }
        )
        val loaded = movieDao.getMovieById(1)
        assertThat(loaded?.title).isEqualTo("New Movie 1")
    }

    @Test
    fun getMovieById_returnsNullWhenMissing() = runTest {
        val loaded = movieDao.getMovieById(999)
        assertThat(loaded).isNull()
    }

    @Test
    fun updateMovie_updatesFields() = runTest {
        val movie = mockMovies[0]
        movieDao.upsertAll(listOf(movie))

        val updated = movie.copy(isFavorite = false)
        movieDao.updateMovie(updated)

        val loaded = movieDao.getMovieById(1)
        assertThat(loaded?.isFavorite).isFalse()
    }

    @Test
    fun clearAll_deletesAllRows() = runTest {
        movieDao.upsertAll(mockMovies)
        movieDao.clearAll()

        assertThat(movieDao.getMovieById(1)).isNull()
    }
    
    @Test
    fun pagingSource_emitsAllMovies() = runTest {
        movieDao.upsertAll(mockMovies)

        val pagingSource = movieDao.pagingSource()
        val result = pagingSource.load(
            Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assertThat((result as Page).data).isEqualTo(mockMovies)
    }

    @Test
    fun favoritePagingSource_emitsOnlyFavorites() = runTest {
        movieDao.upsertAll(mockMovies)

        val pagingSource = movieDao.favoritePagingSource()
        val result = pagingSource.load(
            Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )
        assertThat((result as Page).data).isEqualTo(mockMovies.filter { it.id % 2 == 0 })
    }
}


