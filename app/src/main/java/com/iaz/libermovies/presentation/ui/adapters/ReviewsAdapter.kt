package com.iaz.libermovies.presentation.ui.adapters

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.iaz.libermovies.R
import com.iaz.libermovies.databinding.ItemReviewBinding
import com.iaz.libermovies.models.ResultsReview

class ReviewsAdapter(private val context: Context, private var resultsReviews: ArrayList<ResultsReview>?) : RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): ReviewViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemReviewBinding>(layoutInflater, R.layout.item_review, parent, false)

        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(reviewViewHolder: ReviewViewHolder, i: Int) {

        val review = resultsReviews!![i]
        reviewViewHolder.binding.tvName.text = review.author
        reviewViewHolder.binding.tvContent.text = review.content
        reviewViewHolder.binding.itemReview.setOnClickListener {
            val i1 = Intent(Intent.ACTION_VIEW)
            i1.data = Uri.parse(review.url)
            context.startActivity(i1)
        }

    }

    override fun getItemCount(): Int {
        return resultsReviews!!.size
    }

    fun setNewList(results: ArrayList<ResultsReview>) {
        this.resultsReviews = results
    }

    inner class ReviewViewHolder(val binding: ItemReviewBinding) : RecyclerView.ViewHolder(binding.root)


}