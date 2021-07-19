package com.monstarlab.test.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.monstarlab.test.R
import com.monstarlab.test.adapters.MovieAdapter
import com.monstarlab.test.bo.Movie
import com.monstarlab.test.bo.MovieSearchResult
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

        val url = "https://api.themoviedb.org/3/search/movie?api_key=39aa1a462b9a8e9bc2349ed20bf87913&query=avengers"
        GlobalScope.launch {
            requestData(url)
        }

        var movieList = ArrayList<Movie>()
        movieList.add(Movie("Avengers"))
        movieListRecyclerView = findViewById<RecyclerView>(R.id.movie_list_recyclerview)
        movieAdapter = MovieAdapter{ movie -> onMovieClick(movie)}
        movieListRecyclerView.adapter = movieAdapter
        movieListRecyclerView.layoutManager = LinearLayoutManager(this)
        movieAdapter.setMovies(movieList);
    }

    fun requestData(context: String){
        val request = Request.Builder()
            .url(context)
            .build()

        client.newCall(request).execute().use { response -> processResponse( response.body()!!.string()) }
    }

    fun processResponse(responseBody: String?){
        println(responseBody)
        val movieSearchResult = this.gson.fromJson<MovieSearchResult>(responseBody, MovieSearchResult::class.java)
        runOnUiThread {
            movieAdapter.setMovies(movieSearchResult.results);
        }
    }

    fun onMovieClick(movie:Movie){
        println(movie)
    }
}