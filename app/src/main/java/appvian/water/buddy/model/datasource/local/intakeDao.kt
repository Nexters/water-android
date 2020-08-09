package appvian.water.buddy.model.datasource.local

import androidx.lifecycle.LiveData
import androidx.room.*
import appvian.water.buddy.model.data.Intake


@Dao
interface intakeDao {

    @Query("SELECT * FROM intake WHERE date BETWEEN :today AND :tomorrow  ORDER BY date ASC")
    fun getDaily(today: Long, tomorrow: Long): LiveData<List<Intake>>

    @Query("SELECT date, category, SUM(amount) as amount FROM intake WHERE date BETWEEN :today AND :tomorrow group BY category ORDER BY date ASC")
    fun getDailyByGroup(today: Long, tomorrow: Long): LiveData<List<Intake>>

    @Query("SELECT * FROM intake WHERE date BETWEEN :aWeekAgo AND :today ORDER BY date ASC")
    fun getWeekly(today: Long, aWeekAgo: Long): LiveData<List<Intake>>

    @Query("SELECT * FROM intake WHERE date BETWEEN :aMonthAgo AND :today ORDER BY date ASC")
    fun getMonthly(today: Long, aMonthAgo: Long): LiveData<List<Intake>>

    @Query(
        "select sum(amount) as amount, category, date from (SELECT amount, category, strftime('%w', date/1000, 'unixepoch') as date " +
                "FROM (select * from intake where date between :firstDay And :endDay order by date asc) sub) main " +
                "group by date"
    )
    fun getWeeklyByDay(firstDay: Long, endDay: Long): LiveData<List<Intake>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(intake: Intake)

    @Delete
    fun delete(intake: Intake)
}