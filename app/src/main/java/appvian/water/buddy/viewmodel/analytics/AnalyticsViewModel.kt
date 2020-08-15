package appvian.water.buddy.viewmodel.analytics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import appvian.water.buddy.util.TimeUtil

class AnalyticsViewModel {
    private val _curYear = MutableLiveData<Int>()
    private val _curMonth = MutableLiveData<Int>()
    private val _curDay = MutableLiveData<Int>()

    val curYear = _curYear as LiveData<Int>
    val curMonth = _curMonth as LiveData<Int>
    val curDay = _curDay as LiveData<Int>

    init {
        _curDay.value = TimeUtil.day
        _curMonth.value = TimeUtil.month
        _curYear.value = TimeUtil.year
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