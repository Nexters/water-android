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
import appvian.water.buddy.util.TimeUtil
import appvian.water.buddy.view.modal.calendar.CalendarModal
import appvian.water.buddy.view.modal.calendar.CalendarTotalListener
import appvian.water.buddy.viewmodel.analytics.AnalyticsViewModel
import appvian.water.buddy.viewmodel.analytics.DailyChartViewModel
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet

class DailyChartFragment(val analyVm: AnalyticsViewModel) : Fragment(), CalendarTotalListener {

    private lateinit var binding: FragmentDailyChartBinding
    private lateinit var dailyVm: DailyChartViewModel
    val adapter: DailyAdapter = DailyAdapter()
    val legendAdapter: DailyLegendAdapter = DailyLegendAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_daily_chart, container, false)
        binding.lifecycleOwner = this

        activity?.let {
            dailyVm = DailyChartViewModel(HomeRepository(it))
        }
        initUi()

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        dailyVm.getDailyIntake()
    }

    private fun initUi() {
        dailyVm.getDailyIntake()
        binding.dailyChartDetail.adapter = adapter
        binding.dailyChartLegend.adapter = legendAdapter
        legendAdapter.addData(resources.getStringArray(DrinkMapper.drinkName))

        initSpinner()

        dailyVm.observeIntake.observe(viewLifecycleOwner, Observer {
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

        setSystemText()
        observeByMonthOrWeek()
    }

    private fun observeByMonthOrWeek() {
        analyVm.curYear.observe(viewLifecycleOwner, Observer {
            setDataFromAnalyVm()
        })

        analyVm.curMonth.observe(viewLifecycleOwner, Observer {
            setDataFromAnalyVm()
        })

        analyVm.curDay.observe(viewLifecycleOwner, Observer {
            setDataFromAnalyVm()
        })
    }

    private fun setDataFromAnalyVm() {
        dailyVm.curYear = analyVm.curYear.value ?: 1
        dailyVm.curMonth = analyVm.curMonth.value?.minus(1) ?: 1
        dailyVm.curDay = analyVm.curDay.value ?: 1

        setSpinnertext()
        dailyVm.getDailyIntake()
    }

    private fun initSpinner() {
        setSpinnertext()
        binding.dailyDatePicker.setOnClickListener {
            CalendarModal(
                analyVm.curYear.value ?: TimeUtil.year,
                analyVm.curMonth.value ?: TimeUtil.month,
                this
            ).show(
                childFragmentManager,
                ""
            )
        }
    }

    private fun setSpinnertext() {
        binding.dailyDatePicker.text = getString(R.string.daily_date, dailyVm.curDay)
    }

    private fun setNoneData() {
        val dataSet = PieDataSet(arrayListOf(Entry(1f, 1)), "")
        dataSet.color = resources.getColor(R.color.grey_2, null)
        dataSet.setDrawValues(false)

        updateChart(PieData(arrayListOf("none"), dataSet))
    }

    private fun setDailyData(it: List<Intake>) {
        adapter.setData(it)

        val pieValues = ArrayList<Entry>()
        val legend = ArrayList<String>()
        val colors = ArrayList<Int>()

        for ((j, i) in it.withIndex()) {
            pieValues.add(Entry(i.amount.toFloat(), j))
            legend.add(i.category.toString())
            colors.add(resources.getColor(DrinkMapper.drinkColor[i.category], null))
        }

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

    private fun setSystemText() {
        dailyVm.maxDrink.observe(viewLifecycleOwner, Observer {
            if (!it.equals(-2)) {
                val categoryName = resources.getStringArray(DrinkMapper.drinkName)[it]
                binding.dailySysText.text =
                    String.format(getString(R.string.daily_sys_max_drink), categoryName)
            } else {
                binding.dailySysText.text = getString(R.string.daily_sys_none_water)
            }
        })
    }

    companion object {

        @JvmStatic
        fun newInstance(analyVm: AnalyticsViewModel) =
            DailyChartFragment(analyVm)
    }

    override fun getCalendarTotal(year: Int, month: Int, day: Int) {
        analyVm.setYear(year, month, day)

        dailyVm.curYear = year
        dailyVm.curMonth = month - 1
        dailyVm.curDay = day

        binding.dailyDatePicker.text = getString(R.string.daily_date, dailyVm.curDay)
        dailyVm.getDailyIntake()
    }
}