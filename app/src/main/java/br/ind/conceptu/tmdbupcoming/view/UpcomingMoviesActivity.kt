package br.ind.conceptu.tmdbupcoming.view

import android.content.Intent
import android.graphics.Movie
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import br.ind.conceptu.tmdbupcoming.R
import br.ind.conceptu.tmdbupcoming.network.ServerContentManager
import br.ind.conceptu.tmdbupcoming.presenter.MoviesListPresenter
import br.ind.conceptu.tmdbupcoming.protocol.MoviesListProtocol
import kotlinx.android.synthetic.main.activity_upcoming_movies.*

class UpcomingMoviesActivity : AppCompatActivity(), MoviesListProtocol.View {

    lateinit var presenter:MoviesListProtocol.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upcoming_movies)
        setSupportActionBar(toolbar)

        presenter = MoviesListPresenter(this,this)
        presenter.syncConfigurations()

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    override fun setLoadingMovies(loading: Boolean) {
        //TODO: Set loading whole view
    }

    override fun setLoadingPage(loading: Boolean) {
        //TODO: Add/remove loading cell at the bottom of recycler view
    }

    override fun onGetMoviesListSuccess(movies: List<Movie>) {

    }

    override fun onGetMoviesListFailure() {
        //TODO: Show error alert
    }

    override fun onConfigurationSuccess() {
        presenter.getMoviesList()
    }

    override fun onConfigurationFailure() {
        //TODO: Show error alert
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_upcoming_movies, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.action_about -> {
                startActivity(Intent(this, AboutActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
