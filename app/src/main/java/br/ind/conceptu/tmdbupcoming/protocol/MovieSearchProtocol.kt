package br.ind.conceptu.tmdbupcoming.protocol

import br.ind.conceptu.tmdbupcoming.model.MovieResult
import io.reactivex.Single

interface MovieSearchProtocol {
    interface View {
        fun setEmptyView(empty:Boolean)
        fun setLoadingSearch(loading:Boolean)
        fun onSearchForMoviesSuccess(result:MovieResult)
        fun onSearchForMoviesFailure(query: String)
    }

    interface Presenter {
        fun searchForMoviesWithString(query:String)
    }

    interface NetworkHandler {
        fun searchForMoviesWithString(query:String): Single<MovieResult>
    }
}