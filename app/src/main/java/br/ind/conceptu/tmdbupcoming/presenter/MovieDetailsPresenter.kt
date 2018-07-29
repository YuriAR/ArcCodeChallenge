package br.ind.conceptu.tmdbupcoming.presenter

import android.content.Context
import br.ind.conceptu.tmdbupcoming.network.handler.MovieDetailsNetworkHandler
import br.ind.conceptu.tmdbupcoming.protocol.MovieDetailsProtocol
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MovieDetailsPresenter(private val view:MovieDetailsProtocol.View, private val context: Context):MovieDetailsProtocol.Presenter {

    private val networkHandler = MovieDetailsNetworkHandler()

    override fun getMovieDetails(movieId: Int) {
        view.setLoadingDetails(true)
        networkHandler.getMovieDetails(movieId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view.setLoadingDetails(false)
                    view.onMovieDetailsSuccess(it)
                }, {
                    view.setLoadingDetails(false)
                    view.onMovieDetailsFailure()
                })
    }
}