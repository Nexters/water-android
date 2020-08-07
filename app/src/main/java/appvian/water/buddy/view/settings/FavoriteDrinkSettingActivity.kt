package appvian.water.buddy.view.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import appvian.water.buddy.R
import appvian.water.buddy.databinding.ActivityProfileEditBinding
import appvian.water.buddy.viewmodel.SettingViewModel

class FavoriteDrinkSettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileEditBinding
    private lateinit var viewModel: SettingViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@FavoriteDrinkSettingActivity, R.layout.activity_favorite_drink_setting)
        viewModel = ViewModelProvider(this).get(SettingViewModel::class.java)
        binding.viewModel = viewModel

        initUI()
    }
    private fun initUI(){
        //data 가져와서 recycler view 연결
    }
}