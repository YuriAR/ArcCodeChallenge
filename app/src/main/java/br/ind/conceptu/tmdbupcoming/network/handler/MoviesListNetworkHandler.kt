package br.ind.conceptu.tmdbupcoming.network.handler

import android.graphics.Movie
import br.ind.conceptu.tmdbupcoming.App
import br.ind.conceptu.tmdbupcoming.model.MovieResult
import br.ind.conceptu.tmdbupcoming.network.ServerContentManager
import br.ind.conceptu.tmdbupcoming.protocol.MoviesListProtocol
import com.android.volley.AuthFailureError
import com.android.volley.toolbox.StringRequest
import io.reactivex.Single
import org.json.JSONObject

class MoviesListNetworkHandler: MoviesListProtocol.NetworkHandler {
    override fun getMoviesList(page: Int): Single<MovieResult> {
        val path = "/movie/upcoming"

        val parameters = HashMap<String, String>()
        parameters["api_key"] = ServerContentManager.tmdbKey
        parameters["page"] = page.toString()

        val url = ServerContentManager.getUrlWithPath(path, parameters)

        return Single.create { subscriber ->
            val request = object : StringRequest(Method.GET, url, { response ->
                Thread({
                    if (!response.isNullOrEmpty()){
                        val responseJson = JSONObject(response)
                        subscriber.onSuccess(MovieResult.fromJsonObject(responseJson))
                    }
                    else{
                        val throwable = Throwable("No movies found")
                        subscriber.onError(throwable)
                    }
                }).start()
            }, { error ->
                subscriber.onError(error)
            }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    return ServerContentManager.getHeaders()
                }
            }
            App.instance?.addToRequestQueue(request)
        }
    }
}