package com.example.myapplication.juegopig

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GameData (
    val playerNames : ArrayList<String>,
    val playerScores : ArrayList<Int>,
    val winner : String
):Parcelable