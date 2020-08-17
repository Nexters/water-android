package appvian.water.buddy.viewmodel

import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import appvian.water.buddy.R
import appvian.water.buddy.model.data.Intake
import appvian.water.buddy.model.repository.HomeRepository
import appvian.water.buddy.model.repository.SharedPrefsRepository
import appvian.water.buddy.view.home.MainFragment
import appvian.water.buddy.view.settings.SettingFragment
import appvian.water.buddy.view.analytics.AnalyticsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainViewModel(val activity: FragmentActivity) {

    private val sharedPreRepo = SharedPrefsRepository(activity)
    private val homeRepository = HomeRepository(activity)
    private var fragmentList: List<Fragment> =
        listOf(MainFragment.newInstance(), AnalyticsFragment.newInstance(), SettingFragment())

    val showWhiteImage : MutableLiveData<Boolean> = MutableLiveData(false)
    var fav_1_liveData = sharedPreRepo.fav_1_livedata
    var fav_2_liveData = sharedPreRepo.fav_2_livedata
    var showInsertToast: MutableLiveData<Boolean> = MutableLiveData(false)
    var latestIntake: Intake = Intake(0,0,0)

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
    fun insert(intake: Intake) {
        latestIntake = intake
        showInsertToast.value = true
        homeRepository.insert(intake)
        Log.e("value",showInsertToast.value.toString())
    }

}