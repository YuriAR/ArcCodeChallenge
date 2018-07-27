package br.ind.conceptu.tmdbupcoming.model

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray

class Genre {
    var id = 0
    var name = ""

    companion object {
        fun fromJsonArray(jsonArray: JSONArray):List<Genre>{
            val genreArrayType = object : TypeToken<List<Genre>>() {}.type
            return Gson().fromJson(jsonArray.toString(), genreArrayType)
        }
    }
}