package br.ind.conceptu.tmdbupcoming.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import br.ind.conceptu.tmdbupcoming.R
import br.ind.conceptu.tmdbupcoming.adapter.GenreAdapter
import br.ind.conceptu.tmdbupcoming.model.MovieDetails
import br.ind.conceptu.tmdbupcoming.network.ServerContentManager
import br.ind.conceptu.tmdbupcoming.persistance.SharedPreferencesManager
import br.ind.conceptu.tmdbupcoming.presenter.MovieDetailsPresenter
import br.ind.conceptu.tmdbupcoming.protocol.MovieDetailsProtocol
import br.ind.conceptu.tmdbupcoming.util.DateUtil
import br.ind.conceptu.tmdbupcoming.util.DialogUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_movie_detail.*

class MovieDetailActivity : AppCompatActivity(), MovieDetailsProtocol.View {

    var movieId = 0
    var movieTitle = ""
    var backdropImagePath = ""

    private lateinit var presenter:MovieDetailsProtocol.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        setSupportActionBar(toolbar)

        movieId = intent.getIntExtra("movieId", 0)
        movieTitle = intent.getStringExtra("movieTitle")
        backdropImagePath = intent.getStringExtra("backdropImagePath")

        supportActionBar?.title = movieTitle
        val options = RequestOptions()
                .fitCenter()
                .placeholder(R.drawable.home_header_image)
        Glide.with(this).load(backdropImagePath).apply(options).into(movieBackdropImage)

        presenter = MovieDetailsPresenter(this, this)
        presenter.getMovieDetails(movieId)
    }

    override fun setLoadingDetails(loading: Boolean) {
        detailsView.visibility = if (loading) View.GONE else View.VISIBLE
        loadingView.visibility = if (loading) View.VISIBLE else View.GONE
    }

    override fun onMovieDetailsSuccess(details: MovieDetails) {
        movieTagline.text = details.tagline

        val imageUrl = SharedPreferencesManager.getBaseImageUrl(true, this)
        val allPostersImages = ServerContentManager.getPosterSizes(this)
        val posterImageUrl = imageUrl + allPostersImages.last() + details.poster_path

        val options = RequestOptions()
                .fitCenter()
                .placeholder(R.drawable.ic_film)
        Glide.with(this).load(posterImageUrl).apply(options).into(moviePoster)

        movieRelease.text = DateUtil.formatServerDate(details.release_date)
        movieRating.rating = details.vote_average.toFloat()

        voteCount.text = "(${details.vote_count})"

        genreList.layoutManager = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
        genreList.adapter = GenreAdapter(details.genres)

        movieOverview.text = details.overview
    }

    override fun onMovieDetailsFailure() {
        DialogUtils.showDialogWithDoubleAction(
                this,
                getString(R.string.error),
                getString(R.string.details_loading_error),
                getString(R.string.retry),
                getString(R.string.cancel),
                Runnable {
                    //Positive action
                    presenter.getMovieDetails(movieId)
                },
                null)
    }
}
