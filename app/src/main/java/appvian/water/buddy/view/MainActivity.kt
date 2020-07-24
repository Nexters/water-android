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
        homeViewModel.getDaily(getToday(),getTomorrow()).observe(this, androidx.lifecycle.Observer {

        })

        supportFragmentManager.beginTransaction().replace(R.id.fragment,MainFragment()).commit()
    }

    private fun getToday(): Long{
        val calendar = Calendar.getInstance()
        val today:Long = calendar.time.time
        return today
    }

    private fun getTomorrow(): Long{
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, 1)
        val tomorrow = calendar.time.time
        return tomorrow
    }

    private fun getWeekAgo(): Long{
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE ,-6)
        val aWeekAgo = calendar.time.time
        return aWeekAgo
    }

    private fun getMonthAgo(): Long{
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, -1)
        val aMonthAgo = calendar.time.time
        return aMonthAgo
    }
}
