package ru.fefu.activitytracker.retrofit

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import ru.fefu.activitytracker.retrofit.response.TokenUserModel
import ru.fefu.activitytracker.retrofit.response.UserModel

interface ActivityAPI {
    @GET("api/user/profile")
    suspend fun getProfile(): UserModel

    @POST("api/auth/register")
    suspend fun register(
        @Query("login") login: String,
        @Query("password") pass: String,
        @Query("name") name: String,
        @Query("gender") gender: Int,
    ): TokenUserModel

    @POST("api/auth/login")
    suspend fun login(
        @Query("login") login: String,
        @Query("password") password: String,
    ): TokenUserModel

    @POST("api/auth/logout")
    suspend fun logout(): Unit
}