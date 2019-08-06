package com.iaz.libermovies.presentation.viewModels

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.iaz.libermovies.database.AppDatabase
import com.iaz.libermovies.models.MovieModel

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val popular: LiveData<List<MovieModel>>
    val topRated: LiveData<List<MovieModel>>
    val upcoming: LiveData<List<MovieModel>>
    val favorites: LiveData<List<MovieModel>>

    init {

        val database = AppDatabase.getInstance(this.getApplication())

        popular = database.moviesDao().loadPopularMovies()
        topRated = database.moviesDao().loadTopRatedMovies()
        upcoming = database.moviesDao().loadUpcomingMovies()
        favorites = database.moviesDao().loadFavoritedMovies()
    }
}