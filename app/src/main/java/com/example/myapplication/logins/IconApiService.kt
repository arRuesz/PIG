package com.example.myapplication.logins
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface IconApiService {
    @GET("api/")
    fun getRandomIcons(
        @Query("gender") gender: String,
        @Query("results") results: Int = 3
    ): Call<IconApiResponse>
}