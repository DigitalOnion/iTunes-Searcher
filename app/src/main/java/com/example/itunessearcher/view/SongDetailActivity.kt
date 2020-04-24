package com.example.itunessearcher.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.itunessearcher.R
import com.example.itunessearcher.model.MusicTrack
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail_view.*
import java.util.concurrent.TimeUnit

class SongDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_view)

        val track: MusicTrack = intent.extras.getSerializable("track") as MusicTrack

        Picasso.get().load(track.artworkUrl100).resize(800, 800)
            .into(iv_detail_album_art)
        tv_detail_track_name.text = track.trackName
        tv_detail_album_name.text = track.collectionName
        tv_detail_artist_name.text = track.artistName
        tv_detail_genre.text = getString(R.string.genre) + track.primaryGenreName
        tv_detail_length.text =
            getString(R.string.duration) + millisToMinutes(track.trackTimeMillis)
        tv_detail_releasedate.text =
            getString(R.string.released) + track.releaseDate.substring(0, 10)
        tv_detail_track_price.text = "$${track.trackPrice}"
    }

    fun millisToMinutes(millis: Int): String {
        var millisLong = millis.toLong()

        var timeHours = TimeUnit.MILLISECONDS.toHours(millisLong)
        var timeMins =
            TimeUnit.MILLISECONDS.toMinutes(millisLong) - TimeUnit.HOURS.toMinutes(timeHours)
        var timeSecs =
            TimeUnit.MILLISECONDS.toSeconds(millisLong) - TimeUnit.MINUTES.toSeconds(timeMins)

        return String.format(
            "%02d:%02d:%02d",
            timeHours, timeMins, timeSecs
        )
    }
}
