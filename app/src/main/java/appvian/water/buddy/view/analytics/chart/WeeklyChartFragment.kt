package appvian.water.buddy.view.analytics.chart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import appvian.water.buddy.R
import appvian.water.buddy.databinding.FragmentWeeklyChartBinding
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate

class WeeklyChartFragment : Fragment() {
    private lateinit var binding: FragmentWeeklyChartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_weekly_chart, container, false)
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
        intakeList.add(BarEntry(4f, 5))
        intakeList.add(BarEntry(1.3f, 6))

        val day = ArrayList<String>();
        day.add(getString(R.string.day_sun))
        day.add(getString(R.string.day_mon))
        day.add(getString(R.string.day_tue))
        day.add(getString(R.string.day_wed))
        day.add(getString(R.string.day_thu))
        day.add(getString(R.string.day_fri))
        day.add(getString(R.string.day_sat))

        binding.weeklyBarchart.animateY(5000)
        val dataSet = BarDataSet(intakeList, "Intake List")
        dataSet.barSpacePercent = 50f

        val data = BarData(day, dataSet)
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS)

        binding.weeklyBarchart.data = data
        binding.weeklyBarchart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.weeklyBarchart.setDescription("")
        binding.weeklyBarchart.legend.isEnabled = false
        binding.weeklyBarchart.axisLeft.addLimitLine(LimitLine(2f, "max line"))
    }

    companion object {

        @JvmStatic
        fun newInstance() = WeeklyChartFragment()
    }
}
