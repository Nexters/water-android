package appvian.water.buddy.view

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import appvian.water.buddy.R
import appvian.water.buddy.databinding.ActivityIntroBinding
import appvian.water.buddy.viewmodel.HomeViewModel
import appvian.water.buddy.viewmodel.IntroFragmentViewModel
import appvian.water.buddy.viewmodel.IntroViewModel
import kotlinx.android.synthetic.main.intro_fragment.view.*

class IntroFragment : Fragment() {

    companion object {
        fun newInstance() = IntroFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.intro_fragment, container, false)
        view.nextbtn.setOnClickListener{
            (activity as IntroActivity).replaceFragment(IntroSecondFragment.newInstance())
        }
        return view
    }

}