package br.ind.conceptu.tmdbupcoming.view

import android.content.Intent
import android.graphics.PorterDuff
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import br.ind.conceptu.tmdbupcoming.R
import br.ind.conceptu.tmdbupcoming.adapter.MovieListAdapter
import br.ind.conceptu.tmdbupcoming.model.MovieResult
import br.ind.conceptu.tmdbupcoming.presenter.MovieSearchPresenter
import br.ind.conceptu.tmdbupcoming.protocol.MovieSearchProtocol
import br.ind.conceptu.tmdbupcoming.util.DialogUtils
import kotlinx.android.synthetic.main.activity_search_movie.*

class SearchMovieActivity : AppCompatActivity(), SearchView.OnQueryTextListener, MovieSearchProtocol.View {

    private var searchView:SearchView? = null
    private lateinit var presenter:MovieSearchProtocol.Presenter
    private lateinit var moviesAdapter: MovieListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_movie)
        supportActionBar?.title = getString(R.string.search_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        presenter = MovieSearchPresenter(this)

        searchResultList.layoutManager = LinearLayoutManager(this)
        moviesAdapter = MovieListAdapter(mutableListOf())
        searchResultList.adapter = moviesAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search_movies, menu)

        val searchItem = menu.findItem(R.id.searchBar)
        searchView = searchItem?.actionView as SearchView

        searchView?.setOnQueryTextListener(this)

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
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun setLoadingSearch(loading: Boolean) {
        emptyView.visibility = View.GONE
        searchResultList.visibility = if (loading) View.GONE else View.VISIBLE
        loadingView.visibility = if (loading) View.VISIBLE else View.GONE
    }

    override fun setEmptyView(empty: Boolean) {
        loadingView.visibility = View.GONE
        emptyView.visibility = if (empty) View.VISIBLE else View.GONE
        loadingView.visibility = if (empty) View.GONE else View.VISIBLE
    }

    override fun onSearchForMoviesSuccess(result: MovieResult) {
        moviesAdapter.replaceAllMovies(result.results.toMutableList())
    }

    override fun onSearchForMoviesFailure(query: String) {
        DialogUtils.showRetryDialogWithAction(this,
                getString(R.string.error),
                getString(R.string.search_loading_error),
                Runnable {
                    //Positive action
                    presenter.searchForMoviesWithString(query)
                })
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let {
            presenter.searchForMoviesWithString(it)
            return true
        }
        return false
    }
}
