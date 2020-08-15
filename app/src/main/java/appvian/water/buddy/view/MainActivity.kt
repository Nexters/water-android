package appvian.water.buddy.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import appvian.water.buddy.R
import appvian.water.buddy.databinding.ActivityMainBinding
import appvian.water.buddy.viewmodel.HomeViewModel
import appvian.water.buddy.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)
        val viewModel = MainViewModel(this)
        binding.mainViewModel = viewModel
        viewModel.showWhiteImage.observe(this, Observer {
            if(it){
                binding.bottomWhite.visibility = View.VISIBLE
            } else{
                binding.bottomWhite.visibility = View.GONE
            }
        })
    }
}
