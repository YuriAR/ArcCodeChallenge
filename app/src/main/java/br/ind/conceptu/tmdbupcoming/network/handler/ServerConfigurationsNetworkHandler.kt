package br.ind.conceptu.tmdbupcoming.network.handler

import br.ind.conceptu.tmdbupcoming.App
import br.ind.conceptu.tmdbupcoming.network.ServerContentManager
import com.android.volley.AuthFailureError
import com.android.volley.toolbox.StringRequest
import io.reactivex.Single
import org.json.JSONObject

class ServerConfigurationsNetworkHandler {
    fun getConfigurations(): Single<JSONObject> {
        val path = "/configuration"

        val parameters = HashMap<String, String>()
        parameters["api_key"] = ServerContentManager.tmdbKey

        val url = ServerContentManager.getUrlWithPath(path, parameters)

        return Single.create { subscriber ->
            val request = object : StringRequest(Method.GET, url, { response ->
                Thread({
                    if (!response.isNullOrEmpty()){
                        val responseJson = JSONObject(response)
                        val hasData = responseJson.has("images")
                        if (hasData){
                            val imagesObject = responseJson.getJSONObject("images")
                            subscriber.onSuccess(imagesObject)
                        }
                        else{
                            val throwable = Throwable("No configurations found")
                            subscriber.onError(throwable)
                        }
                    }
                    else{
                        val throwable = Throwable("No configurations found")
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