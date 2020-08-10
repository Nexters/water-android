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
        weeklyVm.weekObserve.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                android.util.Log.d("weekly data", "${it.get(0).vals}")
                setData(it)
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

    private fun setData(values: List<BarEntry>) {

        val day = resources.getStringArray(R.array.weekly)
        val dataSet = BarDataSet(values, "Intake List")
        dataSet.barSpacePercent = 70f

        val data = BarData(day, dataSet)
        dataSet.color = resources.getColor(R.color.blue_1, null)
        dataSet.setDrawValues(false)

        updateChart(data)
    }

    private fun updateChart(data: BarData) {
        binding.weeklyBarchartByWeek.axisLeft.setAxisMaxValue(3.0f)

        binding.weeklyBarchartByWeek.data = data

        val limitLine = LimitLine(weeklyVm.targetValue, "")
        limitLine.lineColor = resources.getColor(R.color.sub_red_1, null)
        binding.weeklyBarchartByWeek.axisLeft.addLimitLine(limitLine)

        binding.weeklyBarchartByWeek.axisLeft.axisLineColor =
            resources.getColor(R.color.transparent, null)
        binding.weeklyBarchartByWeek.xAxis.axisLineColor =
            resources.getColor(R.color.transparent, null)

        binding.weeklyBarchartByWeek.setDescription("")
        binding.weeklyBarchartByWeek.legend.isEnabled = false

        //remove right axis
        binding.weeklyBarchartByWeek.axisRight.isEnabled = false

        //remove grid line
        binding.weeklyBarchartByWeek.xAxis.setDrawGridLines(false)
        binding.weeklyBarchartByWeek.axisLeft.setDrawGridLines(false)

        binding.weeklyBarchartByWeek.xAxis.position = XAxis.XAxisPosition.BOTTOM

        //update chart
        binding.weeklyBarchartByWeek.invalidate()
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            WeeklyChartFragment()
    }
}
