package appvian.water.buddy.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import appvian.water.buddy.R
import appvian.water.buddy.viewmodel.HomeViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var homeViewModel: HomeViewModel


    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        homeViewModel.getDaily().observe(this, androidx.lifecycle.Observer {

        })

        supportFragmentManager.beginTransaction().replace(R.id.fragment,MainFragment()).commit()
    }


}
