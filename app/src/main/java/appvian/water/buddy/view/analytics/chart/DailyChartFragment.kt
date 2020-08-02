package appvian.water.buddy.view.analytics.chart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import appvian.water.buddy.R
import appvian.water.buddy.databinding.FragmentDailyChartBinding
import appvian.water.buddy.model.repository.HomeRepository
import appvian.water.buddy.viewmodel.analytics.DailyChartViewModel
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.utils.ColorTemplate

class DailyChartFragment : Fragment() {

    private lateinit var binding: FragmentDailyChartBinding
    private lateinit var dailyVm: DailyChartViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_daily_chart, container, false)

        activity?.let {
            dailyVm = DailyChartViewModel(HomeRepository(it))
        }
        initUi()

        return binding.root
    }

    private fun initUi() {
        dailyVm.getDailyIntake()
        dailyVm.dailyIntake?.observe(viewLifecycleOwner, Observer {
            val pieValues = ArrayList<Entry>()
            val legend = ArrayList<String>()

            for((j, i) in it.withIndex()){
                android.util.Log.d("Daily Chart", "${i.category}, ${i.amount}")
                pieValues.add(Entry(i.amount.toFloat(), j))
                legend.add(i.category.toString())
            }

            val dataSet = PieDataSet(pieValues, "")
            dataSet.setColors(ColorTemplate.COLORFUL_COLORS)
            dataSet.setDrawValues(false)

            val data = PieData(legend, dataSet)

            binding.dailyPiechart.setUsePercentValues(true)
            binding.dailyPiechart.data = data
            binding.dailyPiechart.legend.isEnabled = false
            binding.dailyPiechart.setDrawSliceText(false)
            binding.dailyPiechart.setDescription("")
            binding.dailyPiechart.invalidate()
        })
    }

    companion object {

        @JvmStatic
        fun newInstance() = DailyChartFragment()
    }
}