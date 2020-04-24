package com.example.itunessearcher.viewmodel

import android.os.Handler
import android.widget.Toast
import androidx.core.util.Consumer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.itunessearcher.App
import com.example.itunessearcher.model.DbPersistence
import com.example.itunessearcher.model.MusicList
import com.example.itunessearcher.model.Network
import com.example.itunessearcher.model.MusicTrack
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

class MusicViewModel : ViewModel() {

    private val baseApiUrl: String = "https://itunes.apple.com/"

    val searchNetwork = MutableLiveData<List<MusicTrack>>()
    val networkError = MutableLiveData<Int>()
    val searchedTerm = MutableLiveData<String> ()

//    val consumerCache = Consumer<MutableList<MusicTrack>> {  }

    fun getTracks(term: String, handler: Handler) {
        DbPersistence.retrieveCacheSearch(term, Consumer<MutableList<MusicTrack>> {
            if(it.size > 0) {
                handler.post(
                    Runnable {
                        Toast.makeText(App.context, "showing cache music tracks", Toast.LENGTH_LONG).show()
                        searchNetwork.value = it
                    }
                )
            } else {
                handler.post(
                    Runnable {
                        Toast.makeText(App.context, "showing music tracks from API", Toast.LENGTH_LONG).show()
                        getTracksFromAPI(term)
                    }
                )
            }
        })
    }

    fun getTracksFromAPI(term: String) {
        val network = Network(baseApiUrl)
        network.initRetrofit().getSearchResults(term, "song")
            .enqueue(object : Callback<MusicList> {
                override fun onResponse(
                    call: Call<MusicList>,
                    response: Response<MusicList>
                ) {
                    if(response.isSuccessful) {
                        val musicList = response.body()
                        searchNetwork.value = musicList?.results
                    } else {
                        networkError.value = response.code()
                    }
                }
                override fun onFailure(call: Call<MusicList>, t: Throwable) {
                    t.printStackTrace()
                    networkError.value = HttpURLConnection.HTTP_BAD_REQUEST
                }
            })
    }

}
