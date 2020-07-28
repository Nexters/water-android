package appvian.water.buddy.binding

import androidx.databinding.BindingAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView


@BindingAdapter("bind:navigationItemSelected")
fun setOnNavigationItemSelected(view: BottomNavigationView, listener:BottomNavigationView.OnNavigationItemSelectedListener){
    view.setOnNavigationItemSelectedListener(listener)
}