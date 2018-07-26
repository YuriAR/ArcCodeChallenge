package br.ind.conceptu.tmdbupcoming.network

import android.content.Context
import br.ind.conceptu.tmdbupcoming.network.handler.ServerConfigurationsNetworkHandler
import br.ind.conceptu.tmdbupcoming.persistance.SharedPreferencesManager
import com.google.gson.Gson
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.util.HashMap
import com.google.gson.reflect.TypeToken



object ServerContentManager {

    private const val baseURL = "https://api.themoviedb.org/3/"
    const val tmdbKey = "1f54bd990f1cdfb230adb312546d765d"

    private val networkHandler = ServerConfigurationsNetworkHandler()

    private var maxRetries = 5
    private var currentRetries = 0

    fun getBackdropSizes(context: Context):List<String>{
        val listType = object : TypeToken<List<String>>() {}.type
        val jsonArrayString = SharedPreferencesManager.getStaticContent(SharedPreferencesManager.StaticContentType.BACKDROP, context)
        if (!jsonArrayString.isEmpty()){
            return Gson().fromJson(jsonArrayString, listType)
        }
        return listOf()
    }

    fun getLogoSizes(context: Context):List<String>{
        val listType = object : TypeToken<List<String>>() {}.type
        val jsonArrayString = SharedPreferencesManager.getStaticContent(SharedPreferencesManager.StaticContentType.LOGO, context)
        if (!jsonArrayString.isEmpty()){
            return Gson().fromJson(jsonArrayString, listType)
        }
        return listOf()
    }

    fun getPosterSizes(context: Context):List<String>{
        val listType = object : TypeToken<List<String>>() {}.type
        val jsonArrayString = SharedPreferencesManager.getStaticContent(SharedPreferencesManager.StaticContentType.POSTER, context)
        if (!jsonArrayString.isEmpty()){
            return Gson().fromJson(jsonArrayString, listType)
        }
        return listOf()
    }

    fun getProfileSizes(context: Context):List<String>{
        val listType = object : TypeToken<List<String>>() {}.type
        val jsonArrayString = SharedPreferencesManager.getStaticContent(SharedPreferencesManager.StaticContentType.PROFILE, context)
        if (!jsonArrayString.isEmpty()){
            return Gson().fromJson(jsonArrayString, listType)
        }
        return listOf()
    }

    fun getStillSizes(context: Context):List<String>{
        val listType = object : TypeToken<List<String>>() {}.type
        val jsonArrayString = SharedPreferencesManager.getStaticContent(SharedPreferencesManager.StaticContentType.STILL, context)
        if (!jsonArrayString.isEmpty()){
            return Gson().fromJson(jsonArrayString, listType)
        }
        return listOf()
    }

    fun getBaseImageUrl(secure:Boolean, context: Context):String{
        return SharedPreferencesManager.getBaseImageUrl(secure, context)
    }

    fun syncAllStaticData(context: Context){
        syncConfigurations(context)
    }

    private fun syncConfigurations(context: Context){

    }

    fun getUrlWithPath(path: String, getParams: Map<String,String>?): String {
        var urlPath = path
        var url = baseURL

        if (urlPath.startsWith("/") && url.endsWith("/")) {
            urlPath = urlPath.substring(1)
        } else if (!url.endsWith("/") && !urlPath.startsWith("/")) {
            url += "/"
        }

        url += urlPath

        getParams?.let { params ->
            url += "?" + getParamsStringFromMap(params)
        }

        return url
    }

    fun getHeaders(isFile:Boolean = false): HashMap<String, String> {
        val headers = HashMap<String, String>()
//        headers["x-auth-token"] = "0Loic6nym4Xh83Ii0ee4587aPpvS5NfFDL5YsbQN"
//        headers["Accept"] = "application/json"
//        if (isFile){
//            headers["Content-Type"] = "multipart/form-data; boundary=---------------------------14737809831466499882746641449"
//        }
//        else{
//            headers["Content-Type"] = "application/json; charset=utf-8"
//        }
        return headers
    }

    fun getBodyFromMap(map: Map<String,Any>):ByteArray?{
        try {
            return JSONObject(map).toString().toByteArray(charset("utf-8"))
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return null
    }

    private fun getParamsStringFromMap(map: Map<String,String>):String{
        var counter = 0
        var result = ""
        for (param in map){
            result += param.key + "=" + param.value
            counter++

            if (counter != map.count()){
                result += "&"
            }
        }
        return result
    }
}