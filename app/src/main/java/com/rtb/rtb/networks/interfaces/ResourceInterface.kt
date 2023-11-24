package com.rtb.rtb.networks.interfaces

import com.rtb.rtb.networks.dto.response.ResourceResponse
import retrofit2.Call
import retrofit2.http.GET

interface ResourceInterface {
    @GET("/api/resources")
    fun getResources(): Call<ResourceResponse>

}