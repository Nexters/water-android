package appvian.water.buddy.viewmodel.analytics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.*

class AnalyticsViewModel {
    private val _curYear = MutableLiveData<Int>()
    private val _curMonth = MutableLiveData<Int>()
    private val _curDay = MutableLiveData<Int>()

    val curYear = _curYear as LiveData<Int>
    val curMonth = _curMonth as LiveData<Int>
    val curDay = _curDay as LiveData<Int>

    init {
        val now = Calendar.getInstance()
        _curDay.value = now[Calendar.DATE]
        _curMonth.value = now[Calendar.MONTH] + 1
        _curYear.value = now[Calendar.YEAR]
    }

    fun setDay(day: Int) {
        _curDay.value = day
    }

    fun setMonth(month: Int, day: Int = 1) {
        _curMonth.value = month
        _curDay.value = day
    }

    fun setYear(year: Int, month: Int = 1, day: Int = 1) {
        _curMonth.value = month
        _curDay.value = day
        _curYear.value = year
    }
}