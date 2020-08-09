package appvian.water.buddy.view.Intro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import appvian.water.buddy.R
import appvian.water.buddy.databinding.ActivityIntroBinding
import appvian.water.buddy.viewmodel.IntroViewModel

class IntroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        supportFragmentManager.beginTransaction()
            .add(R.id.intro_fragment, IntroFragment())
            .commit()
    }
    fun replaceFragment(fragment: Fragment){
        val fragmentr = supportFragmentManager.beginTransaction()
        fragmentr.replace(R.id.intro_fragment, fragment).commit();
    }

}