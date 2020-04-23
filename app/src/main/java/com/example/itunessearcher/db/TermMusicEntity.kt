package com.example.itunessearcher.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.itunessearcher.model.POKOMusicTrack

@Entity
data class TermMusicEntity(
    @PrimaryKey(autoGenerate = true) val termId: Int,
    @ColumnInfo(name = "term") var term: String,
    @ColumnInfo(name = "track") var track: POKOMusicTrack)

