package com.iaz.libermovies.presentation.viewModels

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.iaz.libermovies.database.AppDatabase

class MovieDetailsViewModelFactory(private val mDb: AppDatabase, private val mMovieId: String) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MovieDetailsViewModel(mDb, mMovieId) as T
    }
}