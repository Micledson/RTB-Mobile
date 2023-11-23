package com.rtb.rtb.networks

import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiService {
    private const val BASE_URL: String = "https://abce-2804-14d-12b0-821b-bc6b-58b3-2b0c-483f.ngrok-free.app"
    val instance: Retrofit
        get() = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(
                OkHttpClient.Builder()
                    .connectionSpecs(listOf(ConnectionSpec.CLEARTEXT, ConnectionSpec.MODERN_TLS))
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}