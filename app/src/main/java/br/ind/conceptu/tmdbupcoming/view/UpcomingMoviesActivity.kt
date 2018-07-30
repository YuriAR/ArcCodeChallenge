package br.ind.conceptu.tmdbupcoming.view

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import br.ind.conceptu.tmdbupcoming.R
import br.ind.conceptu.tmdbupcoming.adapter.MovieListAdapter
import br.ind.conceptu.tmdbupcoming.model.Movie
import br.ind.conceptu.tmdbupcoming.model.MovieResult
import br.ind.conceptu.tmdbupcoming.presenter.MoviesListPresenter
import br.ind.conceptu.tmdbupcoming.protocol.MoviesListProtocol
import br.ind.conceptu.tmdbupcoming.util.DialogUtils
import kotlinx.android.synthetic.main.activity_upcoming_movies.*

class UpcomingMoviesActivity : AppCompatActivity(), MoviesListProtocol.View {

    private var currentPage = 1
    private var lastPage = 0

    lateinit var presenter: MoviesListProtocol.Presenter
    lateinit var moviesAdapter: MovieListAdapter

    private var loadingPage = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upcoming_movies)
        setSupportActionBar(toolbar)

        supportActionBar?.title = getString(R.string.upcoming_movies)

        presenter = MoviesListPresenter(this,this)
        presenter.syncConfigurations()

        moviesList.layoutManager = LinearLayoutManager(this)
        moviesAdapter = MovieListAdapter(mutableListOf())
        moviesList.adapter = moviesAdapter

        moviesList.addOnScrollListener( object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (loadingPage || currentPage > lastPage) return
                val layoutManager = moviesList.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        && totalItemCount >= PAGE_SIZE) {
                    loadingPage = true
                    presenter.getMoviesPage(currentPage + 1)
                }
            }
        })
    }

    override fun setLoadingMovies(loading: Boolean) {
        loadingView.visibility = if (loading) View.VISIBLE else View.GONE
        moviesList.visibility = if (loading) View.GONE else View.VISIBLE
    }

    override fun setLoadingPage(loading: Boolean) {
        if (loading != loadingPage){
            loadingPage = loading
            moviesAdapter.setLoadingPage(loading)
        }
    }

    override fun onGetStartingMoviesSuccess(result: MovieResult) {
        lastPage = result.total_pages
        moviesAdapter.replaceAllMovies(result.results.toMutableList())
    }

    override fun onGetStartingMoviesFailure() {
        DialogUtils.showRetryDialogWithAction(this,
                getString(R.string.error),
                getString(R.string.movies_loading_error),
                Runnable {
                    //Positive action
                    presenter.getStartingMovies()
                })
    }

    override fun onGetMoviesPageSuccess(movies: List<Movie>, page: Int) {
        currentPage = page
        moviesAdapter.insertMovies(movies.toMutableList())
    }

    override fun onGetMoviesPageFailure(page: Int) {
        DialogUtils.showRetryDialogWithAction(this,
                getString(R.string.error),
                getString(R.string.movies_loading_error),
                Runnable {
                    //Positive action
                    presenter.getMoviesPage(page)
                })
    }

    override fun onConfigurationSuccess() {
        presenter.syncGenres()
    }

    override fun onConfigurationFailure() {
        DialogUtils.showRetryDialogWithAction(this,
                getString(R.string.error),
                getString(R.string.configuration_error),
                Runnable {
                    //Positive action
                    presenter.syncConfigurations()
                })
    }

    override fun onSyncGenreSuccess() {
        presenter.getStartingMovies()
    }

    override fun onSyncGenreFailure() {
        DialogUtils.showRetryDialogWithAction(this,
                getString(R.string.error),
                getString(R.string.configuration_error),
                Runnable {
                    //Positive action
                    presenter.syncGenres()
                })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_upcoming_movies, menu)

        for (i in 0 until menu.size()) {
            val drawable = menu.getItem(i).icon
            if (drawable != null) {
                drawable.mutate()
                drawable.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP)
            }
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.action_about -> {
                startActivity(Intent(this, AboutActivity::class.java))
                return true
            }
            R.id.action_search -> {
                startActivity(Intent(this, SearchMovieActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val PAGE_SIZE = 20
    }
}
