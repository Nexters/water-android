package appvian.water.buddy.utilities

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.toBitmap
import appvian.water.buddy.R
import appvian.water.buddy.view.MainActivity
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.RandomAccess

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e("TAG","on receive")

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, getCurrentHours())
        calendar.add(Calendar.MINUTE, 30 )
        calendar.set(Calendar.SECOND, 0)
        if(context != null) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
                PendingIntent.getBroadcast(context, 0, intent, 0)
            }
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, alarmIntent)

            val alarm_flag_pref = context.getSharedPreferences("alarm_flag", Context.MODE_PRIVATE)
            val start_time_pref = context.getSharedPreferences("start_time", Context.MODE_PRIVATE)
            val end_time_pref = context.getSharedPreferences("end_time", Context.MODE_PRIVATE)

            if(alarm_flag_pref.getBoolean("alarm_flag",false)){
                //시간 체크
                val startHour = getHours(start_time_pref.getString("start_time","00 : 00")!!)
                val startMinutes = getMinutes(start_time_pref.getString("start_time", "00 : 00")!!)
                val endHour = getHours(end_time_pref.getString("end_time", "00 : 00")!!)
                val endMinutes = getMinutes(end_time_pref.getString("end_time","00 : 00")!!)
                val currHour = getCurrentHours()
                val currMinutes = getCurrentMinutes()
                val intervalMinutes = getIntervalMinutes(context)

                val start = startHour * 60 + startMinutes
                val end = endHour * 60 + endMinutes
                //가능한 알람 시간들
                val alarm_times = arrayListOf<Int>()
                if(start < end) {
                    var tmp = start
                    while (tmp <= end) {
                        alarm_times.add(tmp)
                        tmp += intervalMinutes
                        if (tmp >= 24 * 60) {
                            tmp -= 24 * 60
                        }
                    }
                }else if(start > end){
                    var tmp = start
                    while (tmp <= 24 * 60){
                        alarm_times.add(tmp)
                        tmp += intervalMinutes
                        if(tmp == 24 * 60){
                            tmp = 0
                            break
                        }
                    }
                    if(tmp > 24 * 60){
                        tmp -= 24 * 60
                    }
                    while (tmp <= end) {
                        alarm_times.add(tmp)
                        tmp += intervalMinutes
                    }
                }
                val target = currHour * 60 + currMinutes
                if(alarm_times.contains(target)){
                    triggerAlarm(context)
                }
            }
        }




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
    private fun getHours(value : String) : Int{
        var st = StringTokenizer(value)
        return st.nextToken().toInt()
    }
    private fun getMinutes(value : String) : Int{
        var st = StringTokenizer(value)
        st.nextToken()
        st.nextToken()
        return st.nextToken().toInt()
    }
    private fun getIntervalMinutes(context: Context) : Int{
        val interval_pref = context.getSharedPreferences("interval_time", Context.MODE_PRIVATE)
        //"30 분", "1 시간", "1 시간 30 분", "2 시간", "3 시간", "4 시간", "5 시간", "6 시간"
        when(interval_pref.getString("interval_time", "0 시간")){
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
    private fun triggerAlarm(context: Context){
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationIntent = Intent(context, MainActivity::class.java)

        notificationIntent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP
                or Intent.FLAG_ACTIVITY_SINGLE_TOP)

        val pendingI = PendingIntent.getActivity(
            context, 0,
            notificationIntent, 0
        )


        val builder =
            NotificationCompat.Builder(context, "default")


        //OREO API 26 이상에서는 채널 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setSmallIcon(R.drawable.ic_drink_1) //mipmap 사용시 Oreo 이상에서 시스템 UI 에러남
            builder.setLargeIcon(context.resources.getDrawable(R.drawable.google_play_store_icon, null).toBitmap())
            val channelName = "워비의 수분섭취 알"
            val description = "설정한 시간에 알람합니다."
            val importance = NotificationManager.IMPORTANCE_HIGH //소리와 알림메시지를 같이 보여줌
            val channel =
                NotificationChannel("default", channelName, importance)
            channel.description = description
            notificationManager?.createNotificationChannel(channel)
        } else builder.setSmallIcon(R.mipmap.google_play_store_icon) // Oreo 이하에서 mipmap 사용하지 않으면 Couldn't create icon: StatusBarIcon 에러남

        builder.setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setWhen(System.currentTimeMillis())
            .setContentTitle("워비")
            .setColor(context.resources.getColor(R.color.blue_1,null))
            .setContentText(getAlarmMsg(context))
            .setContentIntent(pendingI)

        notificationManager.notify(1234, builder.build())
    }
    private fun getAlarmMsg(context: Context) : String{
        val msgArr = context.resources.getStringArray(R.array.alarm_txt)
        var random = Random(System.currentTimeMillis())
        return msgArr[random.nextInt(4)]
    }

}