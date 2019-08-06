package com.iaz.libermovies.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.iaz.libermovies.Constants.FAVORITED_MOVIES
import com.iaz.libermovies.Constants.POPULAR_MOVIES
import com.iaz.libermovies.Constants.TOP_RATED_MOVIES
import com.iaz.libermovies.Constants.UPCOMING_MOVIES
import com.iaz.libermovies.models.MovieModel

@Dao
abstract class MoviesDao {

    private fun upsert(movieLocal: MovieModel, movieListType: Int) {
        try {
            insertMovie(movieLocal)

        } catch (e: Exception) {
            updateMovie(movieLocal.id, movieLocal.voteAverage, movieLocal.title, movieLocal.overview, movieLocal.releaseDate, movieLocal.posterPath)
        }

        setMovieListType(movieLocal.id, movieListType)
    }

    @Query("SELECT * FROM movie ORDER BY releaseDate ASC")
    abstract fun loadAllMovies(): LiveData<List<MovieModel>>

    @Query("SELECT * FROM movie WHERE movie.id = :id ORDER BY releaseDate ASC")
    abstract fun loadMovie(id: String): MovieModel

    @Query("SELECT * FROM movie WHERE isPopular = 1")
    abstract fun loadPopularMovies(): LiveData<List<MovieModel>>

    @Query("SELECT * FROM movie WHERE isTopRated = 1")
    abstract fun loadTopRatedMovies(): LiveData<List<MovieModel>>

    @Query("SELECT * FROM movie WHERE isUpcoming = 1")
    abstract fun loadUpcomingMovies(): LiveData<List<MovieModel>>

    @Query("SELECT * FROM movie WHERE isFavorite = 1")
    abstract fun loadFavoritedMovies(): LiveData<List<MovieModel>>

    @Insert(onConflict = OnConflictStrategy.FAIL)
    abstract fun insertMovie(movieLocal: MovieModel)

    @Query("UPDATE movie SET " +
            "voteAverage = :voteAverage, title = :title, overview= :overview, releaseDate=:releaseDate, " +
            "posterPath=:posterPath " +
            "WHERE id = :id")
    abstract fun updateMovie(id: String, voteAverage: String?, title: String?, overview: String?, releaseDate: String?, posterPath: String?)

    @Delete
    abstract fun deleteMovie(movieLocal: MovieModel)

    @Query("SELECT * FROM movie WHERE id = :mMovieId")
    abstract fun loadMovieById(mMovieId: String): LiveData<MovieModel>

    fun insertMovieLocalList(movies: ArrayList<MovieModel>, movieListType: Int) {
        if (movies.isNotEmpty()) {
            for (movie in movies) {
                upsert(movie, movieListType)
            }
        }
    }

    private fun setMovieListType(movieId: String, movieListType: Int) {
        when (movieListType) {
            POPULAR_MOVIES -> setPopular(movieId)
            TOP_RATED_MOVIES -> setTopRated(movieId)
            UPCOMING_MOVIES -> setUpcoming(movieId)
            FAVORITED_MOVIES -> setFavorite(movieId)
        }
    }

    @Query("UPDATE movie " +
            "SET isPopular = 1 " +
            "WHERE id=:movieId")
    abstract fun setPopular(movieId: String)

    @Query("UPDATE movie " +
            "SET isTopRated = 1 " +
            "WHERE id=:movieId")
    abstract fun setTopRated(movieId: String)

    @Query("UPDATE movie " +
            "SET isUpcoming = 1 " +
            "WHERE id=:movieId")
    abstract fun setUpcoming(movieId: String)

    @Query("UPDATE movie " +
            "SET isFavorite = 1 " +
            "WHERE id=:movieId")
    abstract fun setFavorite(movieId: String)

    @Query("UPDATE movie " +
            "SET isFavorite = 0 " +
            "WHERE id=:movieId")
    abstract fun unfavorite(movieId: String)

    @Query("UPDATE movie " +
            "SET videoPath = :videoPath " +
            "WHERE id=:movieId")
    abstract fun setVideoPath(movieId: String, videoPath: String)

}