package com.iaz.libermovies.utils

import android.content.Context
import android.content.SharedPreferences
import com.iaz.libermovies.R

object Prefs {

    private var sharedPrefs: SharedPreferences? = null

    private fun getSharedPrefs(context: Context): SharedPreferences {
        if (sharedPrefs == null)
            sharedPrefs = context.getSharedPreferences(context.getString(R.string.pref_key), Context.MODE_PRIVATE)

        return sharedPrefs!!
    }

    fun storeLastUpdatedTimePopular(context: Context, timeMillis: Long) {
        val editor = getSharedPrefs(context).edit()
        editor.putLong(context.getString(R.string.last_updated_popular), timeMillis)
        editor.apply()
    }

    fun getLastUpdatedTimePopular(context: Context): Long {
        return getSharedPrefs(context).getLong(context.getString(R.string.last_updated_popular), 0)
    }

    fun storeLastUpdatedTimeTopRated(context: Context, timeMillis: Long) {
        val editor = getSharedPrefs(context).edit()
        editor.putLong(context.getString(R.string.last_updated_top_rated), timeMillis)
        editor.apply()
    }

    fun getLastUpdatedTimeTopRated(context: Context): Long {
        return getSharedPrefs(context).getLong(context.getString(R.string.last_updated_top_rated), 0)
    }

    fun storeLastUpdatedTimeUpcoming(context: Context, timeMillis: Long) {
        val editor = getSharedPrefs(context).edit()
        editor.putLong(context.getString(R.string.last_updated_upcoming), timeMillis)
        editor.apply()
    }

    fun getLastUpdatedTimeUpcoming(context: Context): Long {
        return getSharedPrefs(context).getLong(context.getString(R.string.last_updated_upcoming), 0)
    }

}