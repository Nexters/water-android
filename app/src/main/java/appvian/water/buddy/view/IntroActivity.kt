package appvian.water.buddy.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import appvian.water.buddy.R
import appvian.water.buddy.databinding.ActivityIntroBinding
import appvian.water.buddy.databinding.ActivityMainBinding
import appvian.water.buddy.viewmodel.IntroViewModel
import appvian.water.buddy.viewmodel.MainViewModel

class IntroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
//
//        val binding: ActivityIntroBinding =
//            DataBindingUtil.setContentView(this@IntroActivity, R.layout.activity_intro)
//        binding.introViewModel = IntroViewModel(this)

        val fragmentr = supportFragmentManager.beginTransaction()
        fragmentr.add(R.id.intro_fragment, IntroFragment.newInstance()).commit()
    }
    fun replaceFragment(fragment: Fragment){
        val fragmentr = supportFragmentManager.beginTransaction()
        fragmentr.replace(R.id.intro_fragment, fragment).commit();
    }

}