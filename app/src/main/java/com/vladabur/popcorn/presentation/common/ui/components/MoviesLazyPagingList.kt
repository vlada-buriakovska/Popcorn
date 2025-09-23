package com.vladabur.popcorn.presentation.common.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState.Loading
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import com.vladabur.popcorn.BuildConfig
import com.vladabur.popcorn.R
import com.vladabur.popcorn.domain.models.movie.Movie
import com.vladabur.popcorn.presentation.common.preview.preview.MoviePreviewProvider
import com.vladabur.popcorn.presentation.common.ui.theme.AppTypography
import com.vladabur.popcorn.presentation.extensions.isSameMonthAndYear
import com.vladabur.popcorn.presentation.extensions.shimmerEffect
import com.vladabur.popcorn.presentation.extensions.toYearAndMonth
import java.util.Date


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MoviesLazyPagingList(
    movieListItems: LazyPagingItems<Movie>,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    onFavouriteClicked: (Movie) -> Unit,
    onShareClicked: (Movie) -> Unit
) {
    val isLoading =
        movieListItems.loadState.refresh is Loading && movieListItems.itemCount == 0
    val isRefreshing = movieListItems.loadState.refresh is Loading && !isLoading
    val isAppending = movieListItems.loadState.append is Loading
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing, onRefresh = {
            movieListItems.refresh()
        }
    )
    Box(
        modifier = modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
        ) {
            if (isLoading) {
                items(5) {
                    DateListItemPlaceholder()
                    MovieListItemPlaceholder()
                }
            } else {
                items(
                    count = movieListItems.itemCount,
                ) { index ->
                    val item = movieListItems[index]

                    item?.let { movie ->
                        if (index == 0 ||
                            !item.releaseDate.isSameMonthAndYear(
                                movieListItems[index - 1]?.releaseDate
                            )
                        ) {
                            item.releaseDate?.let { DateListItem(it) }
                        }
                        MovieListItem(
                            movie = movie,
                            onFavouriteClicked = onFavouriteClicked,
                            onShareClicked = onShareClicked
                        )
                    }
                }

                if (isAppending) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
        PullRefreshIndicator(
            modifier = Modifier.align(Alignment.TopCenter),
            refreshing = isRefreshing,
            state = pullRefreshState,
            backgroundColor = White,
            contentColor = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun DateListItem(date: Date) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        style = AppTypography.titleSmall,
        text = date.toYearAndMonth()
    )

}

@Composable
private fun MovieListItem(
    movie: Movie,
    onFavouriteClicked: (Movie) -> Unit,
    onShareClicked: (Movie) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    ElevatedCard(
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .clickable {
                isExpanded = !isExpanded
            }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd
            ) {
                if (movie.posterPath.isNullOrEmpty()) {
                    Icon(
                        modifier = Modifier
                            .aspectRatio(4 / 3F)
                            .align(Alignment.Center)
                            .scale(0.5f),
                        imageVector = Icons.Default.Close,
                        contentDescription = movie.title,
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                } else {
                    AsyncImage(
                        modifier = Modifier.aspectRatio(4 / 3F),
                        model = BuildConfig.BASE_IMAGE_URL + movie.posterPath,
                        contentDescription = movie.title,
                        contentScale = ContentScale.Crop,
                    )
                }
                Column {
                    val favoriteIcon = if (movie.isFavorite) {
                        Icons.Default.Favorite

                    } else {
                        Icons.Default.FavoriteBorder
                    }
                    val favoriteIconTint = if (movie.isFavorite) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }

                    IconButton(
                        modifier = Modifier.padding(4.dp),
                        colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.surface),
                        onClick = {
                            onFavouriteClicked(movie)
                        }
                    ) {
                        Icon(
                            favoriteIcon,
                            contentDescription = null,
                            tint = favoriteIconTint
                        )
                    }
                    IconButton(
                        modifier = Modifier.padding(4.dp),
                        colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.surface),
                        onClick = {
                            onShareClicked(movie)
                        }) {
                        Icon(
                            Icons.Default.Share,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
            if (!movie.title.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                        style = AppTypography.titleMedium,
                        text = movie.title
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        movie.voteAverage?.let { voteAverage ->
                            Icon(
                                modifier = Modifier.size(24.dp),
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                modifier = Modifier.padding(end = 2.dp),
                                style = AppTypography.titleSmall,
                                text = stringResource(
                                    R.string.average_vote_formatter,
                                    voteAverage
                                )
                            )
                            movie.voteCount?.let { voteCount ->
                                Text(
                                    modifier = Modifier.padding(end = 8.dp),
                                    style = AppTypography.titleSmall.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    ),
                                    text = stringResource(
                                        R.string.vote_count_formatter,
                                        voteCount
                                    )
                                )
                            }
                        }
                    }
                }
            }
            if (!movie.overview.isNullOrEmpty()) {
                val maxLines = if (isExpanded) {
                    Int.MAX_VALUE
                } else {
                    3
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1F)
                            .padding(horizontal = 8.dp),
                        style = AppTypography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        text = movie.overview,
                        maxLines = maxLines,
                        overflow = TextOverflow.Ellipsis
                    )
                    val expandIcon = if (isExpanded) {
                        Icons.Default.KeyboardArrowUp
                    } else {
                        Icons.Default.KeyboardArrowDown
                    }
                    Icon(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .size(24.dp),
                        imageVector = expandIcon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun DateListItemPlaceholder() {
    Box(
        modifier = Modifier
            .padding(12.dp)
            .width(80.dp)
            .height(24.dp)
            .clip(shape = RoundedCornerShape(8.dp))
            .shimmerEffect()
    )
}

@Composable
private fun MovieListItemPlaceholder() {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            //Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(4 / 3F)
                    .shimmerEffect(),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                //Title
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp)
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                        .clip(shape = RoundedCornerShape(8.dp))
                        .shimmerEffect(),
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    //Star
                    Box(
                        modifier = Modifier
                            .width(24.dp)
                            .height(24.dp)
                            .clip(shape = RoundedCornerShape(8.dp))
                            .shimmerEffect(),
                    )

                    Spacer(modifier = Modifier.width(4.dp))
                    //Rating
                    Box(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .width(40.dp)
                            .height(24.dp)
                            .clip(shape = RoundedCornerShape(8.dp))
                            .shimmerEffect(),
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(4.dp))

        //Description
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .clip(shape = RoundedCornerShape(8.dp))
                .shimmerEffect(),
            style = AppTypography.bodyMedium,
            text = "\n\n\n",
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun DateListItemPreview(
) {
    DateListItem(date = Date())
}

@Preview(showBackground = true)
@Composable
private fun MovieListItemPreview(
    @PreviewParameter(MoviePreviewProvider::class, limit = 1)
    movie: Movie
) {
    MovieListItem(
        movie = movie,
        onFavouriteClicked = {},
        onShareClicked = {}
    )
}