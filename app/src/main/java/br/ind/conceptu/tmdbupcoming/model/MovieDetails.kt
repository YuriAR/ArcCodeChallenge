package br.ind.conceptu.tmdbupcoming.model

import com.google.gson.Gson
import org.json.JSONObject

class MovieDetails {
    var poster_path = ""
    var overview = ""
    var release_date = ""  //AAAA-MM-dd
    var genres = listOf<Genre>()
    var tagline = ""
    var id = 0
    var original_title = ""
    var title = ""
    var backdrop_path = ""
    var popularity = 0.0
    var vote_count = 0
    var vote_average = 0.0

    companion object {
        fun fromJsonObject(jsonObject: JSONObject):MovieDetails{
            return Gson().fromJson(jsonObject.toString(), MovieDetails::class.java)
        }
    }
}