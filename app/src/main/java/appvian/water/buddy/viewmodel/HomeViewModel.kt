package appvian.water.buddy.viewmodel

import android.app.Application
import android.content.res.Resources
import android.util.DisplayMetrics
import android.view.View
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import appvian.water.buddy.R
import appvian.water.buddy.model.data.Intake
import appvian.water.buddy.model.repository.HomeRepository
import appvian.water.buddy.model.repository.SharedPrefsRepository
import kotlinx.android.synthetic.main.delete_toast.view.*
import java.util.*
import kotlin.collections.ArrayList

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = HomeRepository(application)
    private val sharedPrefsRepository = SharedPrefsRepository(application)
    var dailyIntake: LiveData<List<Intake>>? = getDaily()

    var dailyAmount : LiveData<Int>? = getDailyDrinkedAmount()
    var nickname = getName()
    val requiredAmount = getTargetAmount()
    val waterStartY = Resources.getSystem().displayMetrics.heightPixels.toFloat()
    var waterCurrentY = waterStartY
    var characterCurrentY = 0F
    var currentPercent = 0F
    val startPercentTextY : Float = Resources.getSystem().displayMetrics.heightPixels.toFloat() - 270F * (Resources.getSystem().displayMetrics.densityDpi).toFloat() / DisplayMetrics.DENSITY_DEFAULT
    var currentPercentTextY = startPercentTextY
    var percent = getDailyPercent()
    var showDeleteToast: MutableLiveData<Boolean> = MutableLiveData(false)
    var isDeleteButtonClicked: MutableLiveData<Boolean> = MutableLiveData(false)

    val deleteButton = View.OnClickListener {
        isDeleteButtonClicked.value = !isDeleteButtonClicked.value!!
    }

    var deleteIntakeList: MutableLiveData<MutableList<Intake>> = MutableLiveData()

    init {
        deleteIntakeList.value = ArrayList()
    }

    private fun getTargetAmount() : Int{
        var amount = 2000
        sharedPrefsRepository.target_amount_int_live_data.observeForever {
            amount=it
        }
        return amount
    }

    private fun getName(): String{
        var name = ".."
        sharedPrefsRepository.nameLiveData.observeForever {
            name = it
        }
        return name
    }

    private fun getDailyDrinkedAmount(): LiveData<Int>? {
        val dailyAmount = repository.getDailyAmount(getToday(), getTomorrow())
        return dailyAmount
    }

    private fun getDaily(): LiveData<List<Intake>>? {
        val dailyIntake = repository.getDaily(getToday(), getTomorrow())
        return dailyIntake
    }

    private fun getDailyPercent(): LiveData<Float>? {
        val dailyPercent = repository.getDailyPercent(getToday(),getTomorrow(),requiredAmount)
        return dailyPercent
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

    fun modifyCategory(date: Long, category: Int){
        repository.modifyCategory(date, category)
    }

    fun modifyAmount(date: Long, amount: Int){
        repository.modifyAmount(date, amount)
    }

    fun addDeleteList(intake: Intake){
        deleteIntakeList.value?.add(intake)
        deleteIntakeList.value = deleteIntakeList.value
    }

    fun cancelDeleteList(intake: Intake){
        deleteIntakeList.value?.remove(intake)
        deleteIntakeList.value = deleteIntakeList.value
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

    val deleteListner = object : View.OnClickListener{
        override fun onClick(v: View?) {
            if (deleteIntakeList.value!=null) {
                for (i in deleteIntakeList.value!!){
                    delete(i)
                }
                isDeleteButtonClicked.value = false
                deleteIntakeList.value!!.clear()
            }
            showDeleteToast.value = true
        }
    }
}