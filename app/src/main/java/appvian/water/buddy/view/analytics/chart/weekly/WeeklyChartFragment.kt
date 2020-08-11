package appvian.water.buddy.view.analytics.chart.weekly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import appvian.water.buddy.R
import appvian.water.buddy.databinding.FragmentWeeklyChartBinding
import appvian.water.buddy.model.repository.HomeRepository
import appvian.water.buddy.util.DrinkMapper
import appvian.water.buddy.viewmodel.analytics.WeeklyViewModel
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

class WeeklyChartFragment : Fragment() {
    private lateinit var binding: FragmentWeeklyChartBinding
    private lateinit var weeklyVm: WeeklyViewModel

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
            weeklyVm = WeeklyViewModel(HomeRepository(it))
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
                binding.weeklySysText.text = weeklyVm.strSapnBuilder(msg, color)
            } else if (it < 0) {
                val msg = getString(R.string.weekly_sys_minus, Math.abs(it))
                binding.weeklySysText.text = weeklyVm.strSapnBuilder(msg, color)
            } else {
                binding.weeklySysText.text = getString(R.string.weekly_sys_none)
            }
        })

        initSpinner()
    }

    private fun initSpinner() {
        context?.let {
            val spinnerAdapter = ArrayAdapter<String>(
                it,
                R.layout.daily_date_picker,
                R.id.daily_picker_text,
                weeklyVm.totalWeeks.map { getString(R.string.weekly_picker_item, it) })

            binding.weeklySpinner.adapter = spinnerAdapter
            binding.weeklySpinner.setSelection(weeklyVm.curWeek - 1)
            binding.weeklySpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {}

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        weeklyVm.curWeek = weeklyVm.totalWeeks[position]
                        weeklyVm.getWeekIntakeData()
                    }

                }
        }
    }

    private fun setWeeklyData(values: List<BarEntry>) {
        val day = resources.getStringArray(R.array.weekly)

        val dataSet = BarDataSet(values, "Intake List")
        dataSet.barSpacePercent = 70f
        dataSet.colors = values.map { barEntry ->
            if (barEntry.`val` >= weeklyVm.targetValue) resources.getColor(R.color.blue_1, null)
            else resources.getColor(R.color.blue_3, null)
        }

        dataSet.setDrawValues(false)

        val data = BarData(day, dataSet)
        drawWeeklyBarChart(data)

    }

    private fun drawWeeklyBarChart(data: BarData) {
        binding.weeklyBarchartByWeek.axisLeft.setAxisMaxValue(3.0f)

        binding.weeklyBarchartByWeek.data = data

        val limitLine = LimitLine(
            weeklyVm.targetValue,
            getString(R.string.limit_line_target, weeklyVm.targetValue)
        )
        limitLine.lineColor = resources.getColor(R.color.sub_red_1, null)
        limitLine.enableDashedLine(10f, 10f, 0f)

        binding.weeklyBarchartByWeek.axisLeft.addLimitLine(limitLine)

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
        binding.weeklyBarchartByTotal.xAxis.textSize = 14f
        binding.weeklyBarchartByTotal.xAxis.textColor =
            resources.getColor(R.color.chart_label, null)

        //remove legends & description
        binding.weeklyBarchartByTotal.setDescription("")
        binding.weeklyBarchartByTotal.legend.isEnabled = false

        //update chart
        binding.weeklyBarchartByTotal.invalidate()
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            WeeklyChartFragment()
    }
}
