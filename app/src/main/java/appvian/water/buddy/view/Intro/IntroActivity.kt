package appvian.water.buddy.view.Intro

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import appvian.water.buddy.R
import kotlinx.android.synthetic.main.intro_fragment.view.*

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
        fragmentr.replace(R.id.intro_fragment, fragment).commit()
    }


    override fun onBackPressed() {
        super.onBackPressed()
    }
}