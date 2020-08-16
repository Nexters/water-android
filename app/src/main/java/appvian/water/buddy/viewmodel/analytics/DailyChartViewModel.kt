package appvian.water.buddy.viewmodel.analytics


import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import appvian.water.buddy.model.data.Intake
import appvian.water.buddy.model.repository.HomeRepository
import appvian.water.buddy.util.TimeUtil
import java.util.*


class DailyChartViewModel(private val repository: HomeRepository) {
    private val now = Calendar.getInstance()
    private var dailyIntake: LiveData<List<Intake>>? = null
    private val _maxDrink: MutableLiveData<Int> = MutableLiveData()

    val maxDrink:LiveData<Int> = _maxDrink
    val observeIntake: MediatorLiveData<List<Intake>> = MediatorLiveData()

    var curYear = TimeUtil.year
    var curMonth = TimeUtil.month
    var curDay = TimeUtil.day

    fun getDailyIntake() {
        val now = TimeUtil.getCalendarInstance()
        now.set(Calendar.DATE, curDay)
        now.set(Calendar.MONTH, curMonth)
        now.set(Calendar.YEAR, curYear)

        val tomorrow = TimeUtil.getCalendarInstance()
        tomorrow.set(Calendar.DATE, curDay + 1)
        tomorrow.set(Calendar.MONTH, curMonth)
        tomorrow.set(Calendar.YEAR, curYear)

        getDailyIntakeFromRepository(now.timeInMillis, tomorrow.timeInMillis)
    }

    private fun getDailyIntakeFromRepository(today: Long, tomorrow: Long) {
        android.util.Log.d("daily intake", "$today, $tomorrow")
        dailyIntake = repository.getDailyByGroup(today, tomorrow)
        android.util.Log.d("daily intake", "${dailyIntake?.value}")

        dailyIntake?.let {
            observeIntake.addSource(it, androidx.lifecycle.Observer { values ->
                observeIntake.value = values

                if(values.isNotEmpty())
                    _maxDrink.value = values.maxBy { it.amount }?.category
                else
                    _maxDrink.value = -2
            })
        } ?: {
            _maxDrink.value = -2
        } ()
    }
}