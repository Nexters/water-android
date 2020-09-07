package appvian.water.buddy.view

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import appvian.water.buddy.R
import appvian.water.buddy.utilities.AlarmReceiver
import appvian.water.buddy.utilities.WidgetAlarmReceiver
import appvian.water.buddy.view.Intro.IntroActivity
import appvian.water.buddy.viewmodel.IntroViewModel
import com.bumptech.glide.Glide
import java.util.*


class SplashActivity : AppCompatActivity() {
    private lateinit var introViewModel: IntroViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        introViewModel =
            ViewModelProvider(this).get<IntroViewModel>(IntroViewModel::class.java)

        val splash : ImageView = findViewById(R.id.main_splash)
        Glide.with(this).load(R.drawable.main_splash).into(splash)


        startLoading()
    }

    private fun startLoading() {
        val handler = Handler()
        handler.postDelayed(Runnable {
            if(introViewModel.confirm_inform()){
                //00시에 위젯을 업데이트 시키는 알람 등록
                setWidgetAlarm()
                startActivity(Intent(baseContext, IntroActivity::class.java))
                startActivity(intent)
            }
            else{
                startActivity(Intent(baseContext, MainActivity::class.java))
                startActivity(intent)

            }

            finish()
        }, 2000)
    }
    private fun setWidgetAlarm(){
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.add(Calendar.DATE, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(this, WidgetAlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(this, 1, intent, 0)
        }
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, alarmIntent)
    }

}
