package com.iaz.libermovies.utils

import android.content.Context
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View
import com.iaz.libermovies.Constants.updateTime

object Utilities {

    class OffsetDecoration(private val mTopOffset: Int, private val mBottomOffset: Int) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)
            val dataSize = state.itemCount
            val position = parent.getChildAdapterPosition(view)
            if (dataSize > 0 && position == dataSize - 1) {
                outRect.set(0, 0, 0, mBottomOffset)
            } else if (position == 0) {
                outRect.set(0, mTopOffset, 0, 0)
            } else {
                outRect.set(0, 0, 0, 0)
            }

        }
    }

    fun needsUpdatePopular(context: Context): Boolean {
        return System.currentTimeMillis() - Prefs.getLastUpdatedTimePopular(context) > updateTime
    }

    fun needsUpdateUpcoming(context: Context): Boolean {
        return System.currentTimeMillis() - Prefs.getLastUpdatedTimeUpcoming(context) > updateTime
    }

    fun needsUpdateTopRated(context: Context): Boolean {
        return System.currentTimeMillis() - Prefs.getLastUpdatedTimeTopRated(context) > updateTime
    }

}