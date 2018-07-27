package br.ind.conceptu.tmdbupcoming.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.ind.conceptu.tmdbupcoming.R
import br.ind.conceptu.tmdbupcoming.model.Genre
import kotlinx.android.synthetic.main.genre_list_item.view.*

class GenreAdapter(var genres:List<Genre>) : RecyclerView.Adapter<GenreAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.genre_list_item, parent, false))
    }

    fun replaceAllGenres(genres: List<Genre>) {
        this.genres = genres
        notifyDataSetChanged()
    }

    override fun getItemCount() = genres.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(genres[position])
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Genre) = with(itemView) {
            itemView.genreName.text = item.name
        }
    }
}