package appvian.water.buddy.util

import java.util.*

object TimeUtil {
    fun getCalendarInstance(): Calendar {
        val calInstance = Calendar.getInstance()

        calInstance.set(Calendar.AM_PM, Calendar.AM)
        calInstance.set(Calendar.HOUR, 0)
        calInstance.set(Calendar.MINUTE, 0)
        calInstance.set(Calendar.SECOND, 0)
        calInstance.set(Calendar.MILLISECOND, 0)

        return calInstance
    }

    fun getYearWeekToMonthWeek(yearWeek: Int, year: Int?): List<Int> {
        val calInstance = getCalendarInstance()

        year?.let { calInstance.set(Calendar.YEAR, it) }
        calInstance.set(Calendar.WEEK_OF_YEAR, yearWeek)
        calInstance.set(Calendar.DAY_OF_WEEK, 7)

        val month = calInstance.get(Calendar.MONTH) + 1
        val monthWeek = calInstance.get(Calendar.WEEK_OF_MONTH)

        return listOf(month, monthWeek)
    }

    fun getWeekOfMonth(year: Int, month: Int, day: Int): Int {
        val calInstance = getCalendarInstance()
        calInstance.set(year, month, day)

        return calInstance.get(Calendar.WEEK_OF_MONTH)
    }
}