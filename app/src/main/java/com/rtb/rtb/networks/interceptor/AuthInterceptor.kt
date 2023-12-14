package com.rtb.rtb.networks.interceptor

import com.rtb.rtb.database.preferences.SharedPrefs
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val sharedPrefs: SharedPrefs) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = sharedPrefs.getAccessToken()
        val originalRequest = chain.request()

        val newRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .build()

        return chain.proceed(newRequest)
    }
}