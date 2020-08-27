package appvian.water.buddy.view.analytics.chart.weekly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import appvian.water.buddy.R
import appvian.water.buddy.databinding.FragmentWeeklyChartBinding
import appvian.water.buddy.model.repository.HomeRepository
import appvian.water.buddy.model.repository.SharedPrefsRepository
import appvian.water.buddy.util.DrinkMapper
import appvian.water.buddy.util.TimeUtil
import appvian.water.buddy.view.modal.calendar.CalendarModal
import appvian.water.buddy.view.modal.calendar.CalendarTotalListener
import appvian.water.buddy.viewmodel.analytics.AnalyticsViewModel
import appvian.water.buddy.viewmodel.analytics.WeeklyViewModel
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import java.util.*

class WeeklyChartFragment(val analyVm: AnalyticsViewModel) : Fragment() {
    private lateinit var binding: FragmentWeeklyChartBinding
    private lateinit var weeklyVm: WeeklyViewModel

    private val calendarTotalListener: CalendarTotalListener = object : CalendarTotalListener {
        override fun getCalendarTotal(year: Int, month: Int, day: Int) {
            analyVm.setYear(year, month, day)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_weekly_chart, container, false)
        binding.lifecycleOwner = this

        activity?.let {
            weeklyVm = WeeklyViewModel(SharedPrefsRepository(requireContext()), HomeRepository(it))
        }

        initUi()

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        weeklyVm.getWeekIntakeData()
    }

    private fun initUi() {
        weeklyVm.getWeekIntakeData()
        weeklyVm.getTotalWeekIntakeData()

        weeklyVm.weekObserve.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                setWeeklyData(it)
            }
        })

        weeklyVm.weekTotalObserve.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                setWeeklyTotalData(it)
            }
        })

        weeklyVm.sysObserve.observe(viewLifecycleOwner, Observer {
            val color = resources.getColor(R.color.blue_1, null)
            if (it > 0) {
                val msg = getString(R.string.weekly_sys_plus, Math.abs(it))
                binding.weeklySysText.text = weeklyVm.strSpanBuilder(msg, color)
            } else if (it < 0) {
                val msg = getString(R.string.weekly_sys_minus, Math.abs(it))
                binding.weeklySysText.text = weeklyVm.strSpanBuilder(msg, color)
            } else {
                binding.weeklySysText.text = getString(R.string.weekly_sys_none)
            }
        })

        initSpinner()
        observeByMonthOrDay()
    }

    fun observeByMonthOrDay() {
        analyVm.curYear.observe(viewLifecycleOwner, Observer { setDataFromAnalyVm() })
        analyVm.curMonth.observe(viewLifecycleOwner, Observer { setDataFromAnalyVm() })
        analyVm.curDay.observe(viewLifecycleOwner, Observer { setDataFromAnalyVm() })
    }

    private fun setDataFromAnalyVm() {
        weeklyVm.curYear = analyVm.curYear.value ?: 1970
        weeklyVm.curMonth = analyVm.curMonth.value?.minus(1) ?: 1
        weeklyVm.curDay = analyVm.curDay.value ?: 1


        setWeeklySpinnertext()
        weeklyVm.getWeekIntakeData()
        weeklyVm.getTotalWeekIntakeData()
    }

    private fun initSpinner() {
        setWeeklySpinnertext()

        binding.weeklySpinner.setOnClickListener {
            val now = Calendar.getInstance()
            CalendarModal(
                analyVm.curYear.value ?: now[Calendar.YEAR],
                analyVm.curMonth.value ?: now[Calendar.MONTH] + 1, calendarTotalListener
            ).show(childFragmentManager, "")
        }
    }

    private fun setWeeklySpinnertext() {
        binding.weeklySpinnerTv.text = getString(
            R.string.weekly_picker_item,
            TimeUtil.getWeekOfMonth(weeklyVm.curYear, weeklyVm.curMonth, weeklyVm.curDay)
        )
    }

    private fun setWeeklyData(values: List<BarEntry>) {
        val day = resources.getStringArray(R.array.weekly)

        val dataSet = BarDataSet(values, "Intake List")
        weeklyVm.targetValue.observe(viewLifecycleOwner, Observer {
            dataSet.barSpacePercent = 70f
            dataSet.colors = values.map { barEntry ->
                if (barEntry.`val` >= it) resources.getColor(R.color.blue_1, null)
                else resources.getColor(R.color.blue_3, null)
            }
            dataSet.setDrawValues(false)
            binding.weeklyBarchartByWeek.invalidate()
        })

        val data = BarData(day, dataSet)
        drawWeeklyBarChart(data)

    }

    private fun drawWeeklyBarChart(data: BarData) {
        binding.weeklyBarchartByWeek.axisLeft.setAxisMaxValue(3.0f)

        binding.weeklyBarchartByWeek.data = data

        weeklyVm.targetValue.observe(viewLifecycleOwner, Observer {
            //clear limit line
            binding.weeklyBarchartByWeek.axisLeft.limitLines.clear()

            val limitLine = LimitLine(
                weeklyVm.targetValue.value ?: 0f,
                getString(R.string.weekly_chart_y_format, it)
            )
            limitLine.lineColor = resources.getColor(R.color.sub_red_1, null)
            limitLine.textColor = resources.getColor(R.color.sub_red_1, null)
            limitLine.textSize = 10f
            limitLine.typeface =
                ResourcesCompat.getFont(requireContext(), R.font.spoqa_han_sans_bold)

            binding.weeklyBarchartByWeek.axisLeft.addLimitLine(limitLine)
        })

        binding.weeklyBarchartByWeek.axisLeft.axisLineColor =
            resources.getColor(R.color.transparent, null)
        binding.weeklyBarchartByWeek.xAxis.axisLineColor =
            resources.getColor(R.color.transparent, null)

        //text color & size
        binding.weeklyBarchartByWeek.axisLeft.textSize = 12f
        binding.weeklyBarchartByWeek.axisLeft.textColor =
            resources.getColor(R.color.chart_label, null)
        binding.weeklyBarchartByWeek.xAxis.textSize = 12f
        binding.weeklyBarchartByWeek.xAxis.textColor = resources.getColor(R.color.chart_label, null)
        binding.weeklyBarchartByWeek.xAxis.typeface =
            ResourcesCompat.getFont(requireContext(), R.font.spoqa_han_sans_regular)

        binding.weeklyBarchartByWeek.setDescription("")
        binding.weeklyBarchartByWeek.legend.isEnabled = false

        //remove right axis
        binding.weeklyBarchartByWeek.axisRight.isEnabled = false

        //remove grid line
        binding.weeklyBarchartByWeek.xAxis.setDrawGridLines(false)
        binding.weeklyBarchartByWeek.axisLeft.setDrawGridLines(false)

        binding.weeklyBarchartByWeek.xAxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE

        //set y axis format
        binding.weeklyBarchartByWeek.axisLeft.valueFormatter = WeeklyValueFormatter

        //update chart
        binding.weeklyBarchartByWeek.invalidate()
    }

    private fun setWeeklyTotalData(values: List<BarEntry>) {
        val xLabels = weeklyVm.monthWeek.map { getString(R.string.weekly_month_week, it[0], it[1]) }

        val dataSet = BarDataSet(values, "total weekly data")
        dataSet.colors = DrinkMapper.yearWeekDrinkPalette.map { resources.getColor(it, null) }
        dataSet.barSpacePercent = 40f
        dataSet.valueTextSize = 15f
        dataSet.valueFormatter = WeeklyValueFormatter
        dataSet.valueTypeface =
            ResourcesCompat.getFont(requireContext(), R.font.spoqa_han_sans_bold)

        val data = BarData(xLabels, dataSet)

        drawWeeklyTotalBarChart(data)
    }

    private fun drawWeeklyTotalBarChart(data: BarData) {
        binding.weeklyBarchartByTotal.data = data

        //remove grid
        binding.weeklyBarchartByTotal.axisRight.isEnabled = false
        binding.weeklyBarchartByTotal.axisLeft.isEnabled = false
        binding.weeklyBarchartByTotal.xAxis.setDrawGridLines(false)

        //X Axis to bottom
        binding.weeklyBarchartByTotal.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.weeklyBarchartByTotal.xAxis.yOffset = 0f

        //line transparent
        binding.weeklyBarchartByTotal.axisLeft.axisLineColor =
            resources.getColor(R.color.transparent, null)
        binding.weeklyBarchartByTotal.xAxis.axisLineColor =
            resources.getColor(R.color.transparent, null)

        //text size
        binding.weeklyBarchartByTotal.xAxis.textSize = 12f
        binding.weeklyBarchartByTotal.xAxis.textColor =
            resources.getColor(R.color.chart_label, null)
        binding.weeklyBarchartByTotal.xAxis.typeface =
            ResourcesCompat.getFont(requireContext(), R.font.spoqa_han_sans_regular)

        //remove legends & description
        binding.weeklyBarchartByTotal.setDescription("")
        binding.weeklyBarchartByTotal.legend.isEnabled = false

        //update chart
        binding.weeklyBarchartByTotal.invalidate()
    }

    companion object {

        @JvmStatic
        fun newInstance(analyVm: AnalyticsViewModel) =
            WeeklyChartFragment(analyVm)
    }
}
