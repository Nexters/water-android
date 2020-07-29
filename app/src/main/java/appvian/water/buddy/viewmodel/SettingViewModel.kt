package appvian.water.buddy.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.preference.PreferenceManager
import appvian.water.buddy.model.data.SharedPreferenceBooleanLiveData
import appvian.water.buddy.model.data.SharedPreferenceLiveData
import appvian.water.buddy.model.data.SharedPreferenceStringLiveData
import appvian.water.buddy.model.data.stringLiveData


class SettingViewModel(application: Application) : AndroidViewModel(application) {

    val target_amount_pref = application.getSharedPreferences("target_amount", Context.MODE_PRIVATE)

    private var sharedPreferenceLiveData: SharedPreferenceStringLiveData? = null

    fun getSharedPrefs(): SharedPreferenceStringLiveData? {
        return sharedPreferenceLiveData
    }

    fun setSharedPreferences(key: String, value: String) {
        val editor = target_amount_pref.edit()
        editor.putString(key, value)
        editor.apply()
        sharedPreferenceLiveData = SharedPreferenceStringLiveData(target_amount_pref, key, value)
    }
}