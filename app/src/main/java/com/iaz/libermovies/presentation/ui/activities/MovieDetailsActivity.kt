package com.iaz.libermovies.presentation.ui.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.ActivityNotFoundException
import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.iaz.libermovies.Constants.IS_SEARCH
import com.iaz.libermovies.Constants.MOVIE_BUNDLE
import com.iaz.libermovies.Constants.MOVIE_ID
import com.iaz.libermovies.Constants.POSTER_IMAGE_BASE_URL
import com.iaz.libermovies.R
import com.iaz.libermovies.database.AppDatabase
import com.iaz.libermovies.databinding.ActivityMovieDetailsBinding
import com.iaz.libermovies.models.*
import com.iaz.libermovies.networkUtils.OMDBApi
import com.iaz.libermovies.networkUtils.TheMovieDBApi
import com.iaz.libermovies.presentation.ui.adapters.ReviewsAdapter
import com.iaz.libermovies.presentation.viewModels.MovieDetailsViewModel
import com.iaz.libermovies.presentation.viewModels.MovieDetailsViewModelFactory
import com.iaz.libermovies.utils.Utilities
import com.squareup.picasso.Picasso
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback

class MovieDetailsActivity : AppCompatActivity() {

    private var movie: MovieModel? = null
    private var binding: ActivityMovieDetailsBinding? = null
    private var reviewsAdapter: ReviewsAdapter? = null
    private var mDb: AppDatabase? = null
    private var canClick = true
    private val mDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details)

        mDb = AppDatabase.getInstance(this)

        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
            supportActionBar!!.setTitle(R.string.details)
        }

        if (savedInstanceState != null) {
            movie = savedInstanceState.getParcelable(MOVIE_BUNDLE)
            if (movie != null)
                displayMovieInfo()
        } else if (intent.extras != null) {
            val movieId = intent.extras!!.getString(MOVIE_ID)
            if (intent.extras!!.getBoolean(IS_SEARCH)) {
                OMDBApi.fetchMovieDetails(movieId!!, object : Callback<MovieDetails> {
                    override fun onResponse(call: Call<MovieDetails>, response: retrofit2.Response<MovieDetails>) {

                        if (response.body() != null) {
                            movie = MovieModel(response.body()!!)
                            displayMovieInfo()
                        }
                    }

                    override fun onFailure(call: Call<MovieDetails>, t: Throwable) {
                        Toast.makeText(this@MovieDetailsActivity, R.string.error_recovering_data, Toast.LENGTH_SHORT)
                            .show()
                    }
                })
            } else {
                val factory = MovieDetailsViewModelFactory(mDb!!, movieId!!)
                val viewModel = ViewModelProviders.of(this, factory).get(MovieDetailsViewModel::class.java)
                viewModel.movieDetails.observe(this, object : Observer<MovieModel> {
                    override fun onChanged(movieModel: MovieModel?) {
                        movie = movieModel
                        viewModel.movieDetails.removeObserver(this)

                        if (movie != null) {
                            displayMovieInfo()
                        }
                    }
                })
            }

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        this.finish()
        return true
    }

    private fun getVideo(movieId: String) {
        TheMovieDBApi.getVideo(movieId, object : Callback<ResponseVideo> {
            override fun onResponse(call: Call<ResponseVideo>, response: retrofit2.Response<ResponseVideo>) {

                if (response.body() != null &&
                    response.body()!!.results != null &&
                    response.body()!!.results!!.isNotEmpty()
                ) {

                    Completable.fromAction {
                        AppDatabase.getInstance(this@MovieDetailsActivity).moviesDao()
                            .setVideoPath(movieId, response.body()!!.results!![0].key!!)
                    }.subscribeOn(
                        Schedulers.computation()
                    ).subscribe()
                    setVideoPlayer(response.body()!!.results!![0].key)
                }
            }

            override fun onFailure(call: Call<ResponseVideo>, t: Throwable) {
                Toast.makeText(this@MovieDetailsActivity, R.string.error_recovering_data, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getReviews(movieId: String) {
        TheMovieDBApi.getReviews(movieId, object : Callback<ResponseReview> {
            override fun onResponse(call: Call<ResponseReview>, response: retrofit2.Response<ResponseReview>) {

                if (response.body() != null &&
                    response.body()!!.results != null &&
                    response.body()!!.results!!.isNotEmpty()
                ) {

                    binding!!.divider.visibility = View.VISIBLE
                    binding!!.llReviews.visibility = View.VISIBLE

                    setAdapter(response.body()!!.results)
                }
            }

            override fun onFailure(call: Call<ResponseReview>, t: Throwable) {
                Toast.makeText(this@MovieDetailsActivity, R.string.error_recovering_data, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setAdapter(results: ArrayList<ResultsReview>?) {

        if (reviewsAdapter == null)
            reviewsAdapter = ReviewsAdapter(this@MovieDetailsActivity, results)
        else {
            reviewsAdapter!!.setNewList(results!!)
            reviewsAdapter!!.notifyDataSetChanged()
        }

        binding!!.recyclerReviews.layoutManager = LinearLayoutManager(this@MovieDetailsActivity)

        val offsetDecoration = Utilities.OffsetDecoration(12, 8)
        binding!!.recyclerReviews.addItemDecoration(offsetDecoration)
        binding!!.recyclerReviews.adapter = reviewsAdapter
    }

    private fun setVideoPlayer(videoUrl: String?) {
        movie!!.videoPath = videoUrl
        binding!!.llPlayTrailer.visibility = View.VISIBLE
        binding!!.llPlayTrailer.setOnClickListener {
            val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + movie!!.videoPath!!))
            val webIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + movie!!.videoPath!!)
            )
            try {
                this@MovieDetailsActivity.startActivity(appIntent)
            } catch (ex: ActivityNotFoundException) {
                if (webIntent.resolveActivity(packageManager) != null)
                    this@MovieDetailsActivity.startActivity(webIntent)
                else
                    Toast.makeText(
                        this@MovieDetailsActivity,
                        R.string.error_reproducing_video,
                        Toast.LENGTH_SHORT
                    ).show()
            }
        }
    }

    private fun setFavoriteButton() {

        if (movie!!.isFavorite) {
            binding!!.ivTurnFavorite.setImageDrawable(getDrawable(R.drawable.ic_star))
            binding!!.tvTurnFavorite.text = getString(R.string.unfavorite_movie)
            binding!!.llTurnFavorite.setOnClickListener { unfavoriteMovie() }
        } else {
            binding!!.llTurnFavorite.setOnClickListener { favoriteMovie() }
        }
    }

    private fun favoriteMovie() {
        if (canClick) {
            canClick = false

            Completable.fromAction {
                mDisposable.add(Completable.fromAction { mDb!!.moviesDao().setFavorite(movie!!.id) }
                    .subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe({
                        binding!!.ivTurnFavorite.setImageDrawable(getDrawable(R.drawable.ic_star))
                        canClick = true
                        movie!!.isFavorite = true
                        binding!!.tvTurnFavorite.text = getString(R.string.unfavorite_movie)
                        binding!!.llTurnFavorite.setOnClickListener { v -> unfavoriteMovie() }
                    }, { canClick = true }))
            }.subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe()


        }
    }


    private fun unfavoriteMovie() {
        if (canClick) {
            canClick = false
            mDisposable.add(Completable.fromAction { mDb!!.moviesDao().unfavorite(movie!!.id) }
                .subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe({
                    binding!!.ivTurnFavorite.setImageDrawable(getDrawable(R.drawable.ic_star_border))
                    canClick = true
                    movie!!.isFavorite = false
                    binding!!.tvTurnFavorite.text = getString(R.string.turn_favorite)
                    binding!!.llTurnFavorite.setOnClickListener { favoriteMovie() }
                }, { canClick = true }))
        }
    }

    fun displayMovieInfo() {

        if (movie!!.runtime == null ||  movie!!.runtime!!.isEmpty()) {
            binding!!.llRuntime.visibility = View.GONE
        }
        else {
            binding!!.llRuntime.visibility = View.VISIBLE
        }

        if (movie!!.videoPath != null && movie!!.videoPath!!.isNotEmpty())
            setVideoPlayer(movie!!.videoPath)
        else
            getVideo(movie!!.id)

        getReviews(movie!!.id)
        binding!!.movie = movie

        if (movie!!.posterPath != null && movie!!.posterPath!!.isNotEmpty()) {
            var posterPath = movie!!.posterPath
            if (!movie!!.posterPath!!.contains("http")) {
                posterPath = POSTER_IMAGE_BASE_URL + movie!!.posterPath!!
            }
            Picasso.with(this@MovieDetailsActivity).load(posterPath).into(binding!!.ivPoster)
        }
        setFavoriteButton()
    }

    override fun onDestroy() {
        super.onDestroy()
        mDisposable.clear()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(MOVIE_BUNDLE, movie)
    }
}