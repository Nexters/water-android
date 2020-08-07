package appvian.water.buddy.viewmodel

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import appvian.water.buddy.R
import appvian.water.buddy.view.Intro.IntroFragment

class IntroViewModel(val activity: FragmentActivity): ViewModel() {

    private var fragmentList: List<Fragment> = listOf(IntroFragment())


    init {
        activity.supportFragmentManager.beginTransaction()
            .add(R.id.intro_fragment, fragmentList[0])
            .commit()
    }
    fun replaceFragment(fragment: Fragment){
        activity.supportFragmentManager.beginTransaction()
            .replace(R.id.intro_fragment, fragment).commit()
    }


}