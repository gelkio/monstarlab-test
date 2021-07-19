package com.monstarlab.test.bo

import android.icu.number.IntegerWidth

data class MovieSearchResult(val page: Int,
                             val results: List<Movie>,
                             val total_pages: Int,
                             val total_results: Int)