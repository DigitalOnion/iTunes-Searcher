package com.example.itunessearcher.model

import com.google.gson.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type

class Network(var url: String) {
    fun initRetrofit(): RetrofitEndpoint {

        val gson: Gson = GsonBuilder()
            .setLenient()
            .registerTypeAdapterFactory(BestNumberTypeAdapterFactory.getBestNumberTypeAdapterFactory())
            .create()

        var retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return retrofit.create(RetrofitEndpoint::class.java)
    }
}