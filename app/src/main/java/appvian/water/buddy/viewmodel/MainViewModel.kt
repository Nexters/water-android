package appvian.water.buddy.viewmodel

import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import appvian.water.buddy.R
import appvian.water.buddy.view.home.MainFragment
import appvian.water.buddy.view.settings.SettingFragment
import appvian.water.buddy.view.analytics.AnalyticsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainViewModel(val activity: FragmentActivity) {

    private var fragmentList: List<Fragment> =
        listOf(MainFragment.newInstance(), AnalyticsFragment.newInstance(), SettingFragment())

    val showWhiteImage : MutableLiveData<Boolean> = MutableLiveData(false)

    val menuListener = object : BottomNavigationView.OnNavigationItemSelectedListener {
        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.menu_home -> {
                    replaceFragment(fragmentList[0])
                    showWhiteImage.value = false
                    return true
                }

                R.id.menu_analytics -> {
                    replaceFragment(fragmentList[1])
                    showWhiteImage.value = true
                    return true
                }
                R.id.menu_settings -> {
                    replaceFragment(fragmentList[2])
                    showWhiteImage.value = true
                    return true
                }

                else -> {
                    return false
                }
            }
        }

    }

    init {
        activity.supportFragmentManager.beginTransaction()
            .add(R.id.fragment, fragmentList[0])
            .commit()
    }

    private fun replaceFragment(fragment: Fragment) {
        activity.supportFragmentManager.beginTransaction()
            .replace(R.id.fragment, fragment)
            .commit();
    }

    val floatingListner = object : View.OnClickListener{
        override fun onClick(v: View?) {
            val popup = PopupMenu(activity.applicationContext,v)
            popup.menuInflater.inflate(R.menu.popup_menu,popup.menu)
        }
    }
}