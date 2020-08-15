package appvian.water.buddy.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import appvian.water.buddy.model.data.SharedPreferenceBooleanLiveData
import appvian.water.buddy.model.data.SharedPreferenceFloatLiveData
import appvian.water.buddy.model.data.SharedPreferenceIntLiveData

import appvian.water.buddy.model.data.SharedPreferenceStringLiveData
import appvian.water.buddy.model.repository.SharedPrefsRepository


class SettingViewModel(application: Application) : AndroidViewModel(application) {
    //repository
    private val repository = SharedPrefsRepository(application)

    //사용자 닉네임
    var nameLiveData = repository.nameLiveData

    //사용자 키
    var heightLiveData = repository.heightLiveData
    //사용자 몸무게
    var weightLiveData = repository.weightLiveData
    //사용자 목표량  L / ml
    var targetAmountLiveData = repository.targetAmountLiveData
    var target_amount_int_live_data = repository.target_amount_int_live_data

    //프로필 수정 화면에서 입력된 닉네임 길이
    var name_cnt = MutableLiveData<Int>()

    //목표량 설정 화면을 보았는 지
    var is_new_live_data = repository.is_new_live_data

    //자동 목표량 설정인지 아닌지
    var is_auto_settiing_live_data = repository.is_auto_settiing_live_data

    init {
        name_cnt.value = nameLiveData.value!!.length
    }
    fun setNameLiveData(value: String) {
        repository.setNameLiveData(value)
    }

    fun setIsNewLiveData(value: Boolean){
        repository.setIsNewLiveData(value)
    }
    fun setTargetAmountIntLiveData(value : Int){
        repository.setTargetAmountIntLiveData(value)
    }
    fun setTargetAmountFloatLiveData(value : Float){
        repository.setTargetAmountFloatLiveData(value)
    }
    fun setIsAutoSettingLiveData(value : Boolean){
        repository.setIsAutoSettingLiveData(value)
    }
    fun setHeightLiveData(value : Int){
        repository.setHeightLiveData(value)
    }
    fun setWeightLiveData(value : Int){
        repository.setWeightLiveData(value)
    }

}