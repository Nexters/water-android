package appvian.water.buddy.view.modal.calendar

import android.app.Dialog
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import appvian.water.buddy.R
import appvian.water.buddy.databinding.CalendarPickerBinding
import appvian.water.buddy.util.TimeUtil
import appvian.water.buddy.viewmodel.modal.CalendarViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*

class CalendarModal(
    private var curYear: Int,
    var curMonth: Int,
    val calendarTotalListener: CalendarTotalListener
) : BottomSheetDialogFragment(),
    CalendarDayListener {
    private lateinit var binding: CalendarPickerBinding
    private var selectDay = -1

    private val calendarVm =
        CalendarViewModel(curYear, curMonth)
    private val calendarAdapter = CalendarAdapter(calendarVm, this)

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
        setDialogExpand()

        binding.calPickerClose.setOnClickListener { dismiss() }
        binding.calConfirmBtn.setOnClickListener {
            if (selectDay != -1)
                calendarTotalListener.getCalendarTotal(curYear, curMonth, selectDay)
            dismiss()
        }
        binding.calView.adapter = calendarAdapter

        observeData()
    }

    private fun setDialogExpand() {
        //modal 길이 수정
        dialog?.let { dialog ->
            dialog.setOnShowListener { it ->
                val bottomSheetDialog =
                    (it as BottomSheetDialog).findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
                bottomSheetDialog?.let {
                    BottomSheetBehavior.from(it).state = BottomSheetBehavior.STATE_EXPANDED
                    BottomSheetBehavior.from(it).skipCollapsed = true
                    BottomSheetBehavior.from(it).isHideable = true
                }
            }
        }
    }

    private fun observeData() {
        calendarVm.setCalendar()
        calendarVm.dateList.observe(viewLifecycleOwner, Observer {
            calendarAdapter.setDayList(it)
        })

        calendarVm.month.observe(viewLifecycleOwner, Observer {
            curMonth = it
            setCalYearMonthText()
            setNextButtonVisible(curMonth)
        })

        calendarVm.year.observe(viewLifecycleOwner, Observer {
            curYear = it
            setCalYearMonthText()
        })
    }

    private fun setNextButtonVisible(curMonth: Int) {
        val today = TimeUtil.getCalendarInstance()
        val year = today[Calendar.YEAR]
        val month = today[Calendar.MONTH] + 1

        if(year == calendarVm.year.value?: curYear && curMonth >= month)
            binding.calNext.visibility = View.INVISIBLE
        else
            binding.calNext.visibility = View.VISIBLE
    }

    private fun setCalYearMonthText() {
        binding.calYearMonth.text = getString(R.string.calendar_year_month, curYear, curMonth)
    }

    override fun getCalendarDay(day: Int) {
        if (day > 0) {
            selectDay = day
            binding.calConfirmBtn.isClickable = true
            binding.calConfirmBtn.backgroundTintList =
                ColorStateList.valueOf(resources.getColor(R.color.blue_1, null))
        } else {
            selectDay = -1
            binding.calConfirmBtn.isClickable = false
            binding.calConfirmBtn.isFocusable = false
            binding.calConfirmBtn.backgroundTintList =
                ColorStateList.valueOf(resources.getColor(R.color.blue_2, null))
        }
    }
}