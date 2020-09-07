package appvian.water.buddy.utilities

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import appvian.water.buddy.model.repository.SharedPrefsRepository
import java.text.SimpleDateFormat
import java.util.*

class DeviceBootReceiver : BroadcastReceiver() {
    private lateinit var alarmMgr: AlarmManager
    private lateinit var alarmIntent: PendingIntent
    private lateinit var repository: SharedPrefsRepository
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {

            setAlarm(context)
        }
    }
    private fun setAlarm(context: Context){
        setWidgetAlarm(context)
        repository = SharedPrefsRepository(context)

        alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(context, 0, intent, 0)
        }

        var hour_alarm = getStartHour()
        var minutes_alarm = getStartMinutes()
        var hour_curr = getCurrentHours()
        var minutes_curr = getCurrentMinutes()
        var isNextDay = false
        while (true){
            if(hour_alarm > hour_curr){
                break
            }else if(hour_alarm == hour_curr){
                if(minutes_alarm > minutes_curr){
                    break
                }else{
                    hour_alarm += getIntervalMinutes()/60
                    minutes_alarm += getIntervalMinutes()%60
                    if(minutes_alarm >= 60){
                        hour_alarm += minutes_alarm/60
                        minutes_alarm %= 60
                    }
                }
            }else{
                hour_alarm +=  getIntervalMinutes()/60
                minutes_alarm += getIntervalMinutes()%60
                if(minutes_alarm >= 60){
                    hour_alarm += minutes_alarm/60
                    minutes_alarm %= 60
                }
            }
        }

        if(hour_alarm >= 24){
            isNextDay = true
            hour_alarm -= 24
        }
        // Set the alarm
        val calendar: Calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour_alarm)
            set(Calendar.MINUTE, minutes_alarm)
            set(Calendar.SECOND, 0)
        }
        var bTime = calendar.timeInMillis
        var interval = 1000 * 60 * 60 * 24
        //내일 울려야 한다면
        if(isNextDay){
            bTime += interval;
        }
        // setRepeating() lets you specify a precise custom interval--in this case,
        alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, bTime, alarmIntent)
    }
    fun getIntervalMinutes() : Int {
        //"30 분", "1 시간", "1 시간 30 분", "2 시간", "3 시간", "4 시간", "5 시간", "6 시간"
        when(repository.interval_LiveData.value){
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
        var st = StringTokenizer(repository.start_time_LiveData.value)
        return st.nextToken().toInt()
    }
    fun getStartMinutes() : Int{
        var st = StringTokenizer(repository.start_time_LiveData.value)
        st.nextToken()
        st.nextToken()
        return st.nextToken().toInt()
    }
    private fun getCurrentHours() : Int {
        var hourformat = SimpleDateFormat("HH")
        var str = hourformat.format(Date(System.currentTimeMillis()))
        return str.toInt()
    }
    private fun getCurrentMinutes() : Int{
        var minutesformat = SimpleDateFormat("mm")
        var str = minutesformat.format(Date(System.currentTimeMillis()))
        return str.toInt()
    }
    private fun setWidgetAlarm(context: Context){
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.add(Calendar.DATE, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, WidgetAlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(context, 1, intent, 0)
        }
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, alarmIntent)
    }
}