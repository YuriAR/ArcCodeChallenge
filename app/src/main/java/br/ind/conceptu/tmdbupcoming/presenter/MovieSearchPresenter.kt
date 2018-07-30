package br.ind.conceptu.tmdbupcoming.presenter

import br.ind.conceptu.tmdbupcoming.network.handler.MovieSearchNetworkHandler
import br.ind.conceptu.tmdbupcoming.protocol.MovieSearchProtocol
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MovieSearchPresenter(val view:MovieSearchProtocol.View): MovieSearchProtocol.Presenter {

    private val networkHandler = MovieSearchNetworkHandler()

    override fun searchForMoviesWithString(query: String) {
        view.setLoadingSearch(true)
        networkHandler.searchForMoviesWithString(query).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.results.isEmpty()){
                        view.setLoadingSearch(false)
                        view.setEmptyView(true)
                        view.onSearchForMoviesSuccess(it)
                    }
                    else{
                        view.setLoadingSearch(false)
                        view.setEmptyView(false)
                        view.onSearchForMoviesSuccess(it)
                    }
                }, {
                    view.setLoadingSearch(false)
                    view.onSearchForMoviesFailure(query)
                })
    }
}