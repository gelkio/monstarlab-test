package com.monstarlab.test.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.monstarlab.test.R
import com.monstarlab.test.activities.MovieListActivity
import com.monstarlab.test.adapters.MovieAdapter
import com.monstarlab.test.bo.Movie
import com.monstarlab.test.utils.FavoritesPropertyManager

class FavoriteListDialogFragment(val movieListActivity: MovieListActivity): DialogFragment()  {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        super.onCreate(savedInstanceState)
        var builder = AlertDialog.Builder(this.context)

        val dialogContent = activity?.layoutInflater?.inflate(R.layout.favorite_list_dialog, null)
        builder.setView(dialogContent)

        val movieListRecyclerView = dialogContent!!.findViewById<RecyclerView>(R.id.favorite_recyclerview)
        val movieAdapter = MovieAdapter{ movie -> onMovieClick(movie)}
        movieListRecyclerView.adapter = movieAdapter
        movieListRecyclerView.layoutManager = LinearLayoutManager(context)
        movieAdapter.setMovies(FavoritesPropertyManager(requireContext()).getFavorites().movies);

        return builder.show()
    }

    /*
    displays the Detail dialog with the information in
    @Param movie
     */
    fun onMovieClick(movie: Movie){
        println(movie)
        MovieDetailDialogFragment(movie, movieListActivity).show(movieListActivity.supportFragmentManager, "MovieDetail")
    }
}