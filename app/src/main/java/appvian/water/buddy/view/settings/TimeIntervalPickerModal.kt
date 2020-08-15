package appvian.water.buddy.view.settings

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import appvian.water.buddy.R
import appvian.water.buddy.viewmodel.AlarmViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.num_picker_bottom_sheet.view.*

class TimeIntervalPickerModal : BottomSheetDialogFragment() {
    private lateinit var viewModel: AlarmViewModel
    val display_values = arrayOf("30 분", "1 시간", "1 시간 30 분", "2 시간", "3 시간", "4 시간", "5 시간", "6 시간")
    override fun getTheme(): Int = R.style.RoundBottomSheetDialog
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireActivity(),theme)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.num_picker_bottom_sheet, container, false)
        viewModel = ViewModelProvider(this).get(AlarmViewModel::class.java)

        initUI(v)
        return v
    }
    private fun initUI(v : View){
        v.img_cancel.setOnClickListener{
            onDestroyView()
            onDestroy()
        }

        v.numberpicker.minValue = 0
        v.numberpicker.maxValue = 7
        v.numberpicker.displayedValues = display_values

        v.btn_save.setOnClickListener {
            viewModel.setIntervalTime(display_values[v.numberpicker.value])
            onDestroyView()
            onDestroy()
        }
    }
}