package com.example.itunessearcher.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.itunessearcher.R
import com.example.itunessearcher.model.MusicList
import com.example.itunessearcher.model.POKOMusicTrack
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail_view.view.*
import kotlinx.android.synthetic.main.search_item_layout.view.*
import kotlinx.android.synthetic.main.search_item_layout.view.tv_detail_album_name

class SearchListAdapter(val dataSet: MusicList, val clickListener: (POKOMusicTrack) -> Unit) :
    RecyclerView.Adapter<SearchListAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder =
        CustomViewHolder(LayoutInflater
            .from(parent.context)
            .inflate(R.layout.search_item_layout, parent, false))

    override fun getItemCount(): Int = dataSet.results.size

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.onBind(dataSet, position, clickListener)
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind(data: MusicList, position: Int, clickListener: (POKOMusicTrack) -> Unit) {
            Picasso.get().load(data.results[position].artworkUrl100).resize(200, 200)
                .into(itemView.iv_album_art)
            itemView.tv_track_name.text = data.results[position].trackName
            itemView.tv_detail_album_name.text = data.results[position].collectionName
            itemView.tv_artist_name.text = data.results[position].artistName
            itemView.tv_detail_track_price?.text = data.results[position].trackPrice
            itemView.setOnClickListener { clickListener(data.results[position]) }
        }
    }
}