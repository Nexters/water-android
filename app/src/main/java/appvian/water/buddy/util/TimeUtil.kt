package appvian.water.buddy.util

import java.util.*

object TimeUtil {

    fun getCalendarInstance() : Calendar {
        val calInstance = Calendar.getInstance()

        calInstance.set(Calendar.AM_PM, Calendar.AM)
        calInstance.set(Calendar.HOUR, 0)
        calInstance.set(Calendar.MINUTE, 0)
        calInstance.set(Calendar.SECOND, 0)
        calInstance.set(Calendar.MILLISECOND, 0)

        return calInstance
    }
}