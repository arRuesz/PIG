package com.example.myapplication.logins

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName= "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val password: String,
    val imageUrl: String?
)