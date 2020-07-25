package appvian.water.buddy.viewmodel

import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import appvian.water.buddy.R
import appvian.water.buddy.view.MainFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainViewModel(val activity: FragmentActivity) {

    private var fragmentList: List<Fragment> = listOf(MainFragment())

    val menuListener = object : BottomNavigationView.OnNavigationItemSelectedListener {
        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.menu_home -> {
                    replaceFragment(fragmentList[0])
                    return true
                }

                R.id.menu_analytics -> {
                    return true
                }
                R.id.menu_settings -> {
                    return true
                }

                else -> {
                    return false
                }
            }
        }

    }

    private fun replaceFragment(fragment: Fragment) {
        activity.supportFragmentManager.beginTransaction()
            .replace(R.id.fragment, fragment)
            .commit();
    }
}