package com.example.myapplication.logins

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    fun insertarUsuario(user: User)

    @Query("SELECT * FROM users WHERE name= :name")
    fun getUsersByName(name: String): User?

    @Query("SELECT * FROM users WHERE name = :name LIMIT 1")
    fun getUsuarioPorNombre(name: String): User?

    @Query("SELECT imageUrl FROM users WHERE name = :name")
    fun getUrl(name: String): String?

}