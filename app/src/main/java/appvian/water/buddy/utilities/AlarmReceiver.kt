package appvian.water.buddy.utilities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import appvian.water.buddy.R
import appvian.water.buddy.view.MainActivity
import appvian.water.buddy.viewmodel.AlarmViewModel
import java.text.SimpleDateFormat
import java.util.*

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("TAG","on receive")
        val notificationManager =
            context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationIntent = Intent(context, MainActivity::class.java)

        notificationIntent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP
                or Intent.FLAG_ACTIVITY_SINGLE_TOP)

        val pendingI = PendingIntent.getActivity(
            context, 0,
            notificationIntent, 0
        )


        val builder =
            NotificationCompat.Builder(context!!, "default")


        //OREO API 26 이상에서는 채널 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setSmallIcon(R.drawable.ic_launcher_foreground) //mipmap 사용시 Oreo 이상에서 시스템 UI 에러남
            val channelName = "매일 알람 채널"
            val description = "매일 정해진 시간에 알람합니다."
            val importance = NotificationManager.IMPORTANCE_HIGH //소리와 알림메시지를 같이 보여줌
            val channel =
                NotificationChannel("default", channelName, importance)
            channel.description = description
            notificationManager?.createNotificationChannel(channel)
        } else builder.setSmallIcon(R.mipmap.ic_launcher) // Oreo 이하에서 mipmap 사용하지 않으면 Couldn't create icon: StatusBarIcon 에러남

        builder.setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setWhen(System.currentTimeMillis())
            .setTicker("{Time to watch some cool stuff!}")
            .setContentTitle("워비")
            .setColor(context.resources.getColor(R.color.blue_1,null))
            .setContentText("물 드세요")
            .setColor(context.resources.getColor(R.color.blue_1, null))
            .setContentInfo("물 먹엉")
            .setContentIntent(pendingI)

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
            Log.d("TAG", "start : " + startHour + " : " + startMinutes + "end : " + endHour + " : " + endMinutes)
            if(startHour <= currHour && currHour <= endHour){
                if(startHour == currHour){
                    if(startMinutes <= currMinutes){
                        notificationManager.notify(1234, builder.build())
                        Log.d("TAG","알림 울림 : " + currHour + " : " + currMinutes)
                    }
                }else if(endHour == currHour){
                    if(currMinutes <= endMinutes){
                        notificationManager.notify(1234, builder.build())
                        Log.d("TAG","알림 울림 : " + currHour + " : " + currMinutes)
                    }
                }else{
                    notificationManager.notify(1234, builder.build())
                    Log.d("TAG","알림 울림 : " + currHour + " : " + currMinutes)
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

}