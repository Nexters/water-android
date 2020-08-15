package appvian.water.buddy.view.settings

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import androidx.lifecycle.ViewModelProvider
import appvian.water.buddy.R
import appvian.water.buddy.utilities.Code
import appvian.water.buddy.viewmodel.AlarmViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.alarm_bottom_sheet.*
import kotlinx.android.synthetic.main.alarm_bottom_sheet.view.*
import java.text.SimpleDateFormat
import java.util.*

class TimePickerModal(var code : Int) : BottomSheetDialogFragment() {
    private lateinit var viewModel: AlarmViewModel
    override fun getTheme(): Int = R.style.RoundBottomSheetDialog
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireActivity(),theme)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.alarm_bottom_sheet, container, false)
        viewModel = ViewModelProvider(this).get(AlarmViewModel::class.java)

        initUI(v)

        return v
    }
    private fun initUI(v : View){
        v.img_cancel.setOnClickListener{
            onDestroyView()
            onDestroy()
        }
        when(code){
            Code.START_TIME_EDIT -> {
                v.txt_top.text = "시작 시간 설정하기"
                initTimePicker(v.timepicker, StringTokenizer(viewModel.start_time_LiveData.value))
            }
            Code.END_TIME_EDIT -> {
                v.txt_top.text = "종료 시간 설정하기"
                initTimePicker(v.timepicker, StringTokenizer(viewModel.end_time_LiveData.value))
            }
        }


        v.btn_save.setOnClickListener {
            when(code){
                Code.START_TIME_EDIT ->{
                    viewModel.setStartTime(getTimeString(v.timepicker))
                }
                Code.END_TIME_EDIT ->{
                    viewModel.setEndTime(getTimeString(v.timepicker))
                }
            }
            onDestroyView()
            onDestroy()
        }
    }
    private fun initTimePicker(timepicker : TimePicker, st : StringTokenizer){
        var initHour = st.nextToken()
        st.nextToken()
        var initMinute = st.nextToken()
        timepicker.hour = initHour.toInt()
        timepicker.minute = initMinute.toInt()
    }
    private fun getTimeString(timepicker: TimePicker) : String{
        var timeFormat = SimpleDateFormat("HH : mm")
        var calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, timepicker.hour)
        calendar.set(Calendar.MINUTE, timepicker.minute)
        return timeFormat.format(calendar.time)
    }
}