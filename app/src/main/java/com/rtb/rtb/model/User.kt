package com.rtb.rtb.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.rtb.rtb.networks.dto.request.UserRequest

@Entity
class User(
    @PrimaryKey
    val email: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    val password: String,
)

fun User.toRequest(): UserRequest {
    return UserRequest(
        this.email,
        this.password,
        this.firstName,
        this.lastName,
    )
}