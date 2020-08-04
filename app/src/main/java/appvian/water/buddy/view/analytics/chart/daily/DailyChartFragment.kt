package appvian.water.buddy.view.analytics.chart.daily

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import appvian.water.buddy.R
import appvian.water.buddy.databinding.FragmentDailyChartBinding
import appvian.water.buddy.model.data.Intake
import appvian.water.buddy.model.repository.HomeRepository
import appvian.water.buddy.util.DrinkMapper
import appvian.water.buddy.viewmodel.analytics.DailyChartViewModel
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.utils.ColorTemplate

class DailyChartFragment : Fragment() {

    private lateinit var binding: FragmentDailyChartBinding
    private lateinit var dailyVm: DailyChartViewModel
    val adapter: DailyAdapter = DailyAdapter()

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
        binding.dailyChartDetail.adapter = adapter

        dailyVm.dailyIntake?.observe(viewLifecycleOwner, Observer {

            android.util.Log.d("size", "${it.size}")

            if (it.isNotEmpty()) {
                binding.dailyChartDetail.visibility = View.VISIBLE
                binding.dailyViewNone.visibility = View.INVISIBLE
                setDailyData(it)
            } else {
                binding.dailyChartDetail.visibility = View.INVISIBLE
                binding.dailyViewNone.visibility = View.VISIBLE
                setNoneData()
            }
        })
    }

    private fun setNoneData() {
        val dataSet = PieDataSet(arrayListOf(Entry(1f, 1)), "")
        dataSet.color = resources.getColor(R.color.grey_2, null)
        dataSet.setDrawValues(false)

        updateChart(PieData(arrayListOf("none"), dataSet))

        binding.dailySysTarget.text = getString(R.string.daily_sys_target, 0, 2000)
    }

    private fun setDailyData(it: List<Intake>) {
        adapter.setData(it)

        val pieValues = ArrayList<Entry>()
        val legend = ArrayList<String>()
        val colors = ArrayList<Int>()
        var sum = 0

        for ((j, i) in it.withIndex()) {
            android.util.Log.d("Daily Chart", "${i.category}, ${i.amount}")
            pieValues.add(Entry(i.amount.toFloat(), j))
            legend.add(i.category.toString())
            colors.add(resources.getColor(DrinkMapper.drinkColor[i.category], null))
            sum += i.amount
        }
        adapter.totalSum = sum
        binding.dailySysTarget.text = getString(R.string.daily_sys_target, sum, 2000)

        val dataSet = PieDataSet(pieValues, "")
        dataSet.setColors(colors)
        dataSet.setDrawValues(false)

        updateChart(PieData(legend, dataSet))
    }

    private fun updateChart(data: PieData) {
        binding.dailyPiechart.setUsePercentValues(true)
        binding.dailyPiechart.data = data
        binding.dailyPiechart.legend.isEnabled = false
        binding.dailyPiechart.setDrawSliceText(false)
        binding.dailyPiechart.setDescription("")
        binding.dailyPiechart.invalidate()
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            DailyChartFragment()
    }
}