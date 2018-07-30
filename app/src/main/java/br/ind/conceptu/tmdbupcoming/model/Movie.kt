package br.ind.conceptu.tmdbupcoming.model

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray

class Movie {
    var loadingMovieDummy = false
    var poster_path = ""
    var overview = ""
    var release_date = ""  //AAAA-MM-dd
    var genre_ids = listOf<Int>()
    var id = 0
    var original_title = ""
    var title = ""
    var backdrop_path = ""
    var popularity = 0.0
    var vote_count = 0
    var vote_average = 0.0

    companion object {
        fun fromJsonArray(jsonArray:JSONArray):MutableList<Movie>{
            val moviesArrayType = object : TypeToken<List<Movie>>() {}.type
            return Gson().fromJson(jsonArray.toString(), moviesArrayType)
        }
    }
}