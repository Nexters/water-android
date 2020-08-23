package appvian.water.buddy.view.settings

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import appvian.water.buddy.R
import appvian.water.buddy.databinding.ActivityAlarmSettingBinding
import appvian.water.buddy.utilities.AlarmReceiver
import appvian.water.buddy.utilities.Code
import appvian.water.buddy.viewmodel.AlarmViewModel
import java.text.SimpleDateFormat
import java.util.*

class AlarmSettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAlarmSettingBinding
    private lateinit var viewModel: AlarmViewModel
    private var alarmMgr: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent
    private var liveData_init_cnt = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_alarm_setting)
        viewModel = ViewModelProvider(this).get(AlarmViewModel::class.java)
        binding.viewModel = viewModel

        initUI()
    }
    private fun initUI(){
        viewModel.alarm_flag_LiveData.observe(this, Observer {
            binding.switchAlarm.isChecked = it
        })
        viewModel.start_time_LiveData.observe(this, Observer {
            binding.txtStartTime.text = it
            if(liveData_init_cnt >= 3) {
                setAlarm()
            }
            liveData_init_cnt++
        })
        viewModel.end_time_LiveData.observe(this, Observer {
            binding.txtEndTime.text = it
            if(liveData_init_cnt >= 3) {
                setAlarm()
            }
            liveData_init_cnt++
        })
        viewModel.interval_LiveData.observe(this, Observer {
            binding.txtIntervalTime.text = it
            if(liveData_init_cnt >= 3) {
                setAlarm()
            }
            liveData_init_cnt++
        })

        binding.layoutStartTime.setOnClickListener{
            val bottomSheet = TimePickerModal(Code.START_TIME_EDIT)
            val fragmentManager = supportFragmentManager
            bottomSheet.show(fragmentManager, bottomSheet.tag)
        }
        binding.layoutEndTime.setOnClickListener{
            val bottomSheet = TimePickerModal(Code.END_TIME_EDIT)
            val fragmentManager = supportFragmentManager
            bottomSheet.show(fragmentManager, bottomSheet.tag)
        }
        binding.layoutIntervalTime.setOnClickListener{
            val bottomSheet = TimeIntervalPickerModal()
            val fragmentManager = supportFragmentManager
            bottomSheet.show(fragmentManager, bottomSheet.tag)
        }
        binding.switchAlarm.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                viewModel.setAlarmFlag(true)
            }else{
                viewModel.setAlarmFlag(false)
            }
        }
    }

    private fun setAlarm(){
        cancelAlarm()
        Log.d("TAG", "set alarm")
        alarmMgr = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmIntent = Intent(applicationContext, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(applicationContext, 0, intent, 0)
        }

        //시작 시간부터 간격 시간 을 더해주면서  현재 시간보다 커지는 시간을 찾는다.
        //시작 08시이고 현재( 알림을 등록하는 지금) 시간이 9시 30분, 간격이 1시간 이라면
        // 오늘 08 < 09 30  09 < 09 30  10 > 09 30 이므로 10시부터  1시간 간격ㅇ로 울리도록 설정
        //on receieve에서  현재 시간 ( 00시 00분 으로 뽑아서 비교 )  이 시작 시간 ~ 종료 시간 사이라면 알림이 울리도록 한다.
        Log.d("TAG", "hour : " + viewModel.getStartHour() + " minutes : " + viewModel.getStartMinutes() )
        Log.d("TAG", "curr hour : " + getCurrentHours() + "curr minutes : " + getCurrentMinutes())
        var hour_alarm = viewModel.getStartHour()
        var minutes_alarm = viewModel.getStartMinutes()
        val hour_curr = getCurrentHours()
        val minutes_curr = getCurrentMinutes()
        val intervalTime = viewModel.getIntervalMinutes()
        var isNextDay = false
        while (true){
            Log.d("TAG", "while ")
            if(hour_alarm > hour_curr){
                break
            }else if(hour_alarm == hour_curr){
                if(minutes_alarm > minutes_curr){
                    break
                }else{
                    hour_alarm += intervalTime / 60
                    minutes_alarm += intervalTime % 60
                    if(minutes_alarm >= 60){
                        hour_alarm += minutes_alarm/60
                        minutes_alarm %= 60
                    }
                }
            }else{
                hour_alarm += intervalTime / 60
                minutes_alarm += intervalTime % 60
                if(minutes_alarm >= 60){
                    hour_alarm += minutes_alarm / 60
                    minutes_alarm %= 60
                }
            }
        }

        if(hour_alarm >= 24){
            isNextDay = true
            hour_alarm -= 24
        }

        Log.d("TAG",hour_alarm.toString() + " : " + minutes_alarm)
        Log.d("TAG","알림 간격 : " + intervalTime.toLong())
        // Set the alarm
        val calendar: Calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour_alarm)
            set(Calendar.MINUTE, minutes_alarm)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        var bTime = calendar.timeInMillis
        var interval = 1000 * 60 * 60 * 24
        //내일 울려야 한다면
        if(isNextDay){
            bTime += interval;
        }
        var sf = SimpleDateFormat("yyyy년 MM월 dd일 hh시 mm분 ss초")
        Log.d("TAG", sf.format(bTime))
        // setRepeating() lets you specify a precise custom interval--in this case,
        alarmMgr?.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, bTime, alarmIntent)

    }
    private fun cancelAlarm(){
        val alarmManager =
            applicationContext.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        val pendingIntent =
            PendingIntent.getService(applicationContext, 0, intent,
                0)
        if (pendingIntent != null && alarmManager != null) {
            alarmManager.cancel(pendingIntent)
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



}