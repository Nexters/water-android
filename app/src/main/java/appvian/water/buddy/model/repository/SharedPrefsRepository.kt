package appvian.water.buddy.model.repository

import android.content.Context
import appvian.water.buddy.model.data.SharedPreferenceBooleanLiveData
import appvian.water.buddy.model.data.SharedPreferenceFloatLiveData
import appvian.water.buddy.model.data.SharedPreferenceIntLiveData
import appvian.water.buddy.model.data.SharedPreferenceStringLiveData
import java.lang.StringBuilder
import java.util.*

class SharedPrefsRepository(context: Context) {
    //사용자 닉네임
    val name_pref = context.getSharedPreferences("name", Context.MODE_PRIVATE)
    var nameLiveData: SharedPreferenceStringLiveData = SharedPreferenceStringLiveData(name_pref, "name", name_pref.getString("name", "name")!!)

    //사용자 키
    val height_pref = context.getSharedPreferences("height", Context.MODE_PRIVATE)
    var heightLiveData = SharedPreferenceIntLiveData(height_pref, "height", 160)
    //사용자 몸무게
    val weight_pref = context.getSharedPreferences("weight", Context.MODE_PRIVATE)
    var weightLiveData = SharedPreferenceIntLiveData(weight_pref, "weight", 50)
    //사용자 목표량  L / ml
    val target_amounts_pref = context.getSharedPreferences("target_amounts", Context.MODE_PRIVATE)
    var targetAmountLiveData = SharedPreferenceFloatLiveData(target_amounts_pref, "target_amounts", 2f)
    val target_amounts_int_pref = context.getSharedPreferences("target_amounts_ml", Context.MODE_PRIVATE)
    var target_amount_int_live_data = SharedPreferenceIntLiveData(target_amounts_int_pref, "target_amounts_ml", 2000)

    //목표량 설정 화면을 보았는 지
    var is_new_pref = context.getSharedPreferences("is_new_flag", Context.MODE_PRIVATE)
    var is_new_live_data = SharedPreferenceBooleanLiveData(is_new_pref, "is_new_flag",true)

    //자동 목표량 설정인지 아닌지
    var is_auto_setting_pref = context.getSharedPreferences("is_auto_setting", Context.MODE_PRIVATE)
    var is_auto_settiing_live_data = SharedPreferenceBooleanLiveData(is_auto_setting_pref, "is_auto_setting", true)

    //자주 마시는 음료 1
    val fav_1_pref = context.getSharedPreferences("favorite_1", Context.MODE_PRIVATE)
    var fav_1_livedata: SharedPreferenceStringLiveData = SharedPreferenceStringLiveData(fav_1_pref, "favorite_1", fav_1_pref.getString("favorite_1", "")!!)
    //자주 마시는 음료 2
    val fav_2_pref = context.getSharedPreferences("favorite_2", Context.MODE_PRIVATE)
    var fav_2_livedata: SharedPreferenceStringLiveData = SharedPreferenceStringLiveData(fav_2_pref, "favorite_2", fav_2_pref.getString("favorite_2", "")!!)

    //알림 on/ off
    val alarm_flag_pref = context.getSharedPreferences("alarm_flag", Context.MODE_PRIVATE)
    var alarm_flag_LiveData : SharedPreferenceBooleanLiveData = SharedPreferenceBooleanLiveData(alarm_flag_pref, "alarm_flag", alarm_flag_pref.getBoolean("alarm_flag", false))
    //시작 시간
    val start_time_pref = context.getSharedPreferences("start_time", Context.MODE_PRIVATE)
    var start_time_LiveData = SharedPreferenceStringLiveData(start_time_pref, "start_time", start_time_pref.getString("start_time", "00 : 00")!!)
    //종료 시간
    val end_time_pref = context.getSharedPreferences("end_time", Context.MODE_PRIVATE)
    var end_time_LiveData = SharedPreferenceStringLiveData(end_time_pref, "end_time", end_time_pref.getString("end_time", "00 : 00")!!)
    //간격
    val interval_pref = context.getSharedPreferences("interval_time", Context.MODE_PRIVATE)
    var interval_LiveData = SharedPreferenceStringLiveData(interval_pref, "interval_time", interval_pref.getString("interval_time", "0시간")!!)

    fun setAlarmFlag(flag : Boolean){
        val editor = alarm_flag_pref.edit()
        editor.putBoolean("alarm_flag", flag)
        editor.apply()
        alarm_flag_LiveData = SharedPreferenceBooleanLiveData(alarm_flag_pref, "alarm_flag", flag)
    }

    fun setStartTime(value : String){
        val editor = start_time_pref.edit()
        editor.putString("start_time", value)
        editor.apply()
        start_time_LiveData = SharedPreferenceStringLiveData(start_time_pref, "start_time", value)
    }

    fun setEndTime(value : String){
        val editor = end_time_pref.edit()
        editor.putString("end_time", value)
        editor.apply()
        end_time_LiveData = SharedPreferenceStringLiveData(end_time_pref, "end_time", value)
    }
    fun setIntervalTime(value : String){
        val editor = interval_pref.edit()
        editor.putString("interval_time", value)
        editor.apply()
        interval_LiveData = SharedPreferenceStringLiveData(interval_pref, "interval_time", value)
    }


    fun setFav1LiveData(category : Int, amount : Int){
        val editor = fav_1_pref.edit()
        var sb = StringBuilder()
        sb.append(category)
        sb.append(" ")
        sb.append(amount)
        editor.putString("favorite_1", sb.toString())
        editor.apply()
        fav_1_livedata = SharedPreferenceStringLiveData(fav_1_pref, "favorite_1", sb.toString())
    }
    fun setFa2LiveData(category : Int, amount : Int){
        val editor = fav_2_pref.edit()
        var sb = StringBuilder()
        sb.append(category)
        sb.append(" ")
        sb.append(amount)
        editor.putString("favorite_2", sb.toString())
        editor.apply()
        fav_2_livedata = SharedPreferenceStringLiveData(fav_2_pref, "favorite_2", sb.toString())
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