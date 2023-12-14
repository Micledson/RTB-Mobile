package com.rtb.rtb.networks.interfaces

import com.rtb.rtb.networks.dto.request.UserRequest
import com.rtb.rtb.networks.dto.response.UserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserInterface {
    @POST("/api/auth/login")
    fun login(@Body body: UserRequest): Call<UserResponse>

    @POST("/api/auth/sign-up")
    fun signUp(@Body body: UserRequest): Call<UserResponse>
}