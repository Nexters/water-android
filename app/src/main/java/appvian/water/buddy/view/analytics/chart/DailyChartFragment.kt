package appvian.water.buddy.view.analytics.chart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import appvian.water.buddy.R
import appvian.water.buddy.databinding.FragmentDailyChartBinding

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

        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance() = DailyChartFragment()
    }
}