package appvian.water.buddy.model.datasource.local

import androidx.lifecycle.LiveData
import androidx.room.*
import appvian.water.buddy.model.data.Intake


@Dao
interface intakeDao {

    @Query("SELECT * FROM intake WHERE date BETWEEN :today AND :tomorrow  ORDER BY date ASC")
    fun getDaily(today: Long, tomorrow: Long): LiveData<List<Intake>>

    @Query("SELECT SUM(amount) FROM intake WHERE date BETWEEN :today AND :tomorrow")
    fun getDailyAmount(today: Long, tomorrow: Long): LiveData<Int>

    @Query("SELECT ((SUM(amount))*100/:requiredAmount) FROM intake WHERE date BETWEEN :today AND :tomorrow")
    fun getDailyPercent(today: Long, tomorrow: Long, requiredAmount: Int): LiveData<Float>

    @Query("SELECT * FROM intake WHERE date BETWEEN :today AND :tomorrow group BY category ORDER BY date ASC")
    fun getDailyByGroup(today: Long, tomorrow: Long): LiveData<List<Intake>>

    @Query("SELECT * FROM intake WHERE date BETWEEN :aWeekAgo AND :today ORDER BY date ASC")
    fun getWeekly(today: Long, aWeekAgo: Long): LiveData<List<Intake>>

    @Query("SELECT * FROM intake WHERE date BETWEEN :aMonthAgo AND :today ORDER BY date ASC")
    fun getMonthly(today: Long, aMonthAgo: Long): LiveData<List<Intake>>

    @Query("UPDATE intake SET category=:category WHERE date=:date")
    fun modifyCategory(date: Long, category: Int)

    @Query("UPDATE intake SET amount=:amount WHERE date=:date")
    fun modifyAmount(date: Long, amount: Int)

    @Query("DELETE FROM intake")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(intake: Intake)

    @Delete
    fun delete(intake: Intake)
}