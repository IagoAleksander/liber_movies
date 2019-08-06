package com.iaz.libermovies.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
import android.support.annotation.NonNull
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movie")
class MovieModel() : Parcelable {

    @PrimaryKey
    @NonNull
    lateinit var id: String

    var title: String? = null
    var overview: String? = null

    @SerializedName("original_language")
    var originalLanguage: String? = null

    @SerializedName("release_date")
    var releaseDate: String? = null

    @SerializedName("original_title")
    var originalTitle: String? = null

    @SerializedName("vote_count")
    var voteCount: String? = null

    @SerializedName("poster_path")
    var posterPath: String? = null
    var video: Boolean = false
    var videoPath: String? = null
    var popularity: String? = null

    @SerializedName("vote_average")
    var voteAverage: String? = null

    @SerializedName("backdrop_path")
    var backdropPath: String? = null

    var runtime: String? = null

    var isPopular: Boolean = false
    var isTopRated: Boolean = false
    var isUpcoming: Boolean = false
    var isFavorite: Boolean = false

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        title = parcel.readString()
        overview = parcel.readString()
        originalLanguage = parcel.readString()
        releaseDate = parcel.readString()
        originalTitle = parcel.readString()
        voteCount = parcel.readString()
        posterPath = parcel.readString()
        video = parcel.readByte() != 0.toByte()
        videoPath = parcel.readString()
        popularity = parcel.readString()
        voteAverage = parcel.readString()
        backdropPath = parcel.readString()
        runtime = parcel.readString()
        isPopular = parcel.readByte() != 0.toByte()
        isTopRated = parcel.readByte() != 0.toByte()
        isUpcoming = parcel.readByte() != 0.toByte()
        isFavorite = parcel.readByte() != 0.toByte()
    }

    constructor(movie: Search) : this() {
        this.title = movie.title
        this.id = movie.imdbID!!
        this.posterPath = movie.poster
        this.releaseDate = movie.year
    }

    constructor(movie: MovieDetails) : this() {
        this.title = movie.title
        this.id = movie.imdbID!!
        this.posterPath = movie.poster
        this.overview = movie.plot
        this.voteAverage = movie.imdbRating
        this.releaseDate = movie.released
        this.runtime = movie.runtime
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(overview)
        parcel.writeString(originalLanguage)
        parcel.writeString(releaseDate)
        parcel.writeString(originalTitle)
        parcel.writeString(voteCount)
        parcel.writeString(posterPath)
        parcel.writeByte(if (video) 1 else 0)
        parcel.writeString(videoPath)
        parcel.writeString(popularity)
        parcel.writeString(voteAverage)
        parcel.writeString(backdropPath)
        parcel.writeByte(if (isPopular) 1 else 0)
        parcel.writeByte(if (isTopRated) 1 else 0)
        parcel.writeByte(if (isUpcoming) 1 else 0)
        parcel.writeByte(if (isFavorite) 1 else 0)
    }

    companion object CREATOR : Parcelable.Creator<MovieModel> {
        override fun createFromParcel(parcel: Parcel): MovieModel {
            return MovieModel(parcel)
        }

        override fun newArray(size: Int): Array<MovieModel?> {
            return arrayOfNulls(size)
        }
    }


}