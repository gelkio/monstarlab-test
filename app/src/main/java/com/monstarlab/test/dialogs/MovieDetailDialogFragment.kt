package com.monstarlab.test.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.monstarlab.test.R
import com.monstarlab.test.bo.Movie

class MovieDetailDialogFragment(val movie: Movie) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        super.onCreate(savedInstanceState)
        var builder  = AlertDialog.Builder(this.context)

        val dialogContent = activity?.layoutInflater?.inflate(R.layout.movie_dialog, null)
        builder.setTitle(movie.original_title).setView(dialogContent)
        dialogContent!!.findViewById<TextView>(R.id.detail_movie_name).text = movie.original_title
        dialogContent!!.findViewById<TextView>(R.id.detail_movie_overview).text = movie.overview
        dialogContent!!.findViewById<TextView>(R.id.detail_movie_release).text = movie.release_date
        
        return builder.show()
    }

}