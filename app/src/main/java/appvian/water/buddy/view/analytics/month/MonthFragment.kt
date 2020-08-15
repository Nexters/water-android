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
import appvian.water.buddy.viewmodel.analytics.AnalyticsViewModel
import appvian.water.buddy.viewmodel.analytics.MonthViewModel

class MonthFragment(val analyVm: AnalyticsViewModel) : Fragment() {
    private lateinit var monthVm: MonthViewModel
    private lateinit var binding: FragmentMonthChartBinding
    private val rankAdapter = RankAdapter()
    private val characterAdapter = CharacterAdapter()

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
        analyVm.curMonth.observe(viewLifecycleOwner, Observer {
            monthVm.setMonth(it - 1)
            monthVm.getMonthlyIntake()
            characterAdapter.maxDay = monthVm.curMaxDay
        })
    }

    private fun initUi() {

        binding.monthRankList.adapter = rankAdapter
        binding.monthIntakeCharacter.adapter = characterAdapter
        binding.monthIntakeCharacter.addItemDecoration(CharacterItemDecoration())

        monthVm.monthlyRank.observe(viewLifecycleOwner, Observer {
            rankAdapter.setData(it)
            monthVm.loadMoreActive.value?.let { checkLoadAllData(it) }
        })

        setCharacterInfo()
        setLoadMoreButton()
        setIntakeDayText()
    }

    private fun setCharacterInfo() {
        monthVm.characterList.observe(viewLifecycleOwner, Observer {
            characterAdapter.setData(it)
        })
    }

    private fun setLoadMoreButton() {
        monthVm.loadMoreActive.observe(viewLifecycleOwner, Observer {
            checkLoadAllData(it)
        })
    }

    private fun setIntakeDayText() {
        monthVm.observeIntakeDays.observe(viewLifecycleOwner, Observer {
            android.util.Log.d("Month frag", "update ${it}")
            binding.monthSysTv.text = getString(R.string.month_sys_text, it.second, it.first)
        })
    }

    private fun checkLoadAllData(isLoadAll: Boolean) {
        if (isLoadAll) {
            binding.monthLoadMore.visibility = View.GONE
            rankAdapter.setAllData()
        } else {
            binding.monthLoadMore.visibility = View.VISIBLE
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(analyVm: AnalyticsViewModel) = MonthFragment(analyVm)
    }
}