package appvian.water.buddy.model.repository

import android.app.Application
import androidx.lifecycle.LiveData
import appvian.water.buddy.model.data.Intake
import appvian.water.buddy.model.datasource.local.WaterBuddyDb
import appvian.water.buddy.model.datasource.local.intakeDao
import kotlinx.coroutines.InternalCoroutinesApi
import java.lang.Exception

class HomeRepository(application: Application) {

    @InternalCoroutinesApi
    private val waterBuddyDb = WaterBuddyDb.getInstance(application)
    @InternalCoroutinesApi
    private val intakedao: intakeDao = waterBuddyDb!!.intakeDao()

    @InternalCoroutinesApi
    fun getDaily(today: Long, tomorrow: Long) : LiveData<List<Intake>>{
        val dailyIntake: LiveData<List<Intake>> = intakedao.getDaily(today,tomorrow)
        return dailyIntake
    }

    @InternalCoroutinesApi
    fun getWeekly(today: Long, aWeekAgo: Long) : LiveData<List<Intake>>{
        val weeklyIntake: LiveData<List<Intake>> = intakedao.getWeekly(today,aWeekAgo)
        return weeklyIntake
    }

    @InternalCoroutinesApi
    fun getMonthly(today: Long, aMonthAgo: Long) : LiveData<List<Intake>>{
        val monthlyIntake: LiveData<List<Intake>> = intakedao.getMonthly(today,aMonthAgo)
        return monthlyIntake
    }

    @InternalCoroutinesApi
    fun insert(intake: Intake){
        intakedao.insert(intake)
    }

    @InternalCoroutinesApi
    fun delete(intake: Intake){
        intakedao.delete(intake)
    }
}