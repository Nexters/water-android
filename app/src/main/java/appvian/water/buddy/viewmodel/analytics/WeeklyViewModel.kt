package appvian.water.buddy.viewmodel.analytics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import appvian.water.buddy.model.data.Intake
import appvian.water.buddy.model.repository.HomeRepository
import appvian.water.buddy.util.TimeUtil
import com.github.mikephil.charting.data.BarEntry
import java.util.*

class WeeklyViewModel(val repository: HomeRepository) {
    private val now = Calendar.getInstance()
    var curWeek = now.get(Calendar.WEEK_OF_MONTH)
    val totalWeeks = now.getActualMaximum(Calendar.WEEK_OF_MONTH)

    private var weekDay: LiveData<List<Intake>>? = null
    val weekObserve: MediatorLiveData<List<BarEntry>> = MediatorLiveData()
    val targetValue: Float = 1.6f //Todo : sharedPreference에서 불러와야 함

    fun getWeekIntakeData() {
        val firstDate = TimeUtil.getCalendarInstance()
        firstDate.set(Calendar.WEEK_OF_MONTH, curWeek)
        firstDate.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)

        val lastDate = TimeUtil.getCalendarInstance()
        lastDate.set(Calendar.WEEK_OF_MONTH, curWeek + 1)
        lastDate.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)

        weekDay = repository.getWeeklyByDay(firstDate.timeInMillis, lastDate.timeInMillis)
        android.util.Log.d("week", "${firstDate.timeInMillis}, ${lastDate.timeInMillis}")

        weekDay?.let { wr ->
            weekObserve.addSource(wr, androidx.lifecycle.Observer { values ->
                val barList = initBarList()

                android.util.Log.d("weekly data", "$values")
                val groupValue = values.groupBy { it.date }
                    .mapValues { Intake(it.key, 0, it.value.sumBy { it.amount }) }.values

                for (i in groupValue) {
                    val index = i.date.toInt()
                    barList[index] = BarEntry(i.amount / 1000f, index)
                }
                weekObserve.value = barList
            })
        }
    }

    private fun initBarList(): ArrayList<BarEntry> = arrayListOf<BarEntry>().apply {
        for (j in 0..6) {
            add(BarEntry(0f, j))
        }
    }
}