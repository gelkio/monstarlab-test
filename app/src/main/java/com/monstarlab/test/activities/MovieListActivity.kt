package com.monstarlab.test.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.monstarlab.test.R
import com.monstarlab.test.adapters.MovieAdapter
import com.monstarlab.test.bo.Movie
import com.monstarlab.test.bo.MovieSearchResult
import com.monstarlab.test.dialogs.FavoriteListDialogFragment
import com.monstarlab.test.dialogs.MovieDetailDialogFragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request


class MovieListActivity : AppCompatActivity() {
    private val client = OkHttpClient()
    private lateinit var movieListRecyclerView: RecyclerView
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var gson: Gson

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)

        gson = Gson()

        val url = getString(R.string.api_search_url, "avengers")
        GlobalScope.launch {
            requestData(url)
        }

        var movieList = ArrayList<Movie>()
        movieListRecyclerView = findViewById<RecyclerView>(R.id.movie_list_recyclerview)
        movieAdapter = MovieAdapter{ movie -> onMovieClick(movie)}
        movieListRecyclerView.adapter = movieAdapter
        movieListRecyclerView.layoutManager = LinearLayoutManager(this)
        movieAdapter.setMovies(movieList);

        findViewById<Button>(R.id.movie_list_search_btn).setOnClickListener {
            var searchText = findViewById<EditText>(R.id.movie_list_search_field).text
            val url = getString(R.string.api_search_url, searchText)
            GlobalScope.launch {
                requestData(url)
            }
        }

        findViewById<ImageView>(R.id.movie_list_favorite_btn).setOnClickListener {
            FavoriteListDialogFragment(this).show(supportFragmentManager, "FavoriteList")
        }
    }

    /*
    Consume webservice in
    @Param url
    to get the detail of the selected movie
     */
    fun requestData(context: String){
        val request = Request.Builder()
            .url(context)
            .build()

        client.newCall(request).execute().use { response -> processResponse( response.body()!!.string()) }
    }

    /*
    process the response of the webservice in
    @Param responseBody
    to display the information in the recycler view
     */
    fun processResponse(responseBody: String?){
        println(responseBody)
        val movieSearchResult = this.gson.fromJson<MovieSearchResult>(responseBody, MovieSearchResult::class.java)
        runOnUiThread {
            movieAdapter.setMovies(movieSearchResult.results);
        }
    }

    /*
    displays the Detail dialog with the information in
    @Param movie
     */
    fun onMovieClick(movie:Movie){
        println(movie)
        MovieDetailDialogFragment(movie, this).show(supportFragmentManager, "MovieDetail")
    }
}