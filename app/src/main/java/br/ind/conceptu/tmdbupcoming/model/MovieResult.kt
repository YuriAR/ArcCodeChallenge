package br.ind.conceptu.tmdbupcoming.model

import com.google.gson.Gson
import org.json.JSONObject

class MovieResult {
    var page = 0
    var results = listOf<Movie>()
    var total_pages = 0

    companion object {
        fun fromJsonObject(jsonObject: JSONObject):MovieResult{
            return Gson().fromJson(jsonObject.toString(), MovieResult::class.java)
        }
    }
}