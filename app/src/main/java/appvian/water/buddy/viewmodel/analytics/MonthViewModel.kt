package appvian.water.buddy.viewmodel.analytics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import appvian.water.buddy.model.data.Intake
import appvian.water.buddy.model.repository.HomeRepository
import appvian.water.buddy.util.TimeUtil
import java.util.*

class MonthViewModel(private val repository: HomeRepository) {
    private val now = TimeUtil.getCalendarInstance()

    var curMonth = now.get(Calendar.MONTH)
    var curMaxDay = now.getActualMaximum(Calendar.DAY_OF_MONTH)
    var monthlyRank: MediatorLiveData<List<Intake>> = MediatorLiveData()
    var characterList: MediatorLiveData<Map<Int, Intake>> = MediatorLiveData()

    private var _loadMoreActive: MutableLiveData<Boolean> = MutableLiveData()
    private var _observeIntakeDays: MutableLiveData<Pair<Int, Int>> = MutableLiveData()

    val loadMoreActive = _loadMoreActive as LiveData<Boolean>
    val observeIntakeDays = _observeIntakeDays as LiveData<Pair<Int, Int>>

    private val targetValue = 1600

    init {
        _observeIntakeDays.value = Pair(0, curMaxDay)
    }

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
                setRankData(it)
                setCharacterData(it)
            })
        }
    }

    private fun setRankData(it: List<Intake>) {
        val groupRank = it.groupBy { it.category }
            .mapValues { Intake(0, it.key, it.value.sumBy { it.amount }) }
            .values
            .toList()
            .sortedByDescending { it.amount }

        monthlyRank.value?.let {
            if (!it.equals(groupRank))
                monthlyRank.value = groupRank
        } ?: run {
            monthlyRank.value = groupRank
        }
    }

    private fun setCharacterData(it: List<Intake>) {
        val groupCharacter = it.groupBy { it.date.toInt() }
            .mapValues { mapList ->
                mapList.value.maxBy { it.amount }?.let { maxValue ->
                    Intake(mapList.key.toLong(), maxValue.category, maxValue.amount)
                }
            }

        val filterCharacter = groupCharacter.filterValues { values ->
            (values as Intake).amount >= targetValue
        }

        android.util.Log.d("month frag", "filter ${filterCharacter}, ${filterCharacter.size}")
        characterList.value?.let { characterData ->
            if (!characterData.equals(filterCharacter)) {
                characterList.value = filterCharacter as? Map<Int, Intake>
                _observeIntakeDays.value = Pair(filterCharacter.size, curMaxDay)
            }
        } ?: run {
            characterList.value = filterCharacter as? Map<Int, Intake>
            _observeIntakeDays.value = Pair(filterCharacter.size, curMaxDay)
        }
    }

    fun activeLoadMore() {
        _loadMoreActive.value = true
    }
}