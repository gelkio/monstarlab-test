package com.monstarlab.test.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.monstarlab.test.R
import com.monstarlab.test.bo.Movie
import com.squareup.picasso.Picasso

class MovieAdapter(private val onClick: (Movie) -> Unit) :
    RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    private var movieList : List<Movie> = ArrayList<Movie>()

    class ViewHolder(view: View, val onClick: (Movie) -> Unit) : RecyclerView.ViewHolder(view) {
        val movieNameTextView: TextView = view.findViewById(R.id.movie_name)
        val movieReleaseTextView: TextView = view.findViewById(R.id.movie_release)
        val movieOverviewTextView: TextView = view.findViewById(R.id.movie_overview)
        val moviePosterImageView: ImageView = view.findViewById(R.id.movie_poster)
        private var currentMovie: Movie? = null

        init {
            view.setOnClickListener { onClick(currentMovie!!) }
        }

        fun bind(movie: Movie){
           currentMovie = movie

            movieNameTextView.text = movie.original_title
            movieReleaseTextView.text = movie.release_date
            movieOverviewTextView.text = movie.overview

            val url = "https://www.themoviedb.org/t/p/w300_and_h450_bestv2/"+movie.poster_path
            Picasso.get().load(url).into(moviePosterImageView)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_item_movie, viewGroup, false)

        return ViewHolder(view, onClick)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(movieList[position])
    }

    fun setMovies(movieList: List<Movie>){
        this.movieList = movieList
        notifyDataSetChanged()
    }

    override fun getItemCount() = movieList.size
}