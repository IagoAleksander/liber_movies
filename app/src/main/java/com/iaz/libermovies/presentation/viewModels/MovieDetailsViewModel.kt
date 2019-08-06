package com.iaz.libermovies.presentation.viewModels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.iaz.libermovies.database.AppDatabase
import com.iaz.libermovies.models.MovieModel

class MovieDetailsViewModel(database: AppDatabase, mMovieId: String) : ViewModel() {

    val movieDetails: LiveData<MovieModel> = database.moviesDao().loadMovieById(mMovieId)
}