package appvian.water.buddy.view.analytics.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import appvian.water.buddy.R
import appvian.water.buddy.databinding.FragmentCalendarBinding
import appvian.water.buddy.viewmodel.CalendarViewModel

class CalendarFragment : Fragment() {

    private val adapter = CalendarAdapter()
    private val viewModel = CalendarViewModel()
    private lateinit var binding: FragmentCalendarBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_calendar, container, false)
        binding.viewModel = viewModel
        initUi()
        return binding.root
    }

    private fun initUi() {
        binding.calVeiw.adapter = adapter

        viewModel.setCalendar()

        viewModel.year.observe(
            viewLifecycleOwner,
            Observer<Int> { intValue -> binding.calYear.text = intValue.toString() })

        viewModel.month.observe(
            viewLifecycleOwner,
            Observer<Int> { intValue -> binding.calMonth.text = "${intValue}월" })

        viewModel.dateList.observe(
            viewLifecycleOwner,
            Observer { calData -> adapter.setDateList(calData) }
        )
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CalendarFragment()
    }
}