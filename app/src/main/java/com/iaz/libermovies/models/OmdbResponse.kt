package com.iaz.libermovies.models

import com.google.gson.annotations.SerializedName

class OmdbResponse {

    @SerializedName("Search")
    val search: List<Search>? = null
}