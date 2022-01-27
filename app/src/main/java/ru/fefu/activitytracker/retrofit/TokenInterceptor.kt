package ru.fefu.activitytracker.retrofit

import android.content.SharedPreferences
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor(
    private val sharedPreferences: SharedPreferences
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        sharedPreferences.getString("token", null)?.let {
            request.addHeader("Authorization", "Bearer $it")
        }
        return chain.proceed(request.build())
    }
}