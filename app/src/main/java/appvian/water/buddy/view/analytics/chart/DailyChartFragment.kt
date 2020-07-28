package appvian.water.buddy.view.analytics.chart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import appvian.water.buddy.R
import appvian.water.buddy.databinding.FragmentDailyChartBinding
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.utils.ColorTemplate

class DailyChartFragment : Fragment() {

    private lateinit var binding: FragmentDailyChartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_daily_chart, container, false)

        initUi()

        return binding.root
    }

    private fun initUi() {
        binding.dailyPiechart.setUsePercentValues(true)

        val pieValues = ArrayList<Entry>()
        pieValues.add(Entry(1.0f, 0))
        pieValues.add(Entry(2.0f, 1))
        pieValues.add(Entry(3.0f, 2))
        pieValues.add(Entry(4.0f, 3))
        pieValues.add(Entry(5.0f, 4))

        val legend = ArrayList<String>()
        legend.add("1")
        legend.add("2")
        legend.add("3")
        legend.add("4")
        legend.add("5")

        val dataSet = PieDataSet(pieValues, "")
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS)

        val data = PieData(legend, dataSet)

        binding.dailyPiechart.data = data
        binding.dailyPiechart.animateXY(5000, 5000)
        binding.dailyPiechart.legend.position = Legend.LegendPosition.RIGHT_OF_CHART_CENTER
        binding.dailyPiechart.setDescription("")
        binding.dailyPiechart.invalidate()
    }

    companion object {

        @JvmStatic
        fun newInstance() = DailyChartFragment()
    }
}