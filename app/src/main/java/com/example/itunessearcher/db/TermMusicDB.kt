package com.example.itunessearcher.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(TermMusicEntity::class), version = 1)
abstract class TermMusicDB : RoomDatabase() {
    abstract fun termMusicDao() : TermMusicDao
}
