package br.ind.conceptu.tmdbupcoming

import android.app.Application
import android.support.v7.app.AppCompatDelegate
import android.text.TextUtils
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class App: Application() {
    private var requestQueue: RequestQueue? = null

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    fun getRequestQueue(): RequestQueue? {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(applicationContext)
        }
        return requestQueue
    }

    fun <T> addToRequestQueue(req: Request<T>, tag: String) {
        req.tag = if (TextUtils.isEmpty(tag)) TAG else tag
        getRequestQueue()?.add(req)
    }

    fun <T> addToRequestQueue(req: Request<T>) {
        req.tag = TAG
        getRequestQueue()?.add(req)
    }

    fun cancelPendingRequests(tag: Any) {
        if (requestQueue != null) {
            requestQueue?.cancelAll(tag)
        }
    }

    companion object {

        val TAG = App::class.java.simpleName

        @get:Synchronized
        var instance: App? = null
            private set
    }
}