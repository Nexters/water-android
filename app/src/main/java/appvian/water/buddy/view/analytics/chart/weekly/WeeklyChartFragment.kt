package appvian.water.buddy.view.analytics.chart.weekly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import appvian.water.buddy.R
import appvian.water.buddy.databinding.FragmentWeeklyChartBinding
import appvian.water.buddy.model.repository.HomeRepository
import appvian.water.buddy.viewmodel.analytics.WeeklyViewModel
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate

class WeeklyChartFragment : Fragment() {
    private lateinit var binding: FragmentWeeklyChartBinding
    private lateinit var viewModel: WeeklyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_weekly_chart, container, false)

        context?.let {
            viewModel = WeeklyViewModel(HomeRepository(it))
        }

        init()

        return binding.root
    }

    private fun init() {

        val intakeList = ArrayList<BarEntry>()
        intakeList.add(BarEntry(2f, 0))
        intakeList.add(BarEntry(1f, 1))
        intakeList.add(BarEntry(1f, 2))
        intakeList.add(BarEntry(1.5f, 3))
        intakeList.add(BarEntry(0.5f, 4))
        intakeList.add(BarEntry(2f, 5))
        intakeList.add(BarEntry(1.3f, 6))

        val day = resources.getStringArray(R.array.weekly)

        binding.weeklyBarchartByWeek.animateY(5000)
        val dataSet = BarDataSet(intakeList, "Intake List")
        dataSet.barSpacePercent = 50f

        val data = BarData(day, dataSet)
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS)

        binding.weeklyBarchartByWeek.data = data
        binding.weeklyBarchartByWeek.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.weeklyBarchartByWeek.setDescription("")
        binding.weeklyBarchartByWeek.legend.isEnabled = false
        binding.weeklyBarchartByWeek.axisLeft.addLimitLine(LimitLine(2f, ""))
        binding.weeklyBarchartByWeek.axisRight.setDrawLabels(false)

    }

    companion object {

        @JvmStatic
        fun newInstance() =
            WeeklyChartFragment()
    }
}
