package com.iaz.libermovies.presentation.ui.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Parcelable
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.iaz.libermovies.Constants.FAVORITED_MOVIES
import com.iaz.libermovies.Constants.LIST_STATE
import com.iaz.libermovies.Constants.POPULAR_MOVIES
import com.iaz.libermovies.Constants.SORTING_ORDER
import com.iaz.libermovies.Constants.TOP_RATED_MOVIES
import com.iaz.libermovies.Constants.UPCOMING_MOVIES
import com.iaz.libermovies.R
import com.iaz.libermovies.database.AppDatabase
import com.iaz.libermovies.databinding.ActivityMainBinding
import com.iaz.libermovies.models.MovieModel
import com.iaz.libermovies.models.OmdbResponse
import com.iaz.libermovies.models.Response
import com.iaz.libermovies.networkUtils.OMDBApi
import com.iaz.libermovies.networkUtils.TheMovieDBApi
import com.iaz.libermovies.presentation.ui.adapters.MoviesAdapter
import com.iaz.libermovies.presentation.viewModels.MainViewModel
import com.iaz.libermovies.utils.Prefs
import com.iaz.libermovies.utils.Utilities
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback

class MainActivity : AppCompatActivity() {

    private var moviesAdapter: MoviesAdapter? = null
    private var binding: ActivityMainBinding? = null
    private var mDb: AppDatabase? = null
    private var lastSelectedButton = -1
    private var selectedButton = POPULAR_MOVIES
    private var listPosition: Parcelable? = null
    private var isSearch: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        if (binding!!.toolbar != null) {
            setSupportActionBar(binding!!.toolbar)
        }

        mDb = AppDatabase.getInstance(this)

        if (savedInstanceState != null) {
            selectedButton = savedInstanceState.getInt(SORTING_ORDER, 1)
            listPosition = savedInstanceState.getParcelable(LIST_STATE)
        }

        chooseButton(selectedButton)

        binding!!.tvPopular.setOnClickListener { chooseButton(POPULAR_MOVIES) }

        binding!!.tvTopRated.setOnClickListener { chooseButton(TOP_RATED_MOVIES) }

        binding!!.tvUpcoming.setOnClickListener { chooseButton(UPCOMING_MOVIES) }

        binding!!.tvFavorited.setOnClickListener { chooseButton(FAVORITED_MOVIES) }

        // portrait mode
        if (binding!!.tvCategory != null)
            binding!!.tvCategory!!.setOnClickListener {
                if (binding!!.sortLayout.visibility == View.VISIBLE) {
                    collapseSortLayout()
                } else {
                    expandSortLayout()
                }
            }

        if (binding!!.ivSearch != null) {
            binding!!.ivSearch!!.setOnClickListener {
                if (binding!!.ivSearch!!.visibility == View.VISIBLE) {
                    binding!!.ivSearch!!.visibility = View.GONE
                    binding!!.tvCategory!!.visibility = View.GONE
                    binding!!.toolbarSearch!!.visibility = View.VISIBLE
                    binding!!.sortLayout.visibility = View.GONE
                    binding!!.toolbarSearch!!.animation = AnimationUtils.loadAnimation(this, R.anim.scale_x_open)
                }
            }
        }

        if (binding!!.ivBack != null)
            binding!!.ivBack!!.setOnClickListener {
                isSearch = false
                closeSearchLayout()
                lastSelectedButton = -1
                chooseButton(selectedButton)
            }

        if (binding!!.ivClear != null)
            binding!!.ivClear!!.setOnClickListener {
                isSearch = true
                searchMovie(binding!!.etSearch!!.text.toString())
            }

    }

    private fun getPopular() {
        if (Utilities.needsUpdatePopular(this)) {
            TheMovieDBApi.getPopular(object : Callback<Response> {
                override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {

                    if (response.body() != null && selectedButton == POPULAR_MOVIES)
                        setAdapter(response.body()!!.results)

                    Completable.fromAction { mDb!!.moviesDao().insertMovieLocalList(response.body()!!.results!!, POPULAR_MOVIES) }.subscribeOn(
                        Schedulers.computation()).subscribe()
                    Prefs.storeLastUpdatedTimePopular(this@MainActivity, System.currentTimeMillis())
                }

                override fun onFailure(call: Call<Response>, t: Throwable) {
                    Toast.makeText(this@MainActivity, R.string.error_recovering_data, Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            val viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
            viewModel.popular.observe(this, Observer { movieModels ->
                if (movieModels != null && selectedButton == POPULAR_MOVIES)
                    setAdapter(ArrayList(movieModels))
            })
        }
    }

    private fun getTopRated() {
        if (Utilities.needsUpdateTopRated(this)) {
            TheMovieDBApi.getTopRated(object : Callback<Response> {
                override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {

                    if (response.body() != null && selectedButton == TOP_RATED_MOVIES)
                        setAdapter(response.body()!!.results)

                    Completable.fromAction { mDb!!.moviesDao().insertMovieLocalList(response.body()!!.results!!, TOP_RATED_MOVIES) }.subscribeOn(Schedulers.computation()).subscribe()
                    Prefs.storeLastUpdatedTimeTopRated(this@MainActivity, System.currentTimeMillis())
                }

                override fun onFailure(call: Call<Response>, t: Throwable) {
                    Toast.makeText(this@MainActivity, R.string.error_recovering_data, Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            val viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
            viewModel.topRated.observe(this, Observer { movieModels ->
                if (movieModels != null && selectedButton == TOP_RATED_MOVIES)
                    setAdapter(ArrayList(movieModels))
            })
        }
    }

    private fun getUpcoming() {
        if (Utilities.needsUpdateUpcoming(this)) {
            TheMovieDBApi.getUpcoming(object : Callback<Response> {
                override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {

                    if (response.body() != null && selectedButton == UPCOMING_MOVIES)
                        setAdapter(response.body()!!.results)

                    Completable.fromAction { mDb!!.moviesDao().insertMovieLocalList(response.body()!!.results!!, UPCOMING_MOVIES) }.subscribeOn(Schedulers.computation()).subscribe()
                    Prefs.storeLastUpdatedTimeUpcoming(this@MainActivity, System.currentTimeMillis())
                }

                override fun onFailure(call: Call<Response>, t: Throwable) {
                    Toast.makeText(this@MainActivity, R.string.error_recovering_data, Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            val viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
            viewModel.upcoming.observe(this, Observer { movieModels ->
                if (movieModels != null && selectedButton == UPCOMING_MOVIES)
                    setAdapter(ArrayList(movieModels))
            })
        }
    }

    private fun getFavorited() {
        val viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.favorites.observe(this, Observer { movieModels ->
            if (movieModels != null && selectedButton == FAVORITED_MOVIES)
                setAdapter(ArrayList(movieModels))
        })
    }

    private fun searchMovie(movieString: String) {
        OMDBApi.searchMovie(movieString, object : Callback<OmdbResponse> {
            override fun onResponse(call: Call<OmdbResponse>, response: retrofit2.Response<OmdbResponse>) {

                val movieResults = ArrayList<MovieModel>()

                if (response.body() != null && response.body()!!.search != null)
                    for (searchResult in response.body()!!.search!!) {
                        val movieResult = MovieModel(searchResult)
                        movieResults.add(movieResult)
                    }

                setAdapter(movieResults)
            }

            override fun onFailure(call: Call<OmdbResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, R.string.error_recovering_data, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setAdapter(results: ArrayList<MovieModel>?) {

        if (moviesAdapter == null) {
            moviesAdapter = MoviesAdapter(this@MainActivity, results, isSearch)

            binding!!.recyclerMovies.layoutManager = LinearLayoutManager(this@MainActivity)

            val offsetDecoration = Utilities.OffsetDecoration(12, 36)
            binding!!.recyclerMovies.addItemDecoration(offsetDecoration)
            binding!!.recyclerMovies.adapter = moviesAdapter

        } else {
            moviesAdapter!!.setNewList(results!!, isSearch)
            moviesAdapter!!.notifyDataSetChanged()
        }
        if (listPosition != null)
            binding!!.recyclerMovies.layoutManager!!.onRestoreInstanceState(listPosition)
    }

    private fun chooseButton(number: Int) {

        if (number == lastSelectedButton)
            return

        selectedButton = number
        when (number) {
            POPULAR_MOVIES -> {
                getPopular()

                binding!!.tvPopular.background = resources.getDrawable(R.drawable.background_selected_sort)
                binding!!.tvTopRated.background = resources.getDrawable(R.drawable.background_unselected_sort)
                binding!!.tvUpcoming.background = resources.getDrawable(R.drawable.background_unselected_sort)
                binding!!.tvFavorited.background = resources.getDrawable(R.drawable.background_unselected_sort)

                binding!!.tvPopular.setTextColor(resources.getColor(R.color.colorPrimary))
                binding!!.tvTopRated.setTextColor(resources.getColor(R.color.white))
                binding!!.tvUpcoming.setTextColor(resources.getColor(R.color.white))
                binding!!.tvFavorited.setTextColor(resources.getColor(R.color.white))
            }
            TOP_RATED_MOVIES -> {
                getTopRated()

                binding!!.tvPopular.background = resources.getDrawable(R.drawable.background_unselected_sort)
                binding!!.tvTopRated.background = resources.getDrawable(R.drawable.background_selected_sort)
                binding!!.tvUpcoming.background = resources.getDrawable(R.drawable.background_unselected_sort)
                binding!!.tvFavorited.background = resources.getDrawable(R.drawable.background_unselected_sort)

                binding!!.tvPopular.setTextColor(resources.getColor(R.color.white))
                binding!!.tvTopRated.setTextColor(resources.getColor(R.color.colorPrimary))
                binding!!.tvUpcoming.setTextColor(resources.getColor(R.color.white))
                binding!!.tvFavorited.setTextColor(resources.getColor(R.color.white))
            }
            UPCOMING_MOVIES -> {
                getUpcoming()

                binding!!.tvPopular.background = resources.getDrawable(R.drawable.background_unselected_sort)
                binding!!.tvTopRated.background = resources.getDrawable(R.drawable.background_unselected_sort)
                binding!!.tvUpcoming.background = resources.getDrawable(R.drawable.background_selected_sort)
                binding!!.tvFavorited.background = resources.getDrawable(R.drawable.background_unselected_sort)

                binding!!.tvPopular.setTextColor(resources.getColor(R.color.white))
                binding!!.tvTopRated.setTextColor(resources.getColor(R.color.white))
                binding!!.tvUpcoming.setTextColor(resources.getColor(R.color.colorPrimary))
                binding!!.tvFavorited.setTextColor(resources.getColor(R.color.white))
            }
            FAVORITED_MOVIES -> {
                getFavorited()

                binding!!.tvPopular.background = resources.getDrawable(R.drawable.background_unselected_sort)
                binding!!.tvTopRated.background = resources.getDrawable(R.drawable.background_unselected_sort)
                binding!!.tvUpcoming.background = resources.getDrawable(R.drawable.background_unselected_sort)
                binding!!.tvFavorited.background = resources.getDrawable(R.drawable.background_selected_sort)

                binding!!.tvPopular.setTextColor(resources.getColor(R.color.white))
                binding!!.tvTopRated.setTextColor(resources.getColor(R.color.white))
                binding!!.tvUpcoming.setTextColor(resources.getColor(R.color.white))
                binding!!.tvFavorited.setTextColor(resources.getColor(R.color.colorPrimary))
            }
        }
        lastSelectedButton = number
    }

    private fun collapseSortLayout() {
        binding!!.sortLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_y_close))
        binding!!.sortLayout.animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {

            }

            override fun onAnimationEnd(animation: Animation) {
                binding!!.sortLayout.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })
    }

    private fun expandSortLayout() {
        binding!!.sortLayout.visibility = View.VISIBLE
        binding!!.sortLayout.animation = AnimationUtils.loadAnimation(this, R.anim.scale_y_open)
    }

    private fun closeSearchLayout() {
        binding!!.etSearch!!.text.clear()
        binding!!.ivSearch!!.visibility = View.VISIBLE
        binding!!.tvCategory!!.visibility = View.VISIBLE
        binding!!.toolbarSearch!!.startAnimation(closeSearchAnimation())
    }

    private fun closeSearchAnimation(): Animation {
        val animation = AnimationUtils.loadAnimation(this, R.anim.scale_x_close)
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                binding!!.toolbarSearch!!.visibility = View.INVISIBLE
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })

        return animation
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(SORTING_ORDER, selectedButton)
        outState.putParcelable(LIST_STATE, binding!!.recyclerMovies.layoutManager!!.onSaveInstanceState())
    }

}