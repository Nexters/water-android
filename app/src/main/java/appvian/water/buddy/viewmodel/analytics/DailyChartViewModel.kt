package appvian.water.buddy.viewmodel.analytics


import androidx.lifecycle.LiveData
import appvian.water.buddy.model.data.Intake
import appvian.water.buddy.model.repository.HomeRepository
import java.util.*


class DailyChartViewModel(private val repository: HomeRepository) {

    var dailyIntake: LiveData<List<Intake>>?

    init {
        val now = getToday()
        android.util.Log.d("daily intake" ,"$now, ${System.currentTimeMillis()}")
        dailyIntake = repository.getDailyByGroup(now, System.currentTimeMillis())
    }

    private fun getToday() : Long {
        val now = Calendar.getInstance()
        now.set(Calendar.AM_PM, Calendar.AM)
        now.set(Calendar.HOUR, 0)
        now.set(Calendar.MINUTE, 0)
        now.set(Calendar.SECOND, 0)
        now.set(Calendar.MILLISECOND, 0)

        return now.timeInMillis
    }

    fun getDailyIntake() {
        val now = getToday()

        getDailyIntake(now, System.currentTimeMillis())
    }

    private fun getDailyIntake(today: Long, tomorrow: Long) {
        dailyIntake = repository.getDailyByGroup(today, tomorrow)
    }
}