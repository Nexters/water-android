package appvian.water.buddy.binding

import android.view.View
import android.widget.Button
import androidx.databinding.BindingAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView


@BindingAdapter("bind:navigationItemSelected")
fun setOnNavigationItemSelected(view: BottomNavigationView, listener:BottomNavigationView.OnNavigationItemSelectedListener){
    view.setOnNavigationItemSelectedListener(listener)
}

@BindingAdapter("bind:deleteButtonClicked")
fun setOnDeleteButtonClicked(view: Button, listener: View.OnClickListener){
    view.setOnClickListener(listener)
}