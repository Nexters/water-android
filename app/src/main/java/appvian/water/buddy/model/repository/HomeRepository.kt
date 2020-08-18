package appvian.water.buddy.model.repository

import android.content.Context
import androidx.lifecycle.LiveData
import appvian.water.buddy.model.data.Intake
import appvian.water.buddy.model.datasource.local.WaterBuddyDb
import appvian.water.buddy.model.datasource.local.intakeDao
import appvian.water.buddy.utilities.WidgetUpdater
import kotlinx.coroutines.*

class HomeRepository(val context: Context) {

    private val waterBuddyDb = WaterBuddyDb.getInstance(context)

    private val intakedao: intakeDao = waterBuddyDb!!.intakeDao()

    fun getDaily(today: Long, tomorrow: Long): LiveData<List<Intake>>? {
        var dailyIntake: LiveData<List<Intake>>? = null

        dailyIntake = runBlocking(Dispatchers.IO) {
            withContext(Dispatchers.Default) {
                intakedao.getDaily(today, tomorrow)
            }
        }

        return dailyIntake
    }

    fun getDailyAmount(today: Long, tomorrow: Long): LiveData<Int>? {
        var dailyAmount: LiveData<Int>? = null

        dailyAmount = runBlocking(Dispatchers.IO) {
            withContext(Dispatchers.Default) {
                intakedao.getDailyAmount(today, tomorrow)
            }
        }

        return dailyAmount
    }

    fun getDailyPercent(today: Long, tomorrow: Long, requiredAmount: Int): LiveData<Float>? {
        var dailyPercent: LiveData<Float>? = null

        dailyPercent = runBlocking(Dispatchers.IO) {
            withContext(Dispatchers.Default) {
                intakedao.getDailyPercent(today, tomorrow, requiredAmount)
            }
        }

        return dailyPercent
    }

    fun getDailyByGroup(today: Long, tomorrow: Long): LiveData<List<Intake>>? {
        var dailyIntake: LiveData<List<Intake>>? = null

        dailyIntake = runBlocking(Dispatchers.IO) {
            withContext(Dispatchers.Default) {
                intakedao.getDailyByGroup(today, tomorrow)
            }
        }

        return dailyIntake
    }

    fun getWeekly(today: Long, aWeekAgo: Long): LiveData<List<Intake>>? {
        var weeklyIntake: LiveData<List<Intake>>? = null

        weeklyIntake = runBlocking(Dispatchers.IO) {
            withContext(Dispatchers.Default) {
                intakedao.getWeekly(
                    today,
                    aWeekAgo
                )
            }
        }
        return weeklyIntake
    }

    fun getWeeklyByDay(firstDay: Long, lastDay: Long): LiveData<List<Intake>>? {
        var weeklyIntake: LiveData<List<Intake>>? = null

        weeklyIntake = runBlocking(Dispatchers.IO) {
            withContext(Dispatchers.Default) {
                intakedao.getWeeklyByDay(
                    firstDay,
                    lastDay
                )
            }
        }

        return weeklyIntake
    }

    fun getWeeklyByTotal(startWeekDay: Long, endWeekDay: Long): LiveData<List<Intake>>? {
        var weeklyTotal: LiveData<List<Intake>>? = null

        weeklyTotal = runBlocking(Dispatchers.IO) {
            withContext(Dispatchers.Default) {
                intakedao.getWeeklyByTotal(startWeekDay, endWeekDay)
            }
        }

        return weeklyTotal
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

    fun getMonthlyByDay(startMonth: Long, endMonth: Long): LiveData<List<Intake>>? {
        var monthlyIntake: LiveData<List<Intake>>? = null

        monthlyIntake = runBlocking(Dispatchers.IO) {
            withContext(Dispatchers.Default) {
                intakedao.getMonthlyByDay(startMonth, endMonth)
            }
        }

        return monthlyIntake
    }
  
    fun modifyCategory(date: Long, category: Int){
        runBlocking(Dispatchers.IO) {
            intakedao.modifyCategory(date, category)
        }
        WidgetUpdater.update(context)
    }

    fun modifyAmount(date: Long, amount: Int){
        runBlocking(Dispatchers.IO) {
            intakedao.modifyAmount(date, amount)
        }
        WidgetUpdater.update(context)
    }

    fun insert(intake: Intake) {
        android.util.Log.d("repo", "insert $intake")
        runBlocking(Dispatchers.IO) {
            intakedao.insert(intake)
        }
        WidgetUpdater.update(context)
    }

    fun delete(intake: Intake) {
        runBlocking(Dispatchers.IO) {
            intakedao.delete(intake)
        }
        WidgetUpdater.update(context)
    }

    fun deleteAll() {
        runBlocking(Dispatchers.IO) {
            intakedao.deleteAll()
        }
        WidgetUpdater.update(context)
    }
}