package appvian.water.buddy.viewmodel.analytics


import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import appvian.water.buddy.model.data.Intake
import appvian.water.buddy.model.repository.HomeRepository
import java.util.*


class DailyChartViewModel(private val repository: HomeRepository) {

    private var dailyIntake: LiveData<List<Intake>>? = null
    private val _maxDrink: MutableLiveData<Int> = MutableLiveData()

    val maxDrink:LiveData<Int> = _maxDrink
    val observeIntake: MediatorLiveData<List<Intake>> = MediatorLiveData()
    val todayDate = Calendar.getInstance()[Calendar.DATE] - 1


    private fun getToday(): Calendar {
        val now = Calendar.getInstance()
        now.set(Calendar.AM_PM, Calendar.AM)
        now.set(Calendar.HOUR, 0)
        now.set(Calendar.MINUTE, 0)
        now.set(Calendar.SECOND, 0)
        now.set(Calendar.MILLISECOND, 0)

        return now
    }

    fun getDailyIntake() {
        val now = getToday()

        val tomorrow = Calendar.getInstance()
        tomorrow.set(Calendar.DATE, now.get(Calendar.DATE) + 1)
        tomorrow.set(Calendar.AM_PM, Calendar.AM)
        tomorrow.set(Calendar.HOUR, 0)
        tomorrow.set(Calendar.MINUTE, 0)
        tomorrow.set(Calendar.SECOND, 0)
        tomorrow.set(Calendar.MILLISECOND, 0)

        getDailyIntake(now.timeInMillis, tomorrow.timeInMillis)
    }

    fun getDailyIntake(day: Int) {
        val now = Calendar.getInstance()
        now.set(Calendar.DATE, day)
        now.set(Calendar.AM_PM, Calendar.AM)
        now.set(Calendar.HOUR, 0)
        now.set(Calendar.MINUTE, 0)
        now.set(Calendar.SECOND, 0)
        now.set(Calendar.MILLISECOND, 0)

        val tomorrow = Calendar.getInstance()
        tomorrow.set(Calendar.DATE, day + 1)
        tomorrow.set(Calendar.AM_PM, Calendar.AM)
        tomorrow.set(Calendar.HOUR, 0)
        tomorrow.set(Calendar.MINUTE, 0)
        tomorrow.set(Calendar.SECOND, 0)
        tomorrow.set(Calendar.MILLISECOND, 0)

        getDailyIntake(now.timeInMillis, tomorrow.timeInMillis)
    }

    private fun getDailyIntake(today: Long, tomorrow: Long) {
        android.util.Log.d("daily intake", "$today, $tomorrow")
        dailyIntake = repository.getDailyByGroup(today, tomorrow)
        android.util.Log.d("daily intake", "${dailyIntake?.value}")

        dailyIntake?.let {
            observeIntake.addSource(it, androidx.lifecycle.Observer { values ->
                observeIntake.value = values
                _maxDrink.value = values.maxBy { it.amount }?.category
            })
        } ?: {
            _maxDrink.value = -2
        } ()
    }
}