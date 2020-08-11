package appvian.water.buddy.view.home

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import appvian.water.buddy.R
import appvian.water.buddy.databinding.ActivityDailyIntakeListBinding
import appvian.water.buddy.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.delete_toast.view.*

class DailyIntakeListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDailyIntakeListBinding
    private lateinit var homeViewModel: HomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        binding = DataBindingUtil.setContentView(this@DailyIntakeListActivity, R.layout.activity_daily_intake_list)
        binding.homeViewModel = homeViewModel

        initRecyclerView()
        setVisibleCheckbox()
        setButton()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        homeViewModel.isDeleteButtonClicked.value = false
    }

    private fun initRecyclerView(){
        homeViewModel.dailyIntake?.observe(this, Observer {
            var count = 0
            for (i in it){
                count+=1
            }
            if(count!=0) {
                binding.dailyIntakeRecyclerView.visibility = View.VISIBLE
                binding.modifyExplainText.visibility = View.VISIBLE
                binding.noWaterLayout.visibility = View.GONE
                binding.dailyIntakeRecyclerView.apply {
                    layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    homeViewModel.isDeleteButtonClicked.observe(this@DailyIntakeListActivity,
                        Observer {isDeleteClicked ->
                            adapter = DailyIntakeRecyclerViewAdapter(context, this@DailyIntakeListActivity, isDeleteClicked, it) {
                                intake ->
                                val bottomSheet = ModifyIntakeModal(intake)
                                val fragmentManager = supportFragmentManager
                                bottomSheet.show(fragmentManager,bottomSheet.tag)
                            }
                        })
                }
            } else{
                binding.dailyIntakeRecyclerView.visibility = View.GONE
                binding.modifyExplainText.visibility = View.GONE
                binding.noWaterLayout.visibility = View.VISIBLE
            }
        })
    }

    private fun setVisibleCheckbox(){
        binding.deleteButton.setOnClickListener(homeViewModel.deleteButton)
        homeViewModel.isDeleteButtonClicked.observe(this, Observer {
            if(it){
                binding.setDeleteButton.visibility = View.VISIBLE
            } else{
                binding.setDeleteButton.visibility = View.GONE
            }
        })
    }

    private fun setButton(){
        homeViewModel.deleteIntakeList.observe(this, Observer {
            val deleteList = it
            var count = 0
            for (i in deleteList){
                count+=1
            }
            when(count){
                0 -> {
                    binding.setDeleteButton.setBackgroundResource(R.drawable.delete_button_not_active)
                    binding.setDeleteButton.text = String.format("%d개의 내역 삭제하기",count)
                    binding.setDeleteButton.isEnabled = false
                }
                else ->{
                    binding.setDeleteButton.setBackgroundResource(R.drawable.delete_button_active)
                    binding.setDeleteButton.text = String.format("%d개의 내역 삭제하기",count)
                    binding.setDeleteButton.isEnabled = true
                }
            }
            binding.setDeleteButton.setOnClickListener {
                for (i in deleteList){
                    homeViewModel.delete(i)
                }
                homeViewModel.isDeleteButtonClicked.value = false
                homeViewModel.deleteIntakeList.value?.clear()
                Toast.makeText(this,String.format("%d개의 내역을 삭제하였습니다.",count),Toast.LENGTH_SHORT).apply {
                    this.view = View.inflate(this@DailyIntakeListActivity,R.layout.delete_toast,findViewById(R.id.delete_toast))
                    this.view.toast_text.text = String.format("%d개의 내역을 삭제하였습니다.",count)
                    this.view.toast_text.width = Resources.getSystem().displayMetrics.widthPixels - 38 * (Resources.getSystem().displayMetrics.densityDpi) / DisplayMetrics.DENSITY_DEFAULT
                    this.view.toast_text.height = 52 * (Resources.getSystem().displayMetrics.densityDpi) / DisplayMetrics.DENSITY_DEFAULT
                    this.show()
                }
            }
        })
    }
}