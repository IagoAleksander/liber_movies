package com.iaz.libermovies.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class Search private constructor(source: Parcel) : Parcelable {

    @SerializedName("Year")
    var year: String? = null

    @SerializedName("Type")
    var type: String? = null

    @SerializedName("Poster")
    var poster: String? = null

    var imdbID: String? = null

    @SerializedName("Title")
    var title: String? = null

    init {
        year = source.readString()
        type = source.readString()
        poster = source.readString()
        imdbID = source.readString()
        title = source.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(year)
        parcel.writeString(type)
        parcel.writeString(poster)
        parcel.writeString(imdbID)
        parcel.writeString(title)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Search> {
        override fun createFromParcel(parcel: Parcel): Search {
            return Search(parcel)
        }

        override fun newArray(size: Int): Array<Search?> {
            return arrayOfNulls(size)
        }
    }


}