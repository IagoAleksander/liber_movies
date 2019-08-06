package com.iaz.libermovies.models

import com.google.gson.annotations.SerializedName

class MovieDetails {

    @SerializedName("Released")
    var released: String? = null

    @SerializedName("Website")
    var website: String? = null

    @SerializedName("Type")
    var type: String? = null

    var imdbVotes: String? = null

    @SerializedName("Runtime")
    var runtime: String? = null

    @SerializedName("Response")
    var response: String? = null

    @SerializedName("Poster")
    var poster: String? = null

    var imdbID: String? = null

    @SerializedName("Country")
    var country: String? = null

    @SerializedName("BoxOffice")
    var boxOffice: String? = null

    @SerializedName("Title")
    var title: String? = null

    @SerializedName("Dvd")
    var dvd: String? = null

    var imdbRating: String? = null

    @SerializedName("Year")
    var year: String? = null

    @SerializedName("Rated")
    var rated: String? = null

    @SerializedName("Actors")
    var actors: String? = null

    @SerializedName("Plot")
    var plot: String? = null

    @SerializedName("Metascore")
    var metascore: String? = null

    @SerializedName("Writer")
    var writer: String? = null

    @SerializedName("Production")
    var production: String? = null

    @SerializedName("Genre")
    var genre: String? = null

    @SerializedName("Language")
    var language: String? = null

    @SerializedName("Awards")
    var awards: String? = null

    @SerializedName("Director")
    var director: String? = null

}