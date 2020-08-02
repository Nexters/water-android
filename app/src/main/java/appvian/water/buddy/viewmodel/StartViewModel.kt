package appvian.water.buddy.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import appvian.water.buddy.model.repository.HomeRepository

class StartViewModel (application: Application) : AndroidViewModel(application) {
    private val repository = HomeRepository(application)
}