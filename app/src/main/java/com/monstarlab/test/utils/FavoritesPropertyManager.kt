package com.monstarlab.test.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.monstarlab.test.bo.Favorites
import com.monstarlab.test.bo.Movie

class FavoritesPropertyManager {
    private val FAVORITES_KEY = "favorites"
    private var preferences: SharedPreferences

    constructor(context: Context){
        preferences = context.getSharedPreferences("favorites", Context.MODE_PRIVATE)
    }

    fun addToFavorites(movie: Movie){
        var favorites = getFavorites()
        if(favorites.movies == null){
            favorites = Favorites(mutableListOf<Movie>())
        }
        favorites.movies.add(movie)
        preferences.edit().putString(FAVORITES_KEY, Gson().toJson(favorites)).commit()
    }

    fun removeFromFavorites(movieToRemove: Movie){
        var favorites = getFavorites()
        if(favorites.movies != null){
            favorites.movies.removeIf{ movie -> movie.id == movieToRemove.id }
        }
        preferences.edit().putString(FAVORITES_KEY, Gson().toJson(favorites)).commit()
    }

    fun getFavorites(): Favorites{
        val favoritesString = preferences.getString(FAVORITES_KEY,"{}")
        return Gson().fromJson<Favorites>(favoritesString, Favorites::class.java)
    }
}