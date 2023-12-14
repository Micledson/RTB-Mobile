package com.rtb.rtb.networks

import android.content.Context
import com.rtb.rtb.networks.dto.request.RequirementRequest
import com.rtb.rtb.networks.dto.response.RequirementResponse
import com.rtb.rtb.networks.interfaces.RequirementInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.UUID

class RequirementRepository(apiService: ApiService) : BaseRepository() {
    private val retrofit = apiService.instance
    private val service = retrofit.create(RequirementInterface::class.java)

    fun getRequirements(projectId: UUID? = null, callback: (Result<List<RequirementResponse>?>) -> Unit) {
        val request = service.getRequirements(projectId)

        request.enqueue(object : Callback<List<RequirementResponse>> {
            override fun onResponse(
                call: Call<List<RequirementResponse>>,
                response: Response<List<RequirementResponse>>
            ) {
                if (response.isSuccessful) {
                    callback.invoke(Result.Success(response.body()))
                } else {
                    callback.invoke(Result.Error("Error: ${response.errorBody()}"))
                }
            }

            override fun onFailure(call: Call<List<RequirementResponse>>, t: Throwable) {
                callback.invoke(Result.Error("Error: ${t.message}"))
            }
        })
    }

    fun getRequirementById(id: UUID, callback: (Result<RequirementResponse?>) -> Unit) {
        val request = service.getRequirementById(id)

        request.enqueue(object : Callback<RequirementResponse> {
            override fun onResponse(
                call: Call<RequirementResponse>,
                response: Response<RequirementResponse>
            ) {
                if (response.isSuccessful) {
                    callback.invoke(Result.Success(response.body()))
                } else {
                    callback.invoke(Result.Error("Error: ${response.errorBody()}"))
                }
            }

            override fun onFailure(call: Call<RequirementResponse>, t: Throwable) {
                callback.invoke(Result.Error("Error: ${t.message}"))
            }
        })
    }

    fun createRequirement(context: Context, body: RequirementRequest, callback: (Result<Unit>) -> Unit) {
        val request = service.createRequirement(body)

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

    fun updateRequirement(context: Context, id: UUID, body: RequirementRequest, callback: (Result<Unit>) -> Unit) {
        val request = service.updateRequirement(id, body)

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

    fun deleteRequirement(context: Context, id: UUID, callback: (Result<Unit>) -> Unit) {
        val request = service.deleteRequirement(id)

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
