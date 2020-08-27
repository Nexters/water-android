package appvian.water.buddy.viewmodel

import android.app.Application
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import appvian.water.buddy.R
import appvian.water.buddy.model.repository.SharedPrefsRepository
import appvian.water.buddy.view.Intro.IntroFragment


class IntroViewModel(application: Application) : AndroidViewModel(application) {

class Factory(val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return IntroViewModel(application) as T
    }
}
    private val repository = SharedPrefsRepository(application)
    var nameLiveData = repository.nameLiveData

    //사용자 키
    var heightLiveData = repository.heightLiveData
    //사용자 몸무게
    var weightLiveData = repository.weightLiveData
    //사용자 목표량  L / ml
    var targetAmountLiveData = repository.targetAmountLiveData
    var target_amount_int_live_data = repository.target_amount_int_live_data

    fun confirm_inform(): Boolean {
        return nameLiveData.value.toString() == "" || heightLiveData.value.toString().toInt() == 0 || weightLiveData.value.toString().toInt() == 0
    }

    var nameliveText: MutableLiveData<String> = MutableLiveData()
    var targetamountliveText: MutableLiveData<String> = MutableLiveData()

    fun getText(): LiveData<String?>? {
        return nameliveText
    }

    fun TargetAmountSetText(text: Float?){
        targetamountliveText.value = String.format("%.1f",text)
    }
    fun nameSetText(text: String?) {
        nameliveText.value = text
    }

    fun setNameLiveData(value: String) {
        repository.setNameLiveData(value)
    }
    fun setTargetAmountFloatLiveData(value : Float){
        repository.setTargetAmountFloatLiveData(value)
    }
    fun setTargetAmountIntLiveData(value : Int){
        repository.setTargetAmountIntLiveData(value)
    }
    fun setHeightLiveData(value : Int){
        repository.setHeightLiveData(value)
    }
    fun setWeightLiveData(value : Int){
        repository.setWeightLiveData(value)
    }


}