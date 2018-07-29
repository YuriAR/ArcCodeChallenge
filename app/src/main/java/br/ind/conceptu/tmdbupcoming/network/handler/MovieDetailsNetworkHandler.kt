package br.ind.conceptu.tmdbupcoming.network.handler

import br.ind.conceptu.tmdbupcoming.App
import br.ind.conceptu.tmdbupcoming.model.MovieDetails
import br.ind.conceptu.tmdbupcoming.network.ServerContentManager
import br.ind.conceptu.tmdbupcoming.protocol.MovieDetailsProtocol
import com.android.volley.AuthFailureError
import com.android.volley.toolbox.StringRequest
import io.reactivex.Single
import org.json.JSONObject

class MovieDetailsNetworkHandler:MovieDetailsProtocol.NetworkHandler {
    override fun getMovieDetails(movieId: Int): Single<MovieDetails> {
        val path = "/movie/$movieId"

        val parameters = HashMap<String, String>()
        parameters["api_key"] = ServerContentManager.tmdbKey

        val url = ServerContentManager.getUrlWithPath(path, parameters)

        return Single.create { subscriber ->
            val request = object : StringRequest(Method.GET, url, { response ->
                Thread({
                    if (!response.isNullOrEmpty()){
                        val responseJson = JSONObject(response)
                        subscriber.onSuccess(MovieDetails.fromJsonObject(responseJson))
                    }
                    else{
                        val throwable = Throwable("No details found")
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