package com.example.itunessearcher.db

import androidx.room.*

@Dao
interface TermMusicDao {
    @Query("SELECT * FROM TermMusicEntity WHERE term like :term")
    fun findByTerm(term: String): List<TermMusicDao>

    @Insert
    fun insert(vararg termMusicDao: TermMusicDao)

    @Insert
    fun insertAll(vararg termList: List<TermMusicDao>)

    @Delete
    fun delete(term: String)
}