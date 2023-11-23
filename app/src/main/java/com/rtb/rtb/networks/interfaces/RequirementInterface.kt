package com.rtb.rtb.networks.interfaces

import com.rtb.rtb.networks.dto.request.RequirementRequest
import com.rtb.rtb.networks.dto.response.RequirementResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.UUID

interface RequirementInterface {
    @GET("/api/requirements")
    fun getRequirements(
        @Query("projectID") projectId: Int? = null
    ): Call<List<RequirementResponse>>


    @POST("/api/requirements")
    fun createRequirement(
       @Body body: RequirementRequest
    ): Call<Void>

    @GET("/api/requirements/{id}")
    fun getRequirementById(
        @Path("id") id: UUID
    ): Call<RequirementResponse>

    @PUT("/api/requirements/{id}")
    fun updateRequirement(
        @Path("id") id: UUID,
        @Body body: RequirementRequest
    ): Call<Void>

    @DELETE("/api/requirements/{id}")
    fun deleteRequirement(
        @Path("id") id: UUID
    ): Call<Void>


}