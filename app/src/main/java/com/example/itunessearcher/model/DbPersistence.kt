package com.example.itunessearcher.model

import android.os.Looper
import androidx.core.util.Consumer
import androidx.room.*
import com.example.itunessearcher.App
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

val DatabaseName = "iTunesSearcherDatabase"

object DbPersistence {
    val db = Room.databaseBuilder(
        App.context, MusicTrackDatabase::class.java, DatabaseName
    ).build()

    fun persist(term: String?, musicTrackList: List<MusicTrack>) {
        if(term == null) return
        val termsList : MutableList<Term> = musicTrackList.map{ Term(term, it.trackId) }.toMutableList()

        GlobalScope.launch {
            musicTrackList.forEach{
                db.termsDao().insertMusicTrack(
                    it.trackId, it.trackName, it.artistName, it.collectionName, it.artworkUrl100, it.trackTimeMillis, it.primaryGenreName, it.releaseDate, it.trackPrice?:"0.0"
                )
            }
            termsList.forEach{
                db.termsDao().insertTerm(it.term, it.trackId)
            }
        }
    }

    fun retrieveCacheSearch(term: String, musicTrackConsumer: Consumer<MutableList<MusicTrack>>) {
        GlobalScope.launch {
            val musicTrackList = db.termsDao().retrieveMusicTrackList(term)
            musicTrackConsumer.accept(musicTrackList)
        }
    }

    fun clearCache() {
        GlobalScope.launch {
            db.termsDao().deleteAllTerms()
            db.termsDao().deleteAllMusicTracks()
        }
    }
}

@Dao
interface TermsDao {
    @Query("REPLACE INTO Terms VALUES (:term, :trackId)")
    fun insertTerm(term : String, trackId : Long)

    @Query("REPLACE INTO MusicTracks VALUES (:trackId, :trackName, :artistName, :collectionName, :artworkUrl100, :trackTimeMillis, :primaryGenreName, :releaseDate, :trackPrice)")
    fun insertMusicTrack(
        trackId: Long,
        trackName: String,
        artistName: String,
        collectionName: String,
        artworkUrl100: String,
        trackTimeMillis: Int,
        primaryGenreName: String,
        releaseDate: String,
        trackPrice: String
    )

    @Query("SELECT trackId FROM Terms WHERE term LIKE :searchTerm")
    fun findTrackId(searchTerm : String): MutableList<Long>

    @Query("SELECT * FROM MusicTracks INNER JOIN Terms ON MusicTracks.trackId = Terms.trackId WHERE Terms.term = :term")
    fun retrieveMusicTrackList(term: String): MutableList<MusicTrack>

    @Query("DELETE FROM Terms")
    fun deleteAllTerms()

    @Query("DELETE FROM MusicTracks")
    fun deleteAllMusicTracks();
}

@Database(entities = arrayOf(Term::class, MusicTrack::class), version = 1)
abstract class MusicTrackDatabase : RoomDatabase() {
    abstract fun termsDao() : TermsDao
}