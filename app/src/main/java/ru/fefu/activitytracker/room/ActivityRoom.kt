package ru.fefu.activitytracker.room
import android.location.Location
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class ActivityRoom (
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "activity_type") val type: Int,
    @ColumnInfo(name = "date_start") val dateStart: Long,
    @ColumnInfo(name = "date_end") val dateEnd: Long,
    @ColumnInfo(name = "coordinates") val coordinates: List<Pair<Double,Double>>, //latitude and longitude
    @ColumnInfo(name = "finished") val finished: Int,
    @ColumnInfo(name = "distance") val distance: Double,
    @ColumnInfo(name = "comment") val comment: String,
)