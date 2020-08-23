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
    var curYear = TimeUtil.year
    var curMonth = TimeUtil.month
    var curDay = TimeUtil.day

    private var weekDay: LiveData<List<Intake>>? = null
    val weekObserve: MediatorLiveData<List<BarEntry>> = MediatorLiveData()
    val targetValue = sharedPrefsRepository.targetAmountLiveData

    private var weekTotal: LiveData<List<Intake>>? = null
    val weekTotalObserve: MediatorLiveData<List<BarEntry>> = MediatorLiveData()
    var monthWeek =
        Array<List<Int>>(4) { i ->
            TimeUtil.getYearWeekToMonthWeek(
                (now.get(Calendar.WEEK_OF_YEAR) - 3) + i,
                null
            )
        }

    val sysObserve: MutableLiveData<Int> = MutableLiveData()

    fun getWeekIntakeData() {
        android.util.Log.d(
            "weekly vm",
            "getWeekIntakeData curWeek ${curYear}, ${curMonth}, ${curDay}"
        )
        val firstDate = GregorianCalendar(curYear, curMonth, curDay, 0, 0, 0)
        firstDate.timeZone = TimeZone.getDefault()
        firstDate.add(Calendar.WEEK_OF_MONTH, -1)
        firstDate.add(Calendar.WEEK_OF_MONTH, 1)
        firstDate.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)

        val lastDate = GregorianCalendar(curYear, curMonth, curDay, 0, 0, 0)
        lastDate.timeZone = TimeZone.getDefault()
        lastDate.add(Calendar.WEEK_OF_MONTH, 1)
        lastDate.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)

        weekDay = repository.getWeeklyByDay(firstDate.timeInMillis, lastDate.timeInMillis)

        android.util.Log.d(
            "weekly vm",
            "getWeekIntakeData curWeek ${firstDate.time}, ${lastDate.time}"
        )

        weekDay?.let { wr ->
            weekObserve.addSource(wr, androidx.lifecycle.Observer { values ->
                val barList = initBarList(8)

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
        val endWeek = GregorianCalendar(curYear, curMonth, curDay, 0, 0, 0)
        endWeek.add(Calendar.WEEK_OF_YEAR, 1)
        endWeek.set(Calendar.DAY_OF_WEEK, 1)

        val startWeek = GregorianCalendar(curYear, curMonth, curDay, 0, 0, 0)
        startWeek.set(Calendar.DAY_OF_WEEK, 1)
        startWeek.add(Calendar.WEEK_OF_YEAR, -3)

        android.util.Log.d(
            "weekly vm",
            "total curWeek ${startWeek.timeInMillis}, ${endWeek.timeInMillis}"
        )
        weekTotal = repository.getWeeklyByTotal(startWeek.timeInMillis, endWeek.timeInMillis)
        weekTotal?.let {
            weekTotalObserve.addSource(it, androidx.lifecycle.Observer { result ->
                val weekTotalArrayData = initBarList(4)

                val groupResult = result.groupBy { it.date + 1 }
                    .mapValues { it.value.sumBy { it.amount } / 1000f }.entries

                val curYearWeek = endWeek.get(Calendar.WEEK_OF_YEAR) - 4
                android.util.Log.d("weekly vm", "total curWeek $curYearWeek")
                for (i in groupResult) {
                    val index = i.key.toInt() - (curYearWeek)
                    android.util.Log.d("weekly vm", "total curWeek $curYearWeek index : $i")
                    weekTotalArrayData[index] = BarEntry(i.value, index)
                }

                weekTotalObserve.value = weekTotalArrayData
                setMothWeekLabel(curYearWeek)
                setSystemText(weekTotalArrayData)

            })
        }
    }

    private fun setSystemText(weekTotalArrayData: ArrayList<BarEntry>) {
        sysObserve.value =
            if (weekTotalArrayData.filter { it.`val`.equals(0f) }
                    .count() == 4) 0
            else ((weekTotalArrayData.get(3).`val` - weekTotalArrayData.get(2).`val`) * 1000).toInt()
    }

    private fun setMothWeekLabel(startWeek: Int) {
        for (i in 0..3) {
            monthWeek[i] = TimeUtil.getYearWeekToMonthWeek(startWeek + i, curYear)
        }
    }



    fun strSpanBuilder(msg: String, color: Int): SpannableStringBuilder {

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