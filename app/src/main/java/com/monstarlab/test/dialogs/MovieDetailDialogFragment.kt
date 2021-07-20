package com.monstarlab.test.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.google.gson.Gson
import com.monstarlab.test.R
import com.monstarlab.test.activities.MovieListActivity
import com.monstarlab.test.bo.Movie
import com.monstarlab.test.utils.FavoritesPropertyManager
import com.squareup.picasso.Picasso
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request

class MovieDetailDialogFragment(val movie: Movie,
                                val movieListActivity: MovieListActivity) : DialogFragment() {
    private val client = OkHttpClient()
    private lateinit var favoritePropertiesManager: FavoritesPropertyManager
    private lateinit var favoriteBtn: ImageView
    private lateinit var moviePosterImageView: ImageView
    private lateinit var movieNameTextView: TextView
    private lateinit var movieOverviewTextView: TextView
    private lateinit var movieReleaseTextView: TextView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        favoritePropertiesManager = FavoritesPropertyManager(requireContext())

        super.onCreate(savedInstanceState)
        var builder  = AlertDialog.Builder(this.context)

        val dialogContent = activity?.layoutInflater?.inflate(R.layout.movie_dialog, null)
        builder.setView(dialogContent)
        movieNameTextView = dialogContent!!.findViewById<TextView>(R.id.detail_movie_name)
        movieOverviewTextView =dialogContent!!.findViewById<TextView>(R.id.detail_movie_overview)
        movieReleaseTextView = dialogContent!!.findViewById<TextView>(R.id.detail_movie_release)
        moviePosterImageView = dialogContent!!.findViewById<ImageView>(R.id.detail_movie_poster)
        favoriteBtn = dialogContent!!.findViewById<ImageView>(R.id.detail_favorite_btn)

        val url = movieListActivity.getString(R.string.api_detail_url, movie.id.toString())
        GlobalScope.launch {
            requestData(url)
        }

        return builder.show()
    }

    /*
    Consume webservice in
    @Param url
    to get the detail of the selected movie
     */
    fun requestData(url: String){
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).execute().use { response -> processResponse( response.body()!!.string()) }
    }

    /*
    process the response of the webservice in
    @Param response
    to display the information in the UI
     */
    fun processResponse(response: String){
        var movieDetail = Gson().fromJson<Movie>(response, Movie::class.java)
        movieListActivity.runOnUiThread {
            movieNameTextView.text = movieDetail.original_title
            movieOverviewTextView.text = movieDetail.overview
            movieReleaseTextView.text = movieDetail.release_date
            val url = movieListActivity.getString(R.string.images_detail_url, movieDetail.poster_path)
            Picasso.get().load(url).into(moviePosterImageView)
            if (isInFavorites()) {
                favoriteBtn.setImageResource(R.drawable.ic_baseline_favorite_on)
            }
            favoriteBtn.setOnClickListener {
                if (!isInFavorites()) {
                    addToFavorites()
                }else{
                    removeFromFavorites()
                }
            }
        }
    }

    /*
    Adds a movie to favorite list
     */
    fun addToFavorites(){
        favoritePropertiesManager.addToFavorites(movie)
        favoriteBtn.setImageResource(R.drawable.ic_baseline_favorite_on)
    }

    /*
    Removes a movie to favorite list
     */
    fun removeFromFavorites(){
        favoritePropertiesManager.removeFromFavorites(movie)
        favoriteBtn.setImageResource(R.drawable.ic_baseline_favorite_off)
    }

    /*
    Checks if the movie allready in favorite list
     */
    fun isInFavorites(): Boolean{
        favoritePropertiesManager.getFavorites().movies.forEach {
            if(it.id == movie.id){
                return true;
            }
        }
        return false;
    }

}