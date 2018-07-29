package br.ind.conceptu.tmdbupcoming.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.ind.conceptu.tmdbupcoming.R
import br.ind.conceptu.tmdbupcoming.model.Movie
import br.ind.conceptu.tmdbupcoming.network.ServerContentManager
import br.ind.conceptu.tmdbupcoming.persistance.SharedPreferencesManager
import br.ind.conceptu.tmdbupcoming.util.DateUtil
import br.ind.conceptu.tmdbupcoming.view.MovieDetailActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.movie_list_item.view.*

class MovieListAdapter(private var movies:MutableList<Movie>) : RecyclerView.Adapter<MovieListAdapter.ViewHolder>(){

    companion object {
        const val LOADING_PAGE_TYPE = 1
        const val MOVIE_TYPE = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == LOADING_PAGE_TYPE){
            ViewHolder(inflater.inflate(R.layout.loading_list_item, parent, false))
        }
        else{
            ViewHolder(inflater.inflate(R.layout.movie_list_item, parent, false))
        }
    }

    fun replaceAllMovies(movies: MutableList<Movie>) {
        this.movies = movies
        notifyDataSetChanged()
    }

    fun insertMovies(movies: MutableList<Movie>){
        val lastIndexBeforeInsertion = this.movies.size - 1
        this.movies.addAll(movies)
        notifyItemRangeChanged(lastIndexBeforeInsertion, this.movies.size)
    }

    fun setLoadingPage(loading:Boolean){
        if (loading){
            val dummyMovie = Movie()
            dummyMovie.loadingMovieDummy = true
            this.movies.add(dummyMovie)
            notifyItemInserted(this.movies.size)
        }
        else{
            val indexOfLoading = this.movies.indexOfFirst { it.loadingMovieDummy }
            if (indexOfLoading != -1){
                this.movies.removeAt(indexOfLoading)
                notifyItemRemoved(indexOfLoading)
            }
        }
    }

    override fun getItemCount() = movies.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(movies[position], holder.itemViewType)
    }

    override fun getItemViewType(position: Int): Int {
        return if (movies[position].loadingMovieDummy){
            LOADING_PAGE_TYPE
        }
        else{
            MOVIE_TYPE
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Movie, itemViewType:Int) = with(itemView) {
            if (itemViewType == MOVIE_TYPE){
                val imageUrl = SharedPreferencesManager.getBaseImageUrl(true, context)
                val allPostersImages = ServerContentManager.getPosterSizes(context)

                val completeImageUrl = imageUrl + allPostersImages.last() + item.poster_path

                val options = RequestOptions()
                        .fitCenter()
                        .placeholder(R.drawable.ic_film)

                Glide.with(context).load(completeImageUrl).apply(options).into(itemView.moviePoster)
                itemView.movieTitle.text = item.title
                itemView.movieRelease.text = DateUtil.formatServerDate(item.release_date)

                itemView.movieRating.rating = item.vote_average.toFloat()


                genreList.layoutManager = GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
                val genres = ServerContentManager.getGenres(context).filter { item.genre_ids.contains(it.id) }
                genreList.adapter = GenreAdapter(genres)

                val allBackdropImages = ServerContentManager.getBackdropSizes(context)
                val imageBackdropPath = imageUrl + allBackdropImages.last() + item.backdrop_path

                itemView.setOnClickListener {
                    val intent = Intent(context, MovieDetailActivity::class.java)
                    intent.putExtra("movieId", item.id)
                    intent.putExtra("movieTitle", item.title)
                    intent.putExtra("backdropImagePath", imageBackdropPath)
                    context.startActivity(intent)
                }
            }
        }
    }
}