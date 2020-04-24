package com.example.itunessearcher.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

data class MusicList(
    val results: List<MusicTrack>
)

@Entity(tableName = "Terms", primaryKeys = ["term", "trackId"])
data class Term(
    val term: String,
    val trackId: Long
)

@Entity(tableName = "MusicTracks")
data class MusicTrack(
    @PrimaryKey val trackId: Long,
    val trackName: String,
    val artistName: String,
    val collectionName: String,
    val artworkUrl100: String,
    val trackTimeMillis: Int,
    val primaryGenreName: String,
    val releaseDate: String,
    val trackPrice: String
) : Serializable
