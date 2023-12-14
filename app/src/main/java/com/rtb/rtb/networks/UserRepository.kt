package com.rtb.rtb.networks

import com.rtb.rtb.networks.dto.request.UserRequest
import com.rtb.rtb.networks.dto.response.UserResponse
import com.rtb.rtb.networks.interfaces.UserInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository(apiService: ApiService) : BaseRepository() {
    val retrofit = apiService.instance
    val service = retrofit.create(UserInterface::class.java)

    fun login(body: UserRequest, callback: (Result<UserResponse?>) -> Unit) {
        val request = service.login(body)

        request.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                if (response.isSuccessful) {
                    callback.invoke(Result.Success(response.body()))
                } else {
                    callback.invoke(Result.Error("Error: ${response.errorBody()}"))
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                callback.invoke(Result.Error("Error: ${t.message}"))
            }
        })
    }

    fun signUp(body: UserRequest, callback: (Result<UserResponse?>) -> Unit) {
        val request = service.signUp(body)

        request.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                if (response.isSuccessful) {
                    callback.invoke(Result.Success(response.body()))
                } else {
                    callback.invoke(Result.Error("Error: ${response.errorBody()}"))
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                callback.invoke(Result.Error("Error: ${t.message}"))
            }
        })
    }
}