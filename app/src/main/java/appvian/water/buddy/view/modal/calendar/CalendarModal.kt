package appvian.water.buddy.view.modal.calendar

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import appvian.water.buddy.R
import appvian.water.buddy.databinding.CalendarPickerBinding
import appvian.water.buddy.util.TimeUtil
import appvian.water.buddy.viewmodel.modal.CalendarViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CalendarModal : BottomSheetDialogFragment() {
    private lateinit var binding: CalendarPickerBinding
    private var curYear = TimeUtil.year
    private var curMonth = TimeUtil.month

    private val calendarAdapter = CalendarAdapter()
    private val calendarVm =
        CalendarViewModel()

    override fun getTheme(): Int = R.style.RoundBottomSheetDialog

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        BottomSheetDialog(requireActivity(), theme)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.calendar_picker, container, false)
        binding.vm = calendarVm

        initUi()

        return binding.root
    }

    private fun initUi() {
        binding.calPickerClose.setOnClickListener { dismiss() }
        binding.calConfirmBtn.setOnClickListener { dismiss() }

        setCalendarView()
        observeData()
    }

    private fun setCalendarView() {
        binding.calView.adapter = calendarAdapter

    }

    private fun observeData() {
        calendarVm.setCalendar()
        calendarVm.dateList.observe(viewLifecycleOwner, Observer {
            calendarAdapter.setDayList(it)
        })

        calendarVm.month.observe(viewLifecycleOwner, Observer {
            curMonth = it
            setCalYearMonthText()
        })

        calendarVm.year.observe(viewLifecycleOwner, Observer {
            curYear = it
            setCalYearMonthText()
        })
    }

    private fun setCalYearMonthText() {
        binding.calYearMonth.text = getString(R.string.calendar_year_month, curYear, curMonth)
    }
}