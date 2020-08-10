package appvian.water.buddy.view.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import appvian.water.buddy.R
import appvian.water.buddy.databinding.ActivityDailyIntakeListBinding
import appvian.water.buddy.viewmodel.HomeViewModel

class DailyIntakeListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDailyIntakeListBinding
    private lateinit var homeViewModel: HomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        binding = DataBindingUtil.setContentView(this@DailyIntakeListActivity, R.layout.activity_daily_intake_list)
        binding.homeViewModel = homeViewModel
        homeViewModel.dailyIntake?.observe(this, Observer {
            if(it!=null) {
                binding.dailyIntakeRecyclerView.apply {
                    layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    homeViewModel.isDeleteButtonClicked.observe(this@DailyIntakeListActivity,
                        Observer {isDeleteClicked ->
                            adapter = DailyIntakeRecyclerViewAdapter(context, isDeleteClicked, homeViewModel, it) {

                            }
                        })
                }
            }
        })
        binding.deleteButton.setOnClickListener(homeViewModel.deleteButton)
        homeViewModel.isDeleteButtonClicked.observe(this, Observer {
            if(it){
                binding.setDeleteButton.visibility = View.VISIBLE
            } else{
                binding.setDeleteButton.visibility = View.GONE
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        homeViewModel.isDeleteButtonClicked.value = false
    }
}