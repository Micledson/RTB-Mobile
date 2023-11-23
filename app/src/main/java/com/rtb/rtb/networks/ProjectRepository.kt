package com.rtb.rtb.networks

import android.content.Context
import android.widget.Toast
import com.rtb.rtb.networks.dto.request.ProjectRequest
import com.rtb.rtb.networks.dto.response.ProjectResponse
import com.rtb.rtb.networks.interfaces.ProjectInterface

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.UUID

class ProjectRepository {
    val retrofit = ApiService.instance
    val service = retrofit.create(ProjectInterface::class.java)

    fun getProjects(callback: (List<ProjectResponse>?) -> Unit) {
        val request = service.getProjects()

        request.enqueue(object : Callback<List<ProjectResponse>> {
            override fun onResponse(
                call: Call<List<ProjectResponse>>,
                response: Response<List<ProjectResponse>>
            ) {
                if (response.isSuccessful) {
                    callback.invoke(response.body())
                } else {
                    callback.invoke(null)
                }
            }

            override fun onFailure(call: Call<List<ProjectResponse>>, t: Throwable) {
                callback.invoke(null)
            }
        })
    }

    fun getProjectByID(id: UUID, callback: (ProjectResponse?) -> Unit) {
        val request = service.getProjectByID(id)

        request.enqueue(object : Callback<ProjectResponse> {
            override fun onResponse(
                call: Call<ProjectResponse>,
                response: Response<ProjectResponse>
            ) {
                if (response.isSuccessful) {
                    callback.invoke(response.body())
                } else {
                    callback.invoke(null)
                }
            }

            override fun onFailure(call: Call<ProjectResponse>, t: Throwable) {
                callback.invoke(null)
            }
        })
    }

    fun createProject(context: Context, body: ProjectRequest) {
        val request = service.createProject(body)

        request.enqueue(object : Callback<ProjectResponse> {
            override fun onResponse(
                call: Call<ProjectResponse>,
                response: Response<ProjectResponse>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "Projeto criado", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(context, "Erro ${response.errorBody()}", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<ProjectResponse>, t: Throwable) {
                Toast.makeText(context, "Erro ${t.message}", Toast.LENGTH_SHORT)
                    .show()
            }

        })
    }

    fun updateProject(context: Context, id: UUID,  body: ProjectRequest) {
        val request = service.updateProject(id, body)

        request.enqueue(object : Callback<ProjectResponse> {
            override fun onResponse(
                call: Call<ProjectResponse>,
                response: Response<ProjectResponse>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "Projeto Atualizado", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(context, "Erro ${response.errorBody()}", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<ProjectResponse>, t: Throwable) {
                Toast.makeText(context, "Erro ${t.message}", Toast.LENGTH_SHORT)
                    .show()
            }

        })
    }

    fun deleteProject(context: Context, id: UUID) {
        val request = service.deleteProject(id)

        request.enqueue(object : Callback<Void> {
            override fun onResponse(
                call: Call<Void>,
                response: Response<Void>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "Project Deleted Successfully!", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(context, "Error deleting project: ${response.errorBody()}", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(context, "Error deleting project: ${t.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

}


