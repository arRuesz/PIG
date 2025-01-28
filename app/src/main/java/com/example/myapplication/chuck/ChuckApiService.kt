package com.example.myapplication.chuck

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ChuckApiService {
    @GET("jokes/random")
    fun getRandomJoke(@Query("category") category: String): Call<ChuckApiResponse>
}
