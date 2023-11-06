package com.rtb.rtb.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class User(
    @PrimaryKey
    val email: String,
    val firstName: String,
    val lastName: String,
    val password: String,
)