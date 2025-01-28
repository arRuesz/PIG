package com.example.myapplication.logins

data class IconApiResponse (
    val results: List<Result>
){
    data class Result(
        val gender: String,
        val picture: Picture
    )
    data class Picture(
        val large: String
    )
}
