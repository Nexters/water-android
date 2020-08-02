package appvian.water.buddy.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import appvian.water.buddy.model.data.Intake
import appvian.water.buddy.model.repository.HomeRepository
import java.util.*

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = HomeRepository(application)

    var dailyIntake: LiveData<List<Intake>>? = getDaily()

    var weeklyIntake: LiveData<List<Intake>>? = getWeekly()

    var monthlyIntake: LiveData<List<Intake>>? = getMonthly()

    private fun getDaily(): LiveData<List<Intake>>? {
        val dailyIntake = repository.getDaily(getToday(), getTomorrow())
        return dailyIntake
    }

    private fun getWeekly(): LiveData<List<Intake>>? {
        val weeklyIntake = repository.getWeekly(getToday(), getWeekAgo())
        return weeklyIntake
    }

    private fun getMonthly(): LiveData<List<Intake>>? {
        val monthlyIntake = repository.getMonthly(getToday(), getMonthAgo())
        return monthlyIntake
    }

    fun insert(intake: Intake) {
        repository.insert(intake)
    }

    fun delete(intake: Intake) {
        repository.delete(intake)
    }

    private fun getToday(): Long {
        val calendar = Calendar.getInstance()
        val today: Long = calendar.time.time
        return today
    }

    private fun getTomorrow(): Long {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, 1)
        val tomorrow = calendar.time.time
        return tomorrow
    }

    private fun getWeekAgo(): Long {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -6)
        val aWeekAgo = calendar.time.time
        return aWeekAgo
    }

    private fun getMonthAgo(): Long {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, -1)
        val aMonthAgo = calendar.time.time
        return aMonthAgo
    }
}