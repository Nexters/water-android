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
    var kgliveText: MutableLiveData<String> = MutableLiveData()
    var heightliveText: MutableLiveData<String> = MutableLiveData()

    fun getText(): LiveData<String?>? {
        return nameliveText
    }

    fun nameSetText(text: String?) {
        nameliveText.value = text
    }
    fun kgSetText(text: String?) {
        kgliveText.value = text
    }
    fun heightSetText(text: String?) {
        heightliveText.value = text
    }

}