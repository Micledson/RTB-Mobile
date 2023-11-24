package com.rtb.rtb.networks

import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiService {
    private const val BASE_URL: String = "https://3cd5-2804-1dc-4085-0-f5f1-2881-6ac5-a792.ngrok-free.app"
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