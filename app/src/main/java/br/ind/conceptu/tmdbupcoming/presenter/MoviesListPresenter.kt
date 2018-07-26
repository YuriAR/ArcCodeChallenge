package br.ind.conceptu.tmdbupcoming.presenter

import android.content.Context
import br.ind.conceptu.tmdbupcoming.network.handler.MoviesListNetworkHandler
import br.ind.conceptu.tmdbupcoming.network.handler.ServerConfigurationsNetworkHandler
import br.ind.conceptu.tmdbupcoming.persistance.SharedPreferencesManager
import br.ind.conceptu.tmdbupcoming.protocol.MoviesListProtocol
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MoviesListPresenter(private val view:MoviesListProtocol.View, private val context: Context):MoviesListProtocol.Presenter {
    private val configurationsNetworkHandler = ServerConfigurationsNetworkHandler()
    private val moviesListNetworkHandler = MoviesListNetworkHandler()

    override fun getMoviesList(page: Int) {
        moviesListNetworkHandler.getMoviesList(page).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (page == 1){
                        view.setLoadingMovies(false)
                    }
                    else{
                        view.setLoadingPage(false)
                    }
                    view.onGetMoviesListSuccess(it)
                }, {
                    view.onGetMoviesListFailure()
                })
    }

    override fun syncConfigurations() {
        configurationsNetworkHandler.getConfigurations().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val baseImageUrl = it.getString("base_url")
                    val baseSecureImageUrl = it.getString("secure_base_url")
                    val backdropSizes = it.getJSONArray("backdrop_sizes")
                    val logoSizes = it.getJSONArray("logo_sizes")
                    val posterSizes = it.getJSONArray("poster_sizes")
                    val profileSizes = it.getJSONArray("profile_sizes")
                    val stillSizes = it.getJSONArray("still_sizes")

                    SharedPreferencesManager.setBaseImageUrl(false, baseImageUrl, context)
                    SharedPreferencesManager.setBaseImageUrl(true, baseSecureImageUrl, context)

                    SharedPreferencesManager.setStaticContent(SharedPreferencesManager.StaticContentType.BACKDROP, backdropSizes.toString(), context)
                    SharedPreferencesManager.setStaticContent(SharedPreferencesManager.StaticContentType.LOGO, logoSizes.toString(), context)
                    SharedPreferencesManager.setStaticContent(SharedPreferencesManager.StaticContentType.POSTER, posterSizes.toString(), context)
                    SharedPreferencesManager.setStaticContent(SharedPreferencesManager.StaticContentType.PROFILE, profileSizes.toString(), context)
                    SharedPreferencesManager.setStaticContent(SharedPreferencesManager.StaticContentType.STILL, stillSizes.toString(), context)

                    view.onConfigurationSuccess()
                }, {
                    view.onConfigurationFailure()
                })
    }
}