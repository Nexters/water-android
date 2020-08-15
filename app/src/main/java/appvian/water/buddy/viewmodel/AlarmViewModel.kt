package appvian.water.buddy.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.room.SharedSQLiteStatement
import appvian.water.buddy.model.data.SharedPreferenceBooleanLiveData
import appvian.water.buddy.model.data.SharedPreferenceStringLiveData
import appvian.water.buddy.model.repository.SharedPrefsRepository
import java.lang.StringBuilder
import java.util.*

class AlarmViewModel (application: Application) : AndroidViewModel(application) {
    private val repository = SharedPrefsRepository(application)
    //알림 on/ off
    var alarm_flag_LiveData = repository.alarm_flag_LiveData
    //시작 시간
    var start_time_LiveData = repository.start_time_LiveData
    //종료 시간
    var end_time_LiveData = repository.end_time_LiveData
    //간격
    var interval_LiveData = repository.interval_LiveData

    fun setAlarmFlag(flag : Boolean){
        repository.setAlarmFlag(flag)
    }

    fun setStartTime(value : String){
        repository.setStartTime(value)
    }

    fun setEndTime(value : String){
        repository.setEndTime(value)
    }
    fun setIntervalTime(value : String){
        repository.setIntervalTime(value)
    }

    fun getIntervalMinutes() : Int {
        //"30 분", "1 시간", "1 시간 30 분", "2 시간", "3 시간", "4 시간", "5 시간", "6 시간"
        when(interval_LiveData.value){
            "30 분" -> {
                return 30
            }
            "1 시간" -> {
                return 60
            }
            "1 시간 30 분" -> {
                return 90
            }
            "2 시간" -> {
                return 120
            }
            "3 시간" ->{
                return 180
            }
            "4 시간" ->{
                return 240
            }
            "5 시간" ->{
                return 300
            }
            "6 시간" ->{
                return 360
            }
        }
        return 60
    }
    fun getStartHour() : Int {
        var st = StringTokenizer(start_time_LiveData.value)
        return st.nextToken().toInt()
    }
    fun getStartMinutes() : Int{
        var st = StringTokenizer(start_time_LiveData.value)
        st.nextToken()
        st.nextToken()
        return st.nextToken().toInt()
    }

}