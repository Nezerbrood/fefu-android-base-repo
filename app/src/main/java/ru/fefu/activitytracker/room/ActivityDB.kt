package ru.fefu.activitytracker.room
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [ActivityRoom::class], version = 1)

@TypeConverters(DataConverter::class)
abstract class ActivityDB: RoomDatabase() {
    abstract fun activityDao():ActivityDao
}