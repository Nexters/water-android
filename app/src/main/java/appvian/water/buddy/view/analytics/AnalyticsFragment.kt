package appvian.water.buddy.view.analytics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.viewpager2.adapter.FragmentStateAdapter
import appvian.water.buddy.R
import appvian.water.buddy.databinding.FragmentAnalyticsBinding
import appvian.water.buddy.util.TimeUtil
import appvian.water.buddy.view.analytics.chart.daily.DailyChartFragment
import appvian.water.buddy.view.analytics.chart.weekly.WeeklyChartFragment
import appvian.water.buddy.view.analytics.month.MonthFragment
import appvian.water.buddy.view.modal.month.MonthCallbackListener
import appvian.water.buddy.view.modal.month.MonthModal
import appvian.water.buddy.viewmodel.analytics.AnalyticsViewModel
import com.google.android.material.tabs.TabLayoutMediator

private const val PAGE_NUM = 3

class AnalyticsFragment : Fragment(),
    MonthCallbackListener {
    private lateinit var binding: FragmentAnalyticsBinding
    private val analyVm: AnalyticsViewModel = AnalyticsViewModel()

    private val monthPickerListener = View.OnClickListener {
        val bottomSheetDialog = MonthModal(
            analyVm.curMonth.value ?: TimeUtil.month, this
        )
        bottomSheetDialog.show(childFragmentManager, "bottomSheet")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_analytics, container, false)
        initUi()

        return binding.root
    }

    private fun initUi() {
        activity?.let {
            binding.analyticsPager.adapter = ScreenSlidePagerAdapter(it)
        }

        val tabTitle = resources.getStringArray(R.array.analytics_tab_name)
        TabLayoutMediator(binding.analyticsTab, binding.analyticsPager) { tab, position ->
            tab.text = tabTitle[position]
        }.attach()

        analyVm.curMonth.observe(viewLifecycleOwner, Observer {
            binding.analyticsTitle.text = getString(
                R.string.analytics_header_title,
                it
            )
        })

        binding.analyticsTitle.setOnClickListener(monthPickerListener)
        binding.analyticsMonthPicker.setOnClickListener(monthPickerListener)
    }

    companion object {
        @JvmStatic
        fun newInstance() = AnalyticsFragment()
    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) :
        FragmentStateAdapter(fa) {
        private val fragmentList = arrayOf(
            MonthFragment.newInstance(analyVm),
            WeeklyChartFragment.newInstance(analyVm),
            DailyChartFragment.newInstance(analyVm)
        )

        override fun getItemCount(): Int = PAGE_NUM

        override fun createFragment(position: Int): Fragment = fragmentList[position]

    }

    override fun setMonth(month: Int) {
        analyVm.setMonth(month)
    }
}