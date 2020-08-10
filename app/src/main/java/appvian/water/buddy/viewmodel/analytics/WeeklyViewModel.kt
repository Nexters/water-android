package appvian.water.buddy.viewmodel.analytics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import appvian.water.buddy.model.data.Intake
import appvian.water.buddy.model.repository.HomeRepository
import appvian.water.buddy.util.TimeUtil
import com.github.mikephil.charting.data.BarEntry
import java.util.*

class WeeklyViewModel(val repository: HomeRepository) {
    private val now = TimeUtil.getCalendarInstance()
    private val curYearWeek = now.get(Calendar.WEEK_OF_YEAR)

    var curWeek = now.get(Calendar.WEEK_OF_MONTH)
    val totalWeeks = IntArray(now.getActualMaximum(Calendar.WEEK_OF_MONTH)) { i -> i + 1 }

    private var weekDay: LiveData<List<Intake>>? = null
    val weekObserve: MediatorLiveData<List<BarEntry>> = MediatorLiveData()
    val targetValue: Float = 1.6f //Todo : sharedPreference에서 불러와야 함

    private var weekTotal: LiveData<List<Intake>>? = null
    val weekTotalObserve: MediatorLiveData<List<BarEntry>> = MediatorLiveData()
    val monthWeek = Array<String>(4){ i -> "${(curYearWeek - 3) + i}"}

    fun getWeekIntakeData() {
        android.util.Log.d("weekly ", "curweek ${curWeek}")
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

    fun getTotalWeekIntakeData() {
        val endWeek = TimeUtil.getCalendarInstance()
        endWeek.set(Calendar.WEEK_OF_YEAR, curYearWeek + 1)
        endWeek.set(Calendar.DAY_OF_WEEK, 1)

        val startWeek = TimeUtil.getCalendarInstance()
        startWeek.set(Calendar.WEEK_OF_YEAR, curYearWeek - 3)

        android.util.Log.d(
            "weekly vm",
            "${endWeek.get(Calendar.WEEK_OF_YEAR)}, ${endWeek.timeInMillis}, ${startWeek.get(
                Calendar.WEEK_OF_YEAR
            )}"
        )

        weekTotal = repository.getWeeklyByTotal(startWeek.timeInMillis, endWeek.timeInMillis)
        weekTotal?.let {
            weekTotalObserve.addSource(it, androidx.lifecycle.Observer { result ->
                val weekTotalArrayData = arrayListOf<BarEntry>().apply {
                    for (i in 0..3) add(BarEntry(0f, i))
                }

                val groupResult = result.groupBy { it.date + 1 }
                    .mapValues { it.value.sumBy { it.amount } / 1000f }.entries

                for(i in groupResult) {
                    val index = i.key.toInt() - (curYearWeek - 3)
                    weekTotalArrayData[index] = BarEntry(i.value, index)
                }

                weekTotalObserve.value = weekTotalArrayData
            })
        }
    }
}