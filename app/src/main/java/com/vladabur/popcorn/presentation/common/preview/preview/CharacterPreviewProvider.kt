package com.vladabur.popcorn.presentation.common.preview.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.vladabur.popcorn.domain.models.movie.Movie
import java.util.Date

class MoviePreviewProvider : PreviewParameterProvider<Movie> {
    override val values: Sequence<Movie>
        get() = sequenceOf(
            Movie(
                localId = 1,
                id = 216527,
                title = "Avatar 4",
                overview = "The fourth installment of the Avatar franchise.",
                posterPath = "/qzMYKnT4MG1d0gnhwytr4cKhUvS.jpg",
                releaseDate = Date(),
                voteAverage = 4.1,
                voteCount = 500,
                isFavorite = true,
            ),
        )
}