package appvian.water.buddy.viewmodel.modal

import android.view.View
import androidx.lifecycle.MutableLiveData
import appvian.water.buddy.util.TimeUtil
import java.util.*

class CalendarViewModel {

    private val mCal = TimeUtil.getCalendarInstance()
    val year: MutableLiveData<Int> = MutableLiveData()
    val month: MutableLiveData<Int> = MutableLiveData()
    val dateList: MutableLiveData<List<Int?>> = MutableLiveData()

    init {
        year.value = mCal.get(Calendar.YEAR)
        month.value = mCal.get(Calendar.MONTH) + 1
    }

    val prevMonth = View.OnClickListener {
        month.value = month.value?.minus(1)
        month.value?.let {
            if (it < 1) {
                month.value = 12
                year.value = year.value?.minus(1)
            }
        }

        setCalendar()

    }

    val nextMonth = View.OnClickListener {
        month.value = month.value?.plus(1)
        month.value?.let {
            if (it > 12) {
                month.value = 1
                year.value = year.value?.plus(1)
            }
        }
        setCalendar()
    }

    fun setCalendar() {
        val dates = ArrayList<Int?>()

        mCal.set(year.value!!, month.value!! - 1, 1)

        var dayOfWeek = mCal.get(Calendar.DAY_OF_WEEK)
        
        //달력 공백 추가
        if (dayOfWeek != -1) {
            while (dayOfWeek - 1 > 0) {
                dates.add(null)
                dayOfWeek -= 1
            }
        }

        for (i in 1..mCal.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            dates.add(i)
        }

        dateList.value = dates
    }
}
