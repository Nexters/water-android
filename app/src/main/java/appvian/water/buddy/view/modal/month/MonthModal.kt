package appvian.water.buddy.view.modal.month

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import appvian.water.buddy.R
import appvian.water.buddy.databinding.MonthPickerBinding
import appvian.water.buddy.util.TimeUtil
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*

class MonthModal(val curYear:Int, curMonth: Int, val monthCallbackListener: MonthCallbackListener) : BottomSheetDialogFragment() {
    private lateinit var binding: MonthPickerBinding
    private val monthArray = IntArray(12){ i -> i + 1}
    var curVal = curMonth

    override fun getTheme(): Int = R.style.RoundBottomSheetDialog

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        BottomSheetDialog(requireActivity(), theme)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<MonthPickerBinding>(
            inflater,
            R.layout.month_picker,
            container,
            false
        )

        initUi()

        return binding.root
    }

    private fun initUi() {
        binding.monthPickerClose.setOnClickListener { dismiss() }
        binding.monthPickerConfirmBtn.setOnClickListener {
            monthCallbackListener.setMonth(curVal)
            dismiss()
        }
        setMonthPicker()
    }

    private fun setMonthPicker() {
        binding.monthPickerSelect.minValue = 1
        binding.monthPickerSelect.maxValue = setMaxMonth()
        binding.monthPickerSelect.wrapSelectorWheel = false
        binding.monthPickerSelect.value = curVal
        binding.monthPickerSelect.displayedValues = monthArray.map {
            resources.getString(R.string.month_select, curYear, it)
        }.toTypedArray() as Array<String>

        binding.monthPickerSelect.setOnValueChangedListener { picker, oldVal, newVal ->
            curVal = newVal
        }
    }

    private fun setMaxMonth() : Int {
        val today = TimeUtil.getCalendarInstance()

        val year = today[Calendar.YEAR]
        val month = today[Calendar.MONTH] + 1

        if(year == curYear)
            return month

        return 12
    }
}