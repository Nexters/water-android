package appvian.water.buddy.view.Intro

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import appvian.water.buddy.R
import appvian.water.buddy.view.MainActivity
import kotlinx.android.synthetic.main.intro_fourth_fragment.view.*


class IntroFourthFragment : Fragment() {

    companion object {
        fun newInstance() = IntroFourthFragment()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.intro_fourth_fragment, container, false)
        view.startbtn.setOnClickListener {
            startActivity(Intent(activity as IntroActivity, MainActivity::class.java))
        }
        return view
    }


}