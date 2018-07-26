package br.ind.conceptu.tmdbupcoming.protocol

import android.graphics.Movie
import io.reactivex.Single

interface MoviesListProtocol {
    interface View {
        fun setLoadingPage(loading:Boolean)
        fun setLoadingMovies(loading:Boolean)
        fun onGetMoviesListSuccess(movies:List<Movie>)
        fun onGetMoviesListFailure()
        fun onConfigurationSuccess()
        fun onConfigurationFailure()
    }

    interface Presenter {
        fun getMoviesList(page:Int = 1)
        fun syncConfigurations()
    }

    interface NetworkHandler {
        fun getMoviesList(page:Int): Single<List<Movie>>
    }
}