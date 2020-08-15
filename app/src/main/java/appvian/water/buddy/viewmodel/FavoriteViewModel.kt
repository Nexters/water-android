package appvian.water.buddy.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import appvian.water.buddy.model.data.SharedPreferenceStringLiveData
import appvian.water.buddy.model.repository.SharedPrefsRepository
import java.lang.StringBuilder

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = SharedPrefsRepository(application)
    //자주 마시는 음료 1
    var fav_1_livedata = repository.fav_1_livedata
    //자주 마시는 음료 2
    var fav_2_livedata = repository.fav_2_livedata

    fun setFav1LiveData(category : Int, amount : Int){
        repository.setFav1LiveData(category, amount)
    }
    fun setFa2LiveData(category : Int, amount : Int){
        repository.setFa2LiveData(category, amount)
    }
    fun deleteFav1LiveData(){
        repository.deleteFav1LiveData()
    }
    fun deleteFav2LiveData(){
        repository.deleteFav2LiveData()
    }
}