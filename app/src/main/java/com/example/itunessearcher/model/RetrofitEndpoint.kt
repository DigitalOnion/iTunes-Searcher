package com.example.itunessearcher.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitEndpoint {

    @GET("search")
    fun getSearchResults(
        @Query("term") searchTerm: String,
        @Query("entity") type: String
    ): Call<MusicList>
}