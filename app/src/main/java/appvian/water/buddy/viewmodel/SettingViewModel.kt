package appvian.water.buddy.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import appvian.water.buddy.model.data.SharedPreferenceBooleanLiveData
import appvian.water.buddy.model.data.SharedPreferenceFloatLiveData
import appvian.water.buddy.model.data.SharedPreferenceIntLiveData

import appvian.water.buddy.model.data.SharedPreferenceStringLiveData


class SettingViewModel(application: Application) : AndroidViewModel(application) {
    //사용자 닉네임
    val name_pref = application.getSharedPreferences("name", Context.MODE_PRIVATE)
    var nameLiveData: SharedPreferenceStringLiveData = SharedPreferenceStringLiveData(name_pref, "name", name_pref.getString("name", "nick name")!!)

    //사용자 키
    val height_pref = application.getSharedPreferences("height", Context.MODE_PRIVATE)
    var heightLiveData = SharedPreferenceIntLiveData(height_pref, "height", 160)
    //사용자 몸무게
    val weight_pref = application.getSharedPreferences("weight", Context.MODE_PRIVATE)
    var weightLiveData = SharedPreferenceIntLiveData(weight_pref, "weight", 50)
    //사용자 목표량  L / ml
    val target_amounts_pref = application.getSharedPreferences("target_amounts", Context.MODE_PRIVATE)
    var targetAmountLiveData = SharedPreferenceFloatLiveData(target_amounts_pref, "target_amounts", 2f)
    val target_amounts_int_pref = application.getSharedPreferences("target_amounts_ml", Context.MODE_PRIVATE)
    var target_amount_int_live_data = SharedPreferenceIntLiveData(target_amounts_int_pref, "target_amounts_ml", 2000)

    //프로필 수정 화면에서 입력된 닉네임 길이
    var name_cnt = MutableLiveData<Int>()

    //목표량 설정 화면을 보았는 지
    var is_new_pref = application.getSharedPreferences("is_new_flag", Context.MODE_PRIVATE)
    var is_new_live_data = SharedPreferenceBooleanLiveData(is_new_pref, "is_new_flag",true)

    //자동 목표량 설정인지 아닌지
    var is_auto_setting_pref = application.getSharedPreferences("is_auto_setting", Context.MODE_PRIVATE)
    var is_auto_settiing_live_data = SharedPreferenceBooleanLiveData(is_auto_setting_pref, "is_auto_setting", true)

    init {
        name_cnt.value = nameLiveData.value!!.length
    }
    fun setNameLiveData(value: String) {
        val editor = name_pref.edit()
        editor.putString("name", value)
        editor.apply()
        nameLiveData = SharedPreferenceStringLiveData(name_pref, "name", value)
    }

    fun setIsNewLiveData(value: Boolean){
        val editor = is_new_pref.edit()
        editor.putBoolean("is_new_flag", value)
        editor.apply()
        is_new_live_data = SharedPreferenceBooleanLiveData(is_new_pref, "is_new_flag", value)
    }
    fun setTargetAmountIntLiveData(value : Int){
        val editor = target_amounts_int_pref.edit()
        editor.putInt("target_amounts_ml", value)
        editor.apply()
        target_amount_int_live_data = SharedPreferenceIntLiveData(target_amounts_int_pref, "target_amounts_ml", value)
    }
    fun setTargetAmountFloatLiveData(value : Float){
        val editor = target_amounts_pref.edit()
        editor.putFloat("target_amounts", value)
        editor.apply()
        targetAmountLiveData = SharedPreferenceFloatLiveData(target_amounts_pref, "target_amounts", value)
    }
    fun setIsAutoSettingLiveData(value : Boolean){
        val editor = is_auto_setting_pref.edit()
        editor.putBoolean("is_auto_setting", value)
        editor.apply()
        is_auto_settiing_live_data = SharedPreferenceBooleanLiveData(is_auto_setting_pref, "is_auto_setting", value)
    }
    fun setHeightLiveData(value : Int){
        val editor = height_pref.edit()
        editor.putInt("height", value)
        editor.apply()
        heightLiveData = SharedPreferenceIntLiveData(height_pref, "height", value)
    }
    fun setWeightLiveData(value : Int){
        val editor = weight_pref.edit()
        editor.putInt("weight", value)
        editor.apply()
        weightLiveData = SharedPreferenceIntLiveData(weight_pref, "weight", value)
    }

}