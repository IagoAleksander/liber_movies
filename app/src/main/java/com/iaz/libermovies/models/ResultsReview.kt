package com.iaz.libermovies.models

import android.os.Parcel
import android.os.Parcelable

class ResultsReview private constructor(source: Parcel) : Parcelable {

    var author: String? = null

    var content: String? = null

    var url: String? = null

    init {
        author = source.readString()
        content = source.readString()
        url = source.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(author)
        parcel.writeString(content)
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ResultsReview> {
        override fun createFromParcel(parcel: Parcel): ResultsReview {
            return ResultsReview(parcel)
        }

        override fun newArray(size: Int): Array<ResultsReview?> {
            return arrayOfNulls(size)
        }
    }


}