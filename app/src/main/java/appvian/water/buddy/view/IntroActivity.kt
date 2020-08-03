package appvian.water.buddy.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.databinding.DataBindingUtil
import appvian.water.buddy.R
import appvian.water.buddy.databinding.ActivityIntroBinding
import appvian.water.buddy.databinding.ActivityMainBinding
import appvian.water.buddy.viewmodel.IntroViewModel
import appvian.water.buddy.viewmodel.MainViewModel

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityIntroBinding =
            DataBindingUtil.setContentView(this@IntroActivity, R.layout.activity_intro)
        binding.introViewModel = IntroViewModel(this)
    }

}