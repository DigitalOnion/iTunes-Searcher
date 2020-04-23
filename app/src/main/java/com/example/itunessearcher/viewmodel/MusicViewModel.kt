package com.example.itunessearcher.viewmodel

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.itunessearcher.App
import com.example.itunessearcher.model.MusicList
import com.example.itunessearcher.model.Network
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MusicViewModel : ViewModel() {

    private val baseApiUrl: String = " https://itunes.apple.com/"

    private val searchResults = MutableLiveData<MusicList>()
    fun getSearchResults(): LiveData<MusicList> = searchResults

    private val searchDBResults = MutableLiveData<MusicList>()
    fun getSearchDBResults(): LiveData<MusicList> = searchDBResults

    fun getTracksFromDB(term: String) {

    }


    private val searchNetworkResults = MutableLiveData<MusicList>()
    fun getSearchNetworkResults(): LiveData<MusicList> = searchNetworkResults

    fun getTracksFromAPI(term: String) {
        val network = Network(baseApiUrl)
        network.initRetrofit().getSearchResults(term, "song")
            .enqueue(object : Callback<MusicList> {
                override fun onResponse(
                    call: Call<MusicList>,
                    response: Response<MusicList>
                ) {
                    searchNetworkResults.value = response.body()
                }
                override fun onFailure(call: Call<MusicList>, t: Throwable) {
                    t.printStackTrace()
                    Toast.makeText(App.context, t.toString(), Toast.LENGTH_LONG).show()
                }
            })
    }
}
