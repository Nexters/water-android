package appvian.water.buddy.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
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
    //사용자 목표량
    val target_amounts_pref = application.getSharedPreferences("target_amounts", Context.MODE_PRIVATE)
    var targetAmountLiveData = SharedPreferenceFloatLiveData(target_amounts_pref, "target_amounts", 1.6f)

    //프로필 수정 화면에서 입력된 닉네임 길이
    var name_cnt = MutableLiveData<Int>()
    init {
        name_cnt.value = nameLiveData.value!!.length
    }
    fun setNameLiveData(key: String, value: String) {
        val editor = name_pref.edit()
        editor.putString(key, value)
        editor.apply()
        nameLiveData = SharedPreferenceStringLiveData(name_pref, key, value)
    }


}