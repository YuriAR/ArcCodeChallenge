package br.ind.conceptu.tmdbupcoming.protocol

import br.ind.conceptu.tmdbupcoming.model.Movie
import br.ind.conceptu.tmdbupcoming.model.MovieResult
import io.reactivex.Single

interface MoviesListProtocol {
    interface View {
        fun setLoadingPage(loading:Boolean)
        fun setLoadingMovies(loading:Boolean)
        fun onGetStartingMoviesSuccess(result:MovieResult)
        fun onGetStartingMoviesFailure()
        fun onGetMoviesPageSuccess(movies:List<Movie>, page:Int)
        fun onGetMoviesPageFailure(page:Int)
        fun onConfigurationSuccess()
        fun onConfigurationFailure()
        fun onSyncGenreSuccess()
        fun onSyncGenreFailure()
    }

    interface Presenter {
        fun getStartingMovies()
        fun getMoviesPage(page: Int)
        fun syncConfigurations()
        fun syncGenres()
    }

    interface NetworkHandler {
        fun getMoviesList(page:Int): Single<MovieResult>
    }
}