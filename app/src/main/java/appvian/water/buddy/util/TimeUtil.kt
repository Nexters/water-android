package appvian.water.buddy.util

import java.util.*

object TimeUtil {
    fun getCurrent() : Calendar = Calendar.getInstance()
    var year = getCurrent().get(Calendar.YEAR)
    var month = getCurrent().get(Calendar.MONTH) + 1
    var day = getCurrent().get(Calendar.DATE)

    fun getCalendarInstance() : Calendar {
        val calInstance = Calendar.getInstance()

        calInstance.set(Calendar.AM_PM, Calendar.AM)
        calInstance.set(Calendar.HOUR, 0)
        calInstance.set(Calendar.MINUTE, 0)
        calInstance.set(Calendar.SECOND, 0)
        calInstance.set(Calendar.MILLISECOND, 0)

        return calInstance
    }

    fun getYearWeekToMonthWeek(yearWeek:Int, year:Int?): List<Int> {
        val calInstance = getCalendarInstance()

        year?.let { calInstance.set(Calendar.YEAR, it) }
        calInstance.set(Calendar.WEEK_OF_YEAR, yearWeek)
        calInstance.set(Calendar.DAY_OF_WEEK, 7)

        val month = calInstance.get(Calendar.MONTH) + 1
        val monthWeek = calInstance.get(Calendar.WEEK_OF_MONTH)

        return listOf(month, monthWeek)
    }
}