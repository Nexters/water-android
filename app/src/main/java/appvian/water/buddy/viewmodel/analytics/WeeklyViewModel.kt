package appvian.water.buddy.viewmodel.analytics

import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import appvian.water.buddy.model.data.Intake
import appvian.water.buddy.model.repository.HomeRepository
import appvian.water.buddy.model.repository.SharedPrefsRepository
import appvian.water.buddy.util.TimeUtil
import com.github.mikephil.charting.data.BarEntry
import java.util.*
import java.util.regex.Pattern

class WeeklyViewModel(
    private val sharedPrefsRepository: SharedPrefsRepository,
    val repository: HomeRepository
) {
    private val now = TimeUtil.getCalendarInstance()
    private val curYearWeek = now.get(Calendar.WEEK_OF_YEAR)

    var curWeek = now.get(Calendar.WEEK_OF_MONTH)

    private var weekDay: LiveData<List<Intake>>? = null
    val weekObserve: MediatorLiveData<List<BarEntry>> = MediatorLiveData()
    val targetValue = sharedPrefsRepository.targetAmountLiveData

    private var weekTotal: LiveData<List<Intake>>? = null
    val weekTotalObserve: MediatorLiveData<List<BarEntry>> = MediatorLiveData()
    val monthWeek =
        Array<List<Int>>(4) { i -> TimeUtil.getYearWeekToMonthWeek((curYearWeek - 3) + i, null) }

    val sysObserve: MutableLiveData<Int> = MutableLiveData()

    fun getWeekIntakeData() {
        val firstDate = TimeUtil.getCalendarInstance()
        firstDate.set(Calendar.WEEK_OF_MONTH, curWeek)
        firstDate.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)

        val lastDate = TimeUtil.getCalendarInstance()
        lastDate.set(Calendar.WEEK_OF_MONTH, curWeek + 1)
        lastDate.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)

        weekDay = repository.getWeeklyByDay(firstDate.timeInMillis, lastDate.timeInMillis)

        weekDay?.let { wr ->
            weekObserve.addSource(wr, androidx.lifecycle.Observer { values ->
                val barList = initBarList(7)

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

    private fun initBarList(size: Int): ArrayList<BarEntry> = arrayListOf<BarEntry>().apply {
        for (j in 0 until size) {
            add(BarEntry(0f, j))
        }
    }

    fun getTotalWeekIntakeData() {
        val endWeek = TimeUtil.getCalendarInstance()
        endWeek.set(Calendar.WEEK_OF_YEAR, curYearWeek + 1)
        endWeek.set(Calendar.DAY_OF_WEEK, 1)

        val startWeek = TimeUtil.getCalendarInstance()
        startWeek.set(Calendar.WEEK_OF_YEAR, curYearWeek - 3)

        weekTotal = repository.getWeeklyByTotal(startWeek.timeInMillis, endWeek.timeInMillis)
        weekTotal?.let {
            weekTotalObserve.addSource(it, androidx.lifecycle.Observer { result ->
                val weekTotalArrayData = initBarList(4)

                val groupResult = result.groupBy { it.date + 1 }
                    .mapValues { it.value.sumBy { it.amount } / 1000f }.entries

                for (i in groupResult) {
                    val index = i.key.toInt() - (curYearWeek - 3)
                    weekTotalArrayData[index] = BarEntry(i.value, index)
                }

                weekTotalObserve.value = weekTotalArrayData

                sysObserve.value =
                    if (weekTotalArrayData.filter { it.`val`.equals(0f) }
                            .count() == 4) 0
                    else ((weekTotalArrayData.get(3).`val` - weekTotalArrayData.get(2).`val`) * 1000).toInt()
            })
        }
    }


    fun strSapnBuilder(msg: String, color: Int): SpannableStringBuilder {
        val pattern = Pattern.compile("[0-9]+(ml)")
        val style = ForegroundColorSpan(color)

        val builder = SpannableStringBuilder(msg)
        val matchers = pattern.matcher(msg)
        if (matchers.find())
            builder.setSpan(
                style,
                matchers.start(),
                matchers.end(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )

        return builder
    }
}