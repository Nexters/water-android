package appvian.water.buddy.view.modal

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import appvian.water.buddy.R
import appvian.water.buddy.databinding.MonthPickerBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MonthModal : BottomSheetDialogFragment() {
    private lateinit var binding: MonthPickerBinding
    var curVal = 1

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
        binding.monthPickerConfirmBtn.setOnClickListener { dismiss() }
        setMonthPicker()
    }

    private fun setMonthPicker() {
        binding.monthPickerSelect.minValue = 1
        binding.monthPickerSelect.maxValue = 12
        binding.monthPickerSelect.wrapSelectorWheel = false
        binding.monthPickerSelect.setFormatter { resources.getString(R.string.month, it) }
        binding.monthPickerSelect.setOnValueChangedListener { picker, oldVal, newVal ->
            curVal = newVal
        }
    }
}