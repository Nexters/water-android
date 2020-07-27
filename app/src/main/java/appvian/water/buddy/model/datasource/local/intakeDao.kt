package appvian.water.buddy.model.datasource.local

import androidx.lifecycle.LiveData
import androidx.room.*
import appvian.water.buddy.model.data.Intake


@Dao
interface intakeDao {

    @Query("SELECT * FROM intake WHERE date BETWEEN :today AND :tomorrow  ORDER BY date ASC")
    fun getDaily(today: Long, tomorrow : Long): LiveData<List<Intake>>

    @Query("SELECT * FROM intake WHERE date BETWEEN :aWeekAgo AND :today ORDER BY date ASC")
    fun getWeekly(today: Long, aWeekAgo : Long): LiveData<List<Intake>>

    @Query("SELECT * FROM intake WHERE date BETWEEN :aMonthAgo AND :today ORDER BY date ASC")
    fun getMonthly(today: Long, aMonthAgo : Long): LiveData<List<Intake>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(intake: Intake)

    @Delete
    fun delete(intake: Intake)
}