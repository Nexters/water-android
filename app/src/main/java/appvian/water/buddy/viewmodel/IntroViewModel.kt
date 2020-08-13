package appvian.water.buddy.viewmodel

import android.app.Application
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import appvian.water.buddy.R
import appvian.water.buddy.view.Intro.IntroFragment


class IntroViewModel(application: Application) : AndroidViewModel(application) {

class Factory(val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return IntroViewModel(application) as T
    }
}

    var nameliveText: MutableLiveData<String> = MutableLiveData()
    var targetamountliveText: MutableLiveData<String> = MutableLiveData()

    fun getText(): LiveData<String?>? {
        return nameliveText
    }

    fun TargetAmountSetText(text: Int?){
        targetamountliveText.value = text.toString()
    }
    fun nameSetText(text: String?) {
        nameliveText.value = text
    }


}