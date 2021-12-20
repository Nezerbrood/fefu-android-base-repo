package ru.fefu.activitytracker

import android.app.Application
import androidx.room.Room
import ru.fefu.activitytracker.room.ActivityDB

class App : Application() {
    companion object {
        lateinit var INSTANCE: App
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