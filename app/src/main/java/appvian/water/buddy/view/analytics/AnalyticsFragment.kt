package appvian.water.buddy.view.analytics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import appvian.water.buddy.R
import appvian.water.buddy.databinding.FragmentAnalyticsBinding
import appvian.water.buddy.view.analytics.calendar.CalendarFragment
import appvian.water.buddy.view.analytics.chart.DailyChartFragment
import appvian.water.buddy.view.analytics.chart.WeeklyChartFragment
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*

private const val PAGE_NUM = 3

class AnalyticsFragment : Fragment() {
    private lateinit var binding: FragmentAnalyticsBinding

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

        binding.analyticsTitle.text = getString(R.string.analytics_header_title, Calendar.getInstance().get(Calendar.MONTH) + 1)
    }

    companion object {
        @JvmStatic
        fun newInstance() = AnalyticsFragment()
    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) :
        FragmentStateAdapter(fa) {
        private val fragmentList = arrayOf(
            CalendarFragment.newInstance(),
            WeeklyChartFragment.newInstance(),
            DailyChartFragment.newInstance()
        )

        override fun getItemCount(): Int = PAGE_NUM

        override fun createFragment(position: Int): Fragment = fragmentList[position]

    }
}