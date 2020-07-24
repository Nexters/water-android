package appvian.water.buddy.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import appvian.water.buddy.model.data.Intake
import appvian.water.buddy.model.repository.HomeRepository
import kotlinx.coroutines.InternalCoroutinesApi

class HomeViewModel (application: Application) : AndroidViewModel(application) {
    private val repository = HomeRepository(application)

    @InternalCoroutinesApi
    fun getDaily(today: Long, tomorrow: Long): LiveData<List<Intake>>{
        val dailyIntake = repository.getDaily(today,tomorrow)
        return dailyIntake
    }

    @InternalCoroutinesApi
    fun getWeekly(today: Long, aWeekAgo: Long): LiveData<List<Intake>>{
        val weeklyIntake = repository.getWeekly(today,aWeekAgo)
        return weeklyIntake
    }

    @InternalCoroutinesApi
    fun getMonthly(today: Long, aMonthAgo: Long): LiveData<List<Intake>>{
        val monthlyIntake = repository.getMonthly(today,aMonthAgo)
        return monthlyIntake
    }

    @InternalCoroutinesApi
    fun insert(intake: Intake){
        repository.insert(intake)
    }

    @InternalCoroutinesApi
    fun delete(intake: Intake){
        repository.delete(intake)
    }
}