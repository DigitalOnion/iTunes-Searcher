package com.example.itunessearcher.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.itunessearcher.R
import com.example.itunessearcher.model.MusicTrack
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail_view.view.*
import kotlinx.android.synthetic.main.search_item_layout.view.*
import kotlinx.android.synthetic.main.search_item_layout.view.tv_detail_album_name

class SearchListAdapter(val dataSet: List<MusicTrack>, val clickListener: (MusicTrack) -> Unit) :
    RecyclerView.Adapter<SearchListAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder =
        CustomViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.search_item_layout,
                    parent,
                    false
                )
        )

    override fun getItemCount(): Int = dataSet.size

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.onBind(dataSet, position, clickListener)
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind(data: List<MusicTrack>, position: Int, clickListener: (MusicTrack) -> Unit) {
            Picasso.get().load(data[position].artworkUrl100).resize(200, 200)
                .into(itemView.iv_album_art)
            itemView.tv_track_name.text = data[position].trackName
            itemView.tv_detail_album_name.text = data[position].collectionName
            itemView.tv_artist_name.text = data[position].artistName
            itemView.tv_detail_track_price?.text = data[position].trackPrice
            itemView.setOnClickListener { clickListener(data[position]) }
        }
    }
}