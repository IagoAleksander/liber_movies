package com.iaz.libermovies.networkUtils

import com.iaz.libermovies.models.MovieDetails
import com.iaz.libermovies.models.OmdbResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object OMDBApi {

    private const val OMDB_API_BASE_URL = "http://www.omdbapi.com/"

    private val omdbService: OMDBService

    init {

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)

        val retrofit = Retrofit.Builder()
                .baseUrl(OMDB_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build()
        omdbService = retrofit.create(OMDBService::class.java)
    }

    fun searchMovie(movieString: String, callback: Callback<OmdbResponse>) {
        val call = omdbService.searchMovie(movieString)
        call.enqueue(callback)
    }

    fun fetchMovieDetails(movieString: String, callback: Callback<MovieDetails>) {
        val call = omdbService.fetchMovieDetails(movieString)
        call.enqueue(callback)
    }
}
