package ru.fefu.activitytracker

import android.app.Application
import androidx.room.Room
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.fefu.activitytracker.retrofit.TokenInterceptor
import ru.fefu.activitytracker.room.ActivityDB
import java.util.concurrent.TimeUnit

class App : Application() {
    companion object {
        lateinit var INSTANCE: App
    }

    val sharedPrefs by lazy {
        getSharedPreferences("shared_prefs", MODE_PRIVATE)
    }

    val okHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(10L, TimeUnit.SECONDS)
            .readTimeout(10L, TimeUnit.SECONDS)
            .writeTimeout(10L, TimeUnit.SECONDS)
            .callTimeout(10L, TimeUnit.SECONDS)
            .addInterceptor(TokenInterceptor(sharedPrefs))
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }
    val retrofit by lazy {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://fefu.t.feip.co/")
            .client(okHttpClient)
            .build()
    }
    val db: ActivityDB by lazy {
        Room.databaseBuilder(
            this,
            ActivityDB::class.java,
            "my_database"
        ).allowMainThreadQueries().build()
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }

}