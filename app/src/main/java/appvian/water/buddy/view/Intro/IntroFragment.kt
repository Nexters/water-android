package appvian.water.buddy.view.Intro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import appvian.water.buddy.R
import kotlinx.android.synthetic.main.intro_fragment.view.*

class IntroFragment : Fragment() {

    companion object {
        fun newInstance() = IntroFragment()
    }

    //private lateinit var introViewModel: IntroViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.intro_fragment, container, false)
        //introViewModel = ViewModelProvider(this).get(IntroViewModel::class.java)
        view.confirmbtn.setOnClickListener{
            (activity as IntroActivity).replaceFragment(IntroSecondFragment.newInstance())
        }
        return view
    }

}