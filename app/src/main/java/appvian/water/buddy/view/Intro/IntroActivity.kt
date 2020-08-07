package appvian.water.buddy.view.Intro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import appvian.water.buddy.R

class IntroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

//        val binding: ActivityIntroBinding =
//            DataBindingUtil.setContentView(this@IntroActivity, R.layout.activity_intro)
//        binding.introViewModel = IntroViewModel(this)

        val fragmentr = supportFragmentManager.beginTransaction()
        fragmentr.add(R.id.intro_fragment,
            IntroFragment.newInstance()
        ).commit()
    }
    fun replaceFragment(fragment: Fragment){
        val fragmentr = supportFragmentManager.beginTransaction()
        fragmentr.replace(R.id.intro_fragment, fragment).commit();
    }

}