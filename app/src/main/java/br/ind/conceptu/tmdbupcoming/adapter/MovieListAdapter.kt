package br.ind.conceptu.tmdbupcoming.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.ind.conceptu.tmdbupcoming.R
import br.ind.conceptu.tmdbupcoming.model.Movie
import br.ind.conceptu.tmdbupcoming.persistance.SharedPreferencesManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.movie_list_item.view.*

class MovieListAdapter(var movies:MutableList<Movie>) : RecyclerView.Adapter<MovieListAdapter.ViewHolder>(){

    companion object {
        const val LOADING_PAGE_TYPE = 1
        const val MOVIE_TYPE = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.movie_list_item, parent, false))
    }

    fun replaceAllMovies(movies: MutableList<Movie>) {
        this.movies = movies
        notifyDataSetChanged()
    }

    fun insertMovies(movies: MutableList<Movie>){
        val lastIndexBeforeInsertion = this.movies.lastIndex
        this.movies.addAll(movies)
        notifyItemRangeInserted(lastIndexBeforeInsertion, this.movies.lastIndex)
    }

    fun setLoadingPage(loading:Boolean){
        if (loading){
            val dummyMovie = Movie()
            dummyMovie.loadingMovieDummy = true
            this.movies.add(dummyMovie)
            notifyItemInserted(this.movies.lastIndex)
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
            if (itemViewType == LOADING_PAGE_TYPE){

            }
            else{
                val imageUrl = SharedPreferencesManager.getBaseImageUrl(true, context)
                val allPostersImages = SharedPreferencesManager.getStaticContent(SharedPreferencesManager.StaticContentType.POSTER, context)

                val completeImageUrl = imageUrl + allPostersImages.last() //+ item.imagePath...

                val options = RequestOptions()
                        .fitCenter()
                        .placeholder(R.drawable.ic_film)

                Glide.with(context).load(completeImageUrl).apply(options).into(itemView.moviePoster)
                itemView.movieTitle.text //= item.title
                itemView.movieRelease
            }
        }
    }
}