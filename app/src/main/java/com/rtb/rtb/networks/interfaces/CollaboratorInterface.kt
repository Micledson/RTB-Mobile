package com.rtb.rtb.networks.interfaces

import com.rtb.rtb.networks.dto.request.CollaboratorRequest
import com.rtb.rtb.networks.dto.response.CollaboratorResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.UUID

interface CollaboratorInterface {
    @GET("/api/collaborators/{projectId}")
    fun getCollaborators(@Path("projectId") projectId: UUID): Call<List<CollaboratorResponse>>

    @GET("/api/collaborators/possible-collaborators/{projectId}")
    fun getPossibleCollaborators(@Path("projectId") projectId: UUID): Call<List<CollaboratorResponse>>

    @POST("/api/collaborators/{projectId}")
    fun createCollaborator(@Path("projectId") projectId: UUID, @Body body: CollaboratorRequest): Call<Void>

    @DELETE("/api/collaborators/{projectId}/{userId}")
    fun deleteCollaborator(@Path("projectId") projectId: UUID, @Path("userId") userId: UUID): Call<Void>
}