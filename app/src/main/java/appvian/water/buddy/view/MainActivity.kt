package appvian.water.buddy.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import appvian.water.buddy.R
import appvian.water.buddy.databinding.ActivityMainBinding
import appvian.water.buddy.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)
        binding.mainViewModel = MainViewModel(this)
    }
}
