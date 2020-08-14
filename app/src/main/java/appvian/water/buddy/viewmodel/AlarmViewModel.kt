package appvian.water.buddy.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.room.SharedSQLiteStatement
import appvian.water.buddy.model.data.SharedPreferenceBooleanLiveData
import appvian.water.buddy.model.data.SharedPreferenceStringLiveData
import java.lang.StringBuilder

class AlarmViewModel (application: Application) : AndroidViewModel(application) {
    //알림 on/ off
    val alarm_flag_pref = application.getSharedPreferences("alarm_flag", Context.MODE_PRIVATE)
    var alarm_flag_LiveData : SharedPreferenceBooleanLiveData = SharedPreferenceBooleanLiveData(alarm_flag_pref, "alarm_flag", alarm_flag_pref.getBoolean("alarm_flag", false))
    //시작 시간
    val start_time_pref = application.getSharedPreferences("start_time", Context.MODE_PRIVATE)
    var start_time_LiveData = SharedPreferenceStringLiveData(start_time_pref, "start_time", start_time_pref.getString("start_time", "00 : 00")!!)
    //종료 시간
    val end_time_pref = application.getSharedPreferences("end_time", Context.MODE_PRIVATE)
    var end_time_LiveData = SharedPreferenceStringLiveData(end_time_pref, "end_time", end_time_pref.getString("end_time", "00 : 00")!!)
    //간격
    val interval_pref = application.getSharedPreferences("interval_time", Context.MODE_PRIVATE)
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

}