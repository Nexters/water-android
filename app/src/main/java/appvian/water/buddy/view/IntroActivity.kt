package appvian.water.buddy.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import appvian.water.buddy.R
import appvian.water.buddy.databinding.ActivityMainBinding
import appvian.water.buddy.viewmodel.IntroViewModel
import appvian.water.buddy.viewmodel.MainViewModel

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_intro)
        binding.mainViewModel = IntroViewModel(this)

    }
}