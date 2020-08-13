package appvian.water.buddy.viewmodel.analytics

import androidx.lifecycle.MediatorLiveData
import appvian.water.buddy.model.data.Intake
import appvian.water.buddy.model.repository.HomeRepository
import appvian.water.buddy.util.TimeUtil
import java.util.*

class MonthViewModel(private val repository: HomeRepository) {
    private val now = TimeUtil.getCalendarInstance()

    var curMonth = now.get(Calendar.MONTH)
    var monthlyRank:MediatorLiveData<List<Intake>> = MediatorLiveData()

    fun getMonthlyIntake() {
        val startMonth = TimeUtil.getCalendarInstance()
        startMonth.set(Calendar.MONTH, curMonth)
        startMonth.set(Calendar.DATE, 1)

        val endMonth = TimeUtil.getCalendarInstance()
        endMonth.set(Calendar.MONTH, curMonth + 1)
        endMonth.set(Calendar.DATE, 1)

        android.util.Log.d("month fragment", "${startMonth.timeInMillis}, ${endMonth.timeInMillis}")
        val data = repository.getMonthlyByDay(startMonth.timeInMillis, endMonth.timeInMillis)
        data?.let { result ->
            monthlyRank.addSource(result, androidx.lifecycle.Observer {
                android.util.Log.d("month fragment", "${it}")
                val groupRank = it.groupBy { it.category }.mapValues { Intake(0, it.key, it.value.sumBy { it.amount }) }.values

                monthlyRank.value = groupRank.toList().sortedByDescending { it.amount }
            })
        }
    }
}