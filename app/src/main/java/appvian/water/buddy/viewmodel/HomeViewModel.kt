package appvian.water.buddy.viewmodel

import android.app.Application
import android.content.res.Resources
import android.util.DisplayMetrics
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import appvian.water.buddy.model.data.Intake
import appvian.water.buddy.model.repository.HomeRepository
import java.util.*

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = HomeRepository(application)

    var dailyIntake: LiveData<List<Intake>>? = getDaily()

    var dailyAmount : LiveData<Int>? = getDailyDrinkedAmount()

    val requiredAmount = 2000
    val waterStartY = Resources.getSystem().displayMetrics.heightPixels.toFloat() - 100F * (Resources.getSystem().displayMetrics.densityDpi).toFloat() / DisplayMetrics.DENSITY_DEFAULT
    var waterCurrentY = waterStartY
    var characterCurrentY = 0F
    var currentPercent = 0F
    val characterEndY = -(Resources.getSystem().displayMetrics.heightPixels.toFloat() - 300F * (Resources.getSystem().displayMetrics.densityDpi).toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    val startPercentTextY : Float = Resources.getSystem().displayMetrics.heightPixels.toFloat() - 370F * (Resources.getSystem().displayMetrics.densityDpi).toFloat() / DisplayMetrics.DENSITY_DEFAULT
    var currentPercentTextY = startPercentTextY

    private fun getDailyDrinkedAmount(): LiveData<Int>? {
        val dailyAmount = repository.getDailyAmount(getToday(), getTomorrow())
        return dailyAmount
    }

    private fun getDaily(): LiveData<List<Intake>>? {
        val dailyIntake = repository.getDaily(getToday(), getTomorrow())
        return dailyIntake
    }

    fun insert(intake: Intake) {
        repository.insert(intake)
    }

    fun delete(intake: Intake) {
        repository.delete(intake)
    }

    fun deleteAll(){
        repository.deleteAll()
    }

    private fun getToday(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY,0)
        calendar.set(Calendar.MINUTE,0)
        calendar.set(Calendar.SECOND,0)
        calendar.set(Calendar.MILLISECOND,0)
        val today = calendar.time.time
        return today
    }

    private fun getTomorrow(): Long {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, 1)
        calendar.set(Calendar.HOUR_OF_DAY,0)
        calendar.set(Calendar.MINUTE,0)
        calendar.set(Calendar.SECOND,0)
        calendar.set(Calendar.MILLISECOND,0)
        val tomorrow = calendar.time.time
        return tomorrow
    }
}