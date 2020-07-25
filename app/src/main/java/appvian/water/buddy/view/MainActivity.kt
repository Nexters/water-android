package appvian.water.buddy.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import appvian.water.buddy.R
import appvian.water.buddy.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.InternalCoroutinesApi

class MainActivity : AppCompatActivity() {

    private lateinit var homeViewModel: HomeViewModel

    private lateinit var fragmentList: List<Fragment>

    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        homeViewModel.getDaily().observe(this, androidx.lifecycle.Observer {

        })

        setRouting()
    }

    private fun setRouting() {
        fragmentList = listOf(MainFragment()) //라우팅할 fragment 리스트

        main_navigation.setOnNavigationItemSelectedListener { menu ->
            when (menu.itemId) {
                R.id.menu_home -> {
                    replaceFragment(fragmentList[0])
                }
                R.id.menu_analytics -> {
                }
                R.id.menu_settings -> {
                }
            }

            return@setOnNavigationItemSelectedListener true

        }

        supportFragmentManager.beginTransaction().add(R.id.fragment, fragmentList[0]).commit()
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment, fragment)
            .commit();
    }

}
