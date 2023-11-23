package com.rtb.rtb.networks.interfaces

import com.rtb.rtb.networks.dto.request.ProjectRequest
import com.rtb.rtb.networks.dto.response.ProjectResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.util.UUID

interface ProjectInterface {
    @GET("/api/projects")
    fun getProjects(
    ): Call<List<ProjectResponse>>

    @GET("/api/projects/{id}")
    fun getProjectByID(@Path("id") id: UUID): Call<ProjectResponse>

    @POST("/api/projects")
    fun createProject(@Body body: ProjectRequest): Call<ProjectResponse>

    @PUT("/api/projects/{id}")
    fun updateProject(@Path("id") id: UUID, @Body body: ProjectRequest): Call<ProjectResponse>

    @DELETE("/api/projects/{id}")
    fun deleteProject(@Path("id") id: UUID): Call<Void>
}