package com.rtb.rtb.networks

import android.content.Context
import com.rtb.rtb.database.preferences.SharedPrefs
import com.rtb.rtb.networks.interceptor.AuthInterceptor
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiService (context: Context) {

    private val BASE_URL: String = "https://106a-185-243-57-130.ngrok-free.app/api/docs/index.html'"

    private val sharedPrefs = SharedPrefs(context)

    val instance: Retrofit
        get() = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(AuthInterceptor(sharedPrefs))
                    .connectionSpecs(listOf(ConnectionSpec.CLEARTEXT, ConnectionSpec.MODERN_TLS))
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}