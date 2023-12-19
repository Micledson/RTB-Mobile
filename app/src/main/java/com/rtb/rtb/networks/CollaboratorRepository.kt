package com.rtb.rtb.networks

import android.util.Log
import com.rtb.rtb.networks.dto.request.CollaboratorRequest
import com.rtb.rtb.networks.dto.response.CollaboratorResponse
import com.rtb.rtb.networks.interfaces.CollaboratorInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.UUID

class CollaboratorRepository(apiService: ApiService) : BaseRepository() {
    private val retrofit = apiService.instance
    private val service = retrofit.create(CollaboratorInterface::class.java)

    fun getCollaborators(projectId: UUID, callback: (Result<List<CollaboratorResponse>?>) -> Unit) {
        val request = service.getCollaborators(projectId)

        request.enqueue(object : Callback<List<CollaboratorResponse>> {
            override fun onResponse(
                call: Call<List<CollaboratorResponse>>,
                response: Response<List<CollaboratorResponse>>
            ) {
                if (response.isSuccessful) {
                    callback.invoke(Result.Success(response.body()))
                } else {
                    callback.invoke(Result.Error("Error: ${response.errorBody()}"))
                }
            }

            override fun onFailure(call: Call<List<CollaboratorResponse>>, t: Throwable) {
                callback.invoke(Result.Error("Error: ${t.message}"))
            }
        })
    }

    fun getPossibleCollaborators(projectId: UUID, callback: (Result<List<CollaboratorResponse>?>) -> Unit) {
        val request = service.getPossibleCollaborators(projectId)

        request.enqueue(object : Callback<List<CollaboratorResponse>> {
            override fun onResponse(
                call: Call<List<CollaboratorResponse>>,
                response: Response<List<CollaboratorResponse>>
            ) {
                if (response.isSuccessful) {
                    callback.invoke(Result.Success(response.body()))
                } else {
                    callback.invoke(Result.Error("Error: ${response.errorBody()}"))
                }
            }

            override fun onFailure(call: Call<List<CollaboratorResponse>>, t: Throwable) {
                callback.invoke(Result.Error("Error: ${t.message}"))
            }
        })
    }

    fun createCollaborator(projectId: UUID, body: CollaboratorRequest, callback: (Result<Unit>) -> Unit) {
        val request = service.createCollaborator(projectId, body)

        request.enqueue(object : Callback<Void> {
            override fun onResponse(
                call: Call<Void>,
                response: Response<Void>
            ) {
                if (response.isSuccessful) {
                    callback.invoke(Result.Success(Unit))
                } else {
                    callback.invoke(Result.Error("Error: ${response.errorBody()}"))
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                callback.invoke(Result.Error("Error: ${t.message}"))
            }

        })
    }

    fun deleteCollaborator(projectId: UUID,  userId: UUID, callback: (Result<Unit>) -> Unit) {
        val request = service.deleteCollaborator(projectId, userId)

        request.enqueue(object : Callback<Void> {
            override fun onResponse(
                call: Call<Void>,
                response: Response<Void>
            ) {
                if (response.isSuccessful) {
                    callback.invoke(Result.Success(Unit))
                } else {
                    callback.invoke(Result.Error("Error: ${response.errorBody()}"))
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                callback.invoke(Result.Error("Error: ${t.message}"))
            }

        })
    }
}
