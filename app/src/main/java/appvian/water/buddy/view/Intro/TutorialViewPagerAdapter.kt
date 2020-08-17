package appvian.water.buddy.view.Intro

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


class TutorialViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {

    private val fragments: ArrayList<Fragment> = ArrayList()

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
    }

    override fun getCount(): Int {
        return fragments.size
    }

    fun addItem(fragment: Fragment) {
        fragments.add(fragment)
    }

}