package com.rtb.rtb.networks

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.rtb.rtb.networks.dto.request.ProjectRequest
import com.rtb.rtb.networks.dto.request.RequirementRequest
import com.rtb.rtb.networks.dto.response.ProjectResponse
import com.rtb.rtb.networks.dto.response.RequirementResponse
import com.rtb.rtb.networks.interfaces.RequirementInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.UUID

class RequirementRepository {
    val retrofit = ApiService.instance
    val service = retrofit.create(RequirementInterface::class.java)

    fun getRequirements(callback: (List<RequirementResponse>?) -> Unit) {
        val request = service.getRequirements()

        request.enqueue(object : Callback<List<RequirementResponse>> {
            override fun onResponse(
                call: Call<List<RequirementResponse>>,
                response: Response<List<RequirementResponse>>
            ) {
                if (response.isSuccessful) {
                    callback.invoke(response.body())
                } else {
                    callback.invoke(null)
                }
            }

            override fun onFailure(call: Call<List<RequirementResponse>>, t: Throwable) {
                callback.invoke(null)
            }
        })
    }

    fun getRequirementById(id: UUID, callback: (RequirementResponse?) -> Unit) {
        val request = service.getRequirementById(id)

        request.enqueue(object : Callback<RequirementResponse> {
            override fun onResponse(
                call: Call<RequirementResponse>,
                response: Response<RequirementResponse>
            ) {
                if (response.isSuccessful) {
                    callback.invoke(response.body())
                } else {
                    callback.invoke(null)
                }
            }

            override fun onFailure(call: Call<RequirementResponse>, t: Throwable) {
                callback.invoke(null)
            }
        })
    }

    fun createRequirement(context: Context, body: RequirementRequest) {
        val request = service.createRequirement(body)

        request.enqueue(object : Callback<Void> {
            override fun onResponse(
                call: Call<Void>,
                response: Response<Void>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "Requisito criado", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(context, "Erro ${response.errorBody()}", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(context, "Erro ${t.message}", Toast.LENGTH_SHORT)
                    .show()
            }

        })
    }

    fun updateRequirement(context: Context, id: UUID, body: RequirementRequest) {
        val request = service.updateRequirement(id, body)

        request.enqueue(object : Callback<Void> {
            override fun onResponse(
                call: Call<Void>,
                response: Response<Void>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "Requirement Atualizado", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(context, "Erro ${response.errorBody()}", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(context, "Erro ${t.message}", Toast.LENGTH_SHORT)
                    .show()
            }

        })
    }

    fun deleteRequirement(context: Context, id: UUID) {
        val request = service.deleteRequirement(id)

        request.enqueue(object : Callback<Void> {
            override fun onResponse(
                call: Call<Void>,
                response: Response<Void>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "Requirement deletado", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(context, "Erro ${response.errorBody()}", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(context, "Erro ${t.message}", Toast.LENGTH_SHORT)
                    .show()
            }

        })
    }

}