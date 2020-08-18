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
        val listImage: ArrayList<Int> = ArrayList()
        listImage.add(R.mipmap.tutorial_1)
        listImage.add(R.mipmap.tutorial_2)

        viewPager = tutorial_viewPager
        val pageAdapter = TutorialViewPagerAdapter(supportFragmentManager)
        viewPager.adapter = pageAdapter

        for(i in listImage) {
            val imageFragment = onBoardingTutorial()
            val bundle by lazy { Bundle() }
            bundle.putInt("imgRes", i)
            imageFragment.arguments = bundle
            pageAdapter.addItem(imageFragment)
        }
        pageAdapter.notifyDataSetChanged()

        tutorial_close_button.setOnClickListener {
            finish()
        }
    }
}