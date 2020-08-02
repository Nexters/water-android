package appvian.water.buddy.viewmodel

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import appvian.water.buddy.R
import appvian.water.buddy.view.IntroStartFrament

class IntroViewModel(val activity: FragmentActivity) {
    private var fragmentList: List<Fragment> = listOf(IntroStartFrament())
    init {
        activity.supportFragmentManager.beginTransaction()
            .add(R.id.intro_fragment, fragmentList[0])
            .commit()
    }

}