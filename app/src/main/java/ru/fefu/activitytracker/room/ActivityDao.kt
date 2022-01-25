package ru.fefu.activitytracker.room;
import androidx.lifecycle.LiveData
import androidx.room.*
@Dao
interface ActivityDao {
    @Query("SELECT * FROM ActivityRoom ORDER BY date_end DESC")
    fun getAll(): LiveData<List<ActivityRoom>>

    @Query("SELECT * FROM ActivityRoom")
    fun getAllList(): List<ActivityRoom>

    @Delete
    fun delete(activityRoom: ActivityRoom)

    @Insert
    fun insert(activityRoom: ActivityRoom) : Long

    @Query("SELECT * FROM ActivityRoom WHERE id =:id LIMIT 1")
    fun getById(id: Int): ActivityRoom

    @Query("SELECT * FROM ActivityRoom WHERE finished= 0 LIMIT 1")
    fun getUnfinished(): ActivityRoom

    @Query("SELECT * FROM ActivityRoom WHERE id=:id LIMIT 1")
    fun getByIdLiveData(id: Int): LiveData<ActivityRoom>

    @Query("UPDATE ActivityRoom SET finished=1, date_end=:date_end WHERE id=:id")
    fun finishActivity(date_end: Long, id: Int)

    @Query("UPDATE ActivityRoom SET coordinates=:coordinates, distance=:distance  WHERE id=:id")
    fun updateCoordinates(coordinates: List<Pair<Double, Double>>, distance: Double, id: Int)

    /*@Query("UPDATE ActivityRoom SET finished=:finished WHERE id=:id")
    fun finishActivity(finished: Int, id: Int)*/

    @Query("UPDATE ActivityRoom SET comment = :new_comment WHERE id = :id")
    fun setCommentById(id: Int, new_comment: String)

}