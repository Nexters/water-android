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
import kotlinx.coroutines.*

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

    private val intakeWaitList: MutableLiveData<ArrayList<Intake>> = MutableLiveData(ArrayList())

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
    private fun insert(intake: Intake) {
        homeRepository.insert(intake)
    }

    fun addIntake(intake: Intake){
        latestIntake = intake
        showInsertToast.value = true
        intakeWaitList.value!!.add(intake)
        intakeWaitList.observeForever {
            if (it.isNotEmpty()){
                when(it.size){
                    1 -> {
                        GlobalScope.launch(Dispatchers.Main) {
                            delay(2500)
                            withContext(Dispatchers.Main){
                                insert(it[0])
                                it.removeAt(0)
                            }
                        }
                    }

                    else -> {
                        GlobalScope.launch(Dispatchers.Main) {
                            delay(6500)
                            withContext(Dispatchers.Main){
                                insert(it[0])
                                it.removeAt(0)
                            }
                        }
                    }
                }
            }
        }
    }
}