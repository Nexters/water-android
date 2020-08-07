package appvian.water.buddy.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import appvian.water.buddy.model.data.SharedPreferenceStringLiveData

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    //자주 마시는 음료 1
    val fav_1_pref = application.getSharedPreferences("favorite_1", Context.MODE_PRIVATE)
    var fav_1_livedata: SharedPreferenceStringLiveData = SharedPreferenceStringLiveData(fav_1_pref, "favorite_1", fav_1_pref.getString("favorite_1", "")!!)
    //자주 마시는 음료 2
    val fav_2_pref = application.getSharedPreferences("favorite_2", Context.MODE_PRIVATE)
    var fav_2_livedata: SharedPreferenceStringLiveData = SharedPreferenceStringLiveData(fav_2_pref, "favorite_2", fav_2_pref.getString("favorite_2", "")!!)
}