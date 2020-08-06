package appvian.water.buddy.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import appvian.water.buddy.model.data.Intake
import appvian.water.buddy.model.repository.HomeRepository
import java.util.*

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = HomeRepository(application)

    var dailyIntake: LiveData<List<Intake>>? = getDaily()

    var dailyAmount : LiveData<Int>? = getDailyDrinkedAmount()

    val requiredAmount = 2000

    var beforeChangeAmount = 0

    private fun getDailyDrinkedAmount(): LiveData<Int>? {
        val dailyAmount = repository.getDailyAmount(getToday(), getTomorrow())
        return dailyAmount
    }

    private fun getDaily(): LiveData<List<Intake>>? {
        val dailyIntake = repository.getDaily(getToday(), getTomorrow())
        return dailyIntake
    }

    fun insert(intake: Intake) {
        repository.insert(intake)
    }

    fun delete(intake: Intake) {
        repository.delete(intake)
    }

    fun deleteAll(){
        repository.deleteAll()
    }

    private fun getToday(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY,0)
        calendar.set(Calendar.MINUTE,0)
        calendar.set(Calendar.SECOND,0)
        calendar.set(Calendar.MILLISECOND,0)
        val today = calendar.time.time
        return today
    }

    private fun getTomorrow(): Long {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, 1)
        calendar.set(Calendar.HOUR_OF_DAY,0)
        calendar.set(Calendar.MINUTE,0)
        calendar.set(Calendar.SECOND,0)
        calendar.set(Calendar.MILLISECOND,0)
        val tomorrow = calendar.time.time
        return tomorrow
    }
}