package appvian.water.buddy.view.analytics.month

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import appvian.water.buddy.R
import appvian.water.buddy.databinding.FragmentMonthChartBinding
import appvian.water.buddy.model.repository.HomeRepository
import appvian.water.buddy.viewmodel.analytics.MonthViewModel

class MonthFragment : Fragment() {
    private lateinit var monthVm: MonthViewModel
    private lateinit var binding: FragmentMonthChartBinding
    private val rankAdapter = RankAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_month_chart, container, false)
        binding.lifecycleOwner = this

        activity?.let {
            monthVm = MonthViewModel(HomeRepository(it))
            binding.vm = monthVm
        }
        loadData()
        initUi()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun loadData() {
        monthVm.getMonthlyIntake()
    }

    private fun initUi() {
        binding.monthRankList.adapter = rankAdapter

        monthVm.monthlyRank.observe(viewLifecycleOwner, Observer {
            rankAdapter.setData(it)
            monthVm.loadMoreActive.value?.let { checkLoadAllData(it) }
        })

        setLoadMoreButton()
    }

    private fun setLoadMoreButton() {
        monthVm.loadMoreActive.observe(viewLifecycleOwner, Observer {
            checkLoadAllData(it)
        })
    }

    private fun checkLoadAllData(isLoadAll:Boolean){
        if (isLoadAll) {
            binding.monthLoadMore.visibility = View.GONE
            rankAdapter.setAllData()
        } else {
            binding.monthLoadMore.visibility = View.VISIBLE
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = MonthFragment()
    }
}