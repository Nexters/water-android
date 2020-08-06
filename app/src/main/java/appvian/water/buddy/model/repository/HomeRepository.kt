package appvian.water.buddy.model.repository

import android.content.Context
import androidx.lifecycle.LiveData
import appvian.water.buddy.model.data.Intake
import appvian.water.buddy.model.datasource.local.WaterBuddyDb
import appvian.water.buddy.model.datasource.local.intakeDao
import kotlinx.coroutines.*

class HomeRepository(context: Context) {

    private val waterBuddyDb = WaterBuddyDb.getInstance(context)

    private val intakedao: intakeDao = waterBuddyDb!!.intakeDao()

    fun getDaily(today: Long, tomorrow: Long): LiveData<List<Intake>>? {
        var dailyIntake: LiveData<List<Intake>>? = null

        dailyIntake = runBlocking(Dispatchers.IO){
            withContext(Dispatchers.Default) {
                intakedao.getDaily(today, tomorrow)
            }
        }

        return dailyIntake
    }

    fun getDailyAmount(today: Long, tomorrow: Long): LiveData<Int>? {
        var dailyAmount: LiveData<Int>? = null

        dailyAmount = runBlocking (Dispatchers.IO){
            withContext(Dispatchers.Default){
                intakedao.getDailyAmount(today, tomorrow)
            }
        }

        return  dailyAmount
    }

    fun getDailyByGroup(today: Long, tomorrow: Long): LiveData<List<Intake>>? {
        var dailyIntake: LiveData<List<Intake>>? = null

        dailyIntake = runBlocking(Dispatchers.IO){
            withContext(Dispatchers.Default) {
                intakedao.getDailyByGroup(today, tomorrow)
            }
        }

        return dailyIntake
    }

    fun getWeekly(today: Long, aWeekAgo: Long): LiveData<List<Intake>>? {
        var weeklyIntake: LiveData<List<Intake>>? = null

        GlobalScope.launch(Dispatchers.Default) {
            weeklyIntake = withContext(Dispatchers.Default) {
                intakedao.getWeekly(
                    today,
                    aWeekAgo
                )
            }
        }
        return weeklyIntake
    }

    fun getMonthly(today: Long, aMonthAgo: Long): LiveData<List<Intake>>? {
        var monthlyIntake: LiveData<List<Intake>>? = intakedao.getMonthly(today, aMonthAgo)

        GlobalScope.launch(Dispatchers.Default) {
            monthlyIntake = withContext(Dispatchers.Default) {
                intakedao.getMonthly(
                    today,
                    aMonthAgo
                )
            }
        }

        return monthlyIntake
    }

    fun insert(intake: Intake) {
        android.util.Log.d("repo", "insert $intake")
        runBlocking(Dispatchers.IO) {
            intakedao.insert(intake)
        }
    }

    fun delete(intake: Intake) {
        runBlocking(Dispatchers.IO) {
            intakedao.delete(intake)
        }
    }

    fun deleteAll() {
        runBlocking(Dispatchers.IO) {
            intakedao.deleteAll()
        }
    }
}