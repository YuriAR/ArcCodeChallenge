package br.ind.conceptu.tmdbupcoming.protocol

import android.graphics.Movie
import io.reactivex.Single

interface MoviesListProtocol {
    interface View {
        fun setLoadingPage(loading:Boolean)
        fun setLoadingMovies(loading:Boolean)
        fun updateMoviesList(movies:List<Movie>)
    }

    interface Presenter {
        fun getMoviesList(page:Int = 1)
    }

    interface NetworkHandler {
        fun getMoviesList(page:Int): Single<List<Movie>>
    }
}