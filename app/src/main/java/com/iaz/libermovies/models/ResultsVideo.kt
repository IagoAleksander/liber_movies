package com.iaz.libermovies.models

import android.os.Parcel
import android.os.Parcelable

class ResultsVideo private constructor(source: Parcel) : Parcelable {

    var key: String? = null

    override fun describeContents(): Int {
        return 0
    }

    init {
        key = source.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(key)
    }

    companion object CREATOR : Parcelable.Creator<ResultsVideo> {
        override fun createFromParcel(parcel: Parcel): ResultsVideo {
            return ResultsVideo(parcel)
        }

        override fun newArray(size: Int): Array<ResultsVideo?> {
            return arrayOfNulls(size)
        }
    }

}