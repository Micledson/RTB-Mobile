package com.rtb.rtb.networks

import com.rtb.rtb.networks.dto.response.RequirementResponse
import com.rtb.rtb.networks.dto.response.ResourceResponse
import com.rtb.rtb.networks.interfaces.RequirementInterface
import com.rtb.rtb.networks.interfaces.ResourceInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResourceRepository {
    val retrofit = ApiService.instance
    val service = retrofit.create(ResourceInterface::class.java)

    fun getResources(callback: (ResourceResponse?) -> Unit) {
        val request = service.getResources()

        request.enqueue(object : Callback<ResourceResponse> {
            override fun onResponse(
                call: Call<ResourceResponse>,
                response: Response<ResourceResponse>
            ) {
                if (response.isSuccessful) {
                    callback.invoke(response.body())
                } else {
                    callback.invoke(null)
                }
            }

            override fun onFailure(call: Call<ResourceResponse>, t: Throwable) {
                callback.invoke(null)
            }
        })
    }
}