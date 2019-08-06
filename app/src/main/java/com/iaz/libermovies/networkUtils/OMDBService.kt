package com.iaz.libermovies.networkUtils

import com.iaz.libermovies.models.MovieDetails
import com.iaz.libermovies.models.OmdbResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

internal interface OMDBService {

    @GET("?apikey=$API_KEY")
    fun searchMovie(
            @Query("s") movieID: String): Call<OmdbResponse>

    @GET("?apikey=$API_KEY&plot=full")
    fun fetchMovieDetails(
            @Query("i") s: String
    ): Call<MovieDetails>

    companion object {

        //TODO insert OMDB key
        const val API_KEY = ""
    }

}