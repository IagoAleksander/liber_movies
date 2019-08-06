package com.iaz.libermovies

object Constants {

    const val POSTER_IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185/"

    const val POPULAR_MOVIES = 1
    const val TOP_RATED_MOVIES = 2
    const val UPCOMING_MOVIES = 3
    const val FAVORITED_MOVIES = 4

    const val MOVIE_ID = "movie_id"
    const val MOVIE_BUNDLE = "movie_bundle"
    const val SORTING_ORDER = "sorting_order"
    const val LIST_STATE = "list_state"
    const val IS_SEARCH = "is_search"

    //    24 Hours = 86,400,000 Milliseconds
    const val updateTime: Long = 86400000
}
