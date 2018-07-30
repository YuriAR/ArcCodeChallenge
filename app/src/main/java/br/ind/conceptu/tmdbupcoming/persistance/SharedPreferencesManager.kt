package br.ind.conceptu.tmdbupcoming.persistance

import android.content.Context
import android.preference.PreferenceManager

object SharedPreferencesManager {

    enum class StaticContentType(val key:String) {
        LOGO(logoSizesComplementKey),
        POSTER(posterSizesComplementKey),
        PROFILE(profileSizesComplementKey),
        STILL(stillSizesComplementKey),
        BACKDROP(backdropSizesComplementKey),
        GENRE(genreComplementKey)
    }

    private const val imageUrlKey = "IMAGE_URL_"
    private const val imageUrlSafeComplementKey = "HTTPS"
    private const val imageUrlUnsafeComplementKey = "HTTP"

    private const val staticContentKey = "STATIC_CONTENT_"
    private const val logoSizesComplementKey = "LOGO_SIZES"
    private const val posterSizesComplementKey = "POSTER_SIZES"
    private const val profileSizesComplementKey = "PROFILE_SIZES"
    private const val stillSizesComplementKey = "STILL_SIZES"
    private const val backdropSizesComplementKey = "BACKDROP_SIZES"
    private const val genreComplementKey = "GENRE"

    fun setStaticContent(contentType:StaticContentType, content:String, context: Context){
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putString(staticContentKey + contentType.key, content)
        editor.apply()
    }

    fun getStaticContent(contentType:StaticContentType, context: Context): String{
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getString(staticContentKey + contentType.key, "")
    }

    fun setBaseImageUrl(secure:Boolean, url:String, context: Context){
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        if (secure) editor.putString(imageUrlKey + imageUrlSafeComplementKey, url) else editor.putString(imageUrlKey + imageUrlUnsafeComplementKey, url)
        editor.apply()
    }

    fun getBaseImageUrl(secure:Boolean, context: Context):String{
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return if (secure) preferences.getString(imageUrlKey + imageUrlSafeComplementKey, "") else preferences.getString(imageUrlKey + imageUrlUnsafeComplementKey, "")
    }

    fun clearAllData(context: Context){
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.clear().apply()
    }
}