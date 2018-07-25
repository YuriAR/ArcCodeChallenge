package br.ind.conceptu.tmdbupcoming.protocol

import br.ind.conceptu.tmdbupcoming.model.MovieDetails
import io.reactivex.Single

interface MovieDetailsProtocol {
    interface View {
        fun setLoadingDetails(loading:Boolean)
        fun updateMovieDetails(details:MovieDetails)
    }

    interface Presenter {
        fun getMovieDetails(movieId:Int)
    }

    interface NetworkHandler {
        fun getMovieDetails(movieId:Int):Single<MovieDetails>
    }
}