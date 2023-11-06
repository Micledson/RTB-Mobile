package com.rtb.rtb.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rtb.rtb.model.User

@Dao
abstract class UserDao {
    @Query("SELECT * FROM user WHERE email = :email AND password = :password")
    abstract fun authenticate(email : String, password : String) : User?

    @Insert
    abstract fun save(user : User): Long
}