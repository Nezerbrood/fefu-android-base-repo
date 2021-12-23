package ru.fefu.activitytracker.room;
import androidx.lifecycle.LiveData
import androidx.room.*
@Dao
interface ActivityDao {
    @Query("SELECT * FROM activity ORDER BY date_end DESC")
    fun getAll(): LiveData<List<Activity>>

    @Delete
    fun delete(activity: Activity)

    @Insert
    fun insert(activity: Activity)

    @Query("SELECT * FROM activity WHERE id =:id")
    fun getById(id: Int): List<Activity>


    @Query("UPDATE activity SET comment = :new_comment WHERE id = :id")
    fun setCommentById(id: Int, new_comment: String)

}