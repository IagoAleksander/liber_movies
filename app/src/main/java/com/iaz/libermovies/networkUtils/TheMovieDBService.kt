package com.iaz.libermovies.networkUtils

import com.iaz.libermovies.models.Response
import com.iaz.libermovies.models.ResponseReview
import com.iaz.libermovies.models.ResponseVideo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

internal interface TheMovieDBService {

    @get:GET("3/movie/popular?api_key=$API_KEY")
    val popular: Call<Response>

    @get:GET("3/movie/top_rated?api_key=$API_KEY")
    val topRated: Call<Response>

    @get:GET("3/movie/upcoming?api_key=$API_KEY")
    val upcoming: Call<Response>

    @GET("3/movie/{movie_id}/videos?api_key=$API_KEY")
    fun getVideos(
            @Path("movie_id") movieID: String): Call<ResponseVideo>

    @GET("3/movie/{movie_id}/reviews?api_key=$API_KEY")
    fun getReviews(
            @Path("movie_id") movieID: String): Call<ResponseReview>

    companion object {

        //TODO insert TMDB key
        const val API_KEY = ""
    }

}