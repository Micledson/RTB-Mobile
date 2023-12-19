package com.rtb.rtb.networks

import android.content.Context
import com.rtb.rtb.networks.dto.request.ProjectRequest
import com.rtb.rtb.networks.dto.response.ProjectResponse
import com.rtb.rtb.networks.interfaces.ProjectInterface

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.UUID

class ProjectRepository(apiService: ApiService) : BaseRepository() {
    val retrofit = apiService.instance
    val service = retrofit.create(ProjectInterface::class.java)

    fun getProjects(callback: (Result<List<ProjectResponse>?>) -> Unit) {
        val request = service.getProjects()

        request.enqueue(object : Callback<List<ProjectResponse>> {
            override fun onResponse(
                call: Call<List<ProjectResponse>>,
                response: Response<List<ProjectResponse>>
            ) {
                if (response.isSuccessful) {
                    callback.invoke(Result.Success(response.body()))
                } else {
                    callback.invoke(Result.Error("Error: ${response.errorBody()}"))
                }
            }

            override fun onFailure(call: Call<List<ProjectResponse>>, t: Throwable) {
                callback.invoke(Result.Error("Error: ${t.message}"))
            }
        })
    }

    fun getProjectByID(id: UUID, callback: (Result<ProjectResponse?>) -> Unit) {
        val request = service.getProjectByID(id)

        request.enqueue(object : Callback<ProjectResponse> {
            override fun onResponse(
                call: Call<ProjectResponse>,
                response: Response<ProjectResponse>
            ) {
                if (response.isSuccessful) {
                    callback.invoke(Result.Success(response.body()))
                } else {
                    callback.invoke(Result.Error("Error: ${response.errorBody()}"))
                }
            }

            override fun onFailure(call: Call<ProjectResponse>, t: Throwable) {
                callback.invoke(Result.Error("Error: ${t.message}"))
            }
        })
    }

    fun createProject(context: Context, body: ProjectRequest, callback: (Result<ProjectResponse>) -> Unit) {
        val request = service.createProject(body)

        request.enqueue(object : Callback<ProjectResponse> {
            override fun onResponse(
                call: Call<ProjectResponse>,
                response: Response<ProjectResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { callback.invoke(Result.Success(it)) }
                } else {
                    callback.invoke(Result.Error("Error: ${response.errorBody()}"))
                }
            }

            override fun onFailure(call: Call<ProjectResponse>, t: Throwable) {
                callback.invoke(Result.Error("Error: ${t.message}"))
            }

        })
    }

    fun updateProject(context: Context, id: UUID, body: ProjectRequest, callback: (Result<Unit>) -> Unit) {
        val request = service.updateProject(id, body)

        request.enqueue(object : Callback<ProjectResponse> {
            override fun onResponse(
                call: Call<ProjectResponse>,
                response: Response<ProjectResponse>
            ) {
                if (response.isSuccessful) {
                    callback.invoke(Result.Success(Unit))
                } else {
                    callback.invoke(Result.Error("Error: ${response.errorBody()}"))
                }
            }

            override fun onFailure(call: Call<ProjectResponse>, t: Throwable) {
                callback.invoke(Result.Error("Error: ${t.message}"))
            }

        })
    }

    fun deleteProject(context: Context, id: UUID, callback: (Result<Unit>) -> Unit) {
        val request = service.deleteProject(id)

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
