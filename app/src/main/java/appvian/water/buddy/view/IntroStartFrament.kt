package appvian.water.buddy.view

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import appvian.water.buddy.R
import appvian.water.buddy.databinding.ActivityMainBinding
import appvian.water.buddy.viewmodel.IntroViewModel
import appvian.water.buddy.viewmodel.MainViewModel
import appvian.water.buddy.viewmodel.StartViewModel

class IntroStartFrament : Fragment() {

    companion object {
        fun newInstance() = IntroStartFrament()
    }

    private lateinit var viewModel: StartViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.intro_start_fragment, container, false)


        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(StartViewModel::class.java)
        // TODO: Use the ViewModel
    }

}