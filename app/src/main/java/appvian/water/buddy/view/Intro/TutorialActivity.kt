package appvian.water.buddy.view.Intro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import appvian.water.buddy.R
import kotlinx.android.synthetic.main.activity_tutorial.*

class TutorialActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)

        viewPager = tutorial_viewPager
        val pageAdapter = TutorialViewPagerAdapter(supportFragmentManager)
        viewPager.adapter = pageAdapter

        pageAdapter.addItem(TutorialFragment1())
        pageAdapter.addItem(TutorialFragment2())
        pageAdapter.notifyDataSetChanged()

        tutorial_close_button.setOnClickListener {
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}