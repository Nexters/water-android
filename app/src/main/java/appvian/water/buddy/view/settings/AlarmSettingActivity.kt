package appvian.water.buddy.view.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import appvian.water.buddy.R
import appvian.water.buddy.databinding.ActivityAlarmSettingBinding
import appvian.water.buddy.utilities.Code
import appvian.water.buddy.viewmodel.AlarmViewModel

class AlarmSettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAlarmSettingBinding
    private lateinit var viewModel: AlarmViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_alarm_setting)
        viewModel = ViewModelProvider(this).get(AlarmViewModel::class.java)
        binding.viewModel = viewModel

        initUI()
    }
    private fun initUI(){
        viewModel.alarm_flag_LiveData.observe(this, Observer {

        })
        viewModel.start_time_LiveData.observe(this, Observer {
            binding.txtStartTime.text = it
        })
        viewModel.end_time_LiveData.observe(this, Observer {
            binding.txtEndTime.text = it
        })
        viewModel.interval_LiveData.observe(this, Observer {
            binding.txtIntervalTime.text = it
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
            val bottomSheet = TimePickerModal(Code.INTERVAL_TIME_EDIT)
            val fragmentManager = supportFragmentManager
            bottomSheet.show(fragmentManager, bottomSheet.tag)
        }

    }
}