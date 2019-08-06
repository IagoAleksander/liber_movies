package com.iaz.libermovies.presentation.ui.adapters

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.iaz.libermovies.Constants.IS_SEARCH
import com.iaz.libermovies.Constants.MOVIE_ID
import com.iaz.libermovies.Constants.POSTER_IMAGE_BASE_URL
import com.iaz.libermovies.R
import com.iaz.libermovies.databinding.ItemMovieBinding
import com.iaz.libermovies.models.MovieModel
import com.iaz.libermovies.presentation.ui.activities.MovieDetailsActivity
import com.squareup.picasso.Picasso

class MoviesAdapter(private val context: Context, private var moviesList: ArrayList<MovieModel>?, private var isSearch: Boolean) : RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): MovieViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemMovieBinding>(layoutInflater, R.layout.item_movie, parent, false)

        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(movieViewHolder: MovieViewHolder, i: Int) {

        val movie = moviesList!![i]

        var posterPath = movie.posterPath
        if (movie.posterPath != null && !movie.posterPath!!.contains("http")) {
            posterPath = POSTER_IMAGE_BASE_URL + movie.posterPath!!
        }
        Picasso.with(context).load(posterPath).into(movieViewHolder.binding.ivPoster)
        movieViewHolder.binding.tvName.text = movie.title

        if (movie.releaseDate != null)
            movieViewHolder.binding.tvYear.text = String.format("(%s)", movie.releaseDate!!.substring(0, 4))

        if (movie.voteAverage != null) {
            movieViewHolder.binding.rbScore.visibility = View.VISIBLE
            movieViewHolder.binding.tvScore.visibility = View.VISIBLE
            movieViewHolder.binding.rbScore.rating = java.lang.Float.valueOf(movie.voteAverage!!) / 2f
            movieViewHolder.binding.tvScore.text = movie.voteAverage
        } else {
            movieViewHolder.binding.rbScore.visibility = View.GONE
            movieViewHolder.binding.tvScore.visibility = View.GONE
        }

        movieViewHolder.binding.cvMovie.setOnClickListener {
            val intent = Intent(context, MovieDetailsActivity::class.java)
            intent.putExtra(MOVIE_ID, movie.id)
            intent.putExtra(IS_SEARCH, isSearch)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return moviesList!!.size
    }

    fun setNewList(results: ArrayList<MovieModel>, isSearch: Boolean) {
        this.moviesList = results
        this.isSearch = isSearch
    }

    inner class MovieViewHolder(val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root)


}