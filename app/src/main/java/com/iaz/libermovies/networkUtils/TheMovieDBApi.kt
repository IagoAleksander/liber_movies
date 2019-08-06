package com.iaz.libermovies.networkUtils

import com.iaz.libermovies.models.Response
import com.iaz.libermovies.models.ResponseReview
import com.iaz.libermovies.models.ResponseVideo
import okhttp3.OkHttpClient
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TheMovieDBApi {

    private const val TMDB_API_BASE_URL = "https://api.themoviedb.org/"
    private val theMovieDBService: TheMovieDBService

    init {

        val httpClient = OkHttpClient.Builder()

        val retrofit = Retrofit.Builder()
                .baseUrl(TMDB_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build()
        theMovieDBService = retrofit.create(TheMovieDBService::class.java)
    }


    fun getPopular(callback: Callback<Response>) {
        val call = theMovieDBService.popular
        call.enqueue(callback)
    }

    fun getTopRated(callback: Callback<Response>) {
        val call = theMovieDBService.topRated
        call.enqueue(callback)
    }

    fun getUpcoming(callback: Callback<Response>) {
        val call = theMovieDBService.upcoming
        call.enqueue(callback)
    }

    fun getVideo(movieId: String, callback: Callback<ResponseVideo>) {
        val call = theMovieDBService.getVideos(movieId)
        call.enqueue(callback)
    }

    fun getReviews(movieId: String, callback: Callback<ResponseReview>) {
        val call = theMovieDBService.getReviews(movieId)
        call.enqueue(callback)
    }
}