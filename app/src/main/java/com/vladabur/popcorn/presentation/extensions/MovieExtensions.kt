package com.vladabur.popcorn.presentation.extensions

import android.content.Context
import android.content.Intent
import com.vladabur.popcorn.BuildConfig
import com.vladabur.popcorn.R
import com.vladabur.popcorn.domain.models.movie.Movie


fun Movie.share(context: Context) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"

        val movieTitle = this@share.title
        val movieDescription = this@share.overview
        val movieLink = BuildConfig.SHARE_URL_PREFIX + this@share.id
        val shareText = "$movieTitle\n$movieDescription\n$movieLink"

        putExtra(Intent.EXTRA_TEXT, shareText)
        putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.share_subject))
    }
    try {
        context.startActivity(shareIntent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}