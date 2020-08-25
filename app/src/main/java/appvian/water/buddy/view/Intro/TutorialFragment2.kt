package appvian.water.buddy.view.Intro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import appvian.water.buddy.R

class TutorialFragment2 : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tutorial2, container, false)
    }

    companion object {
        fun newInstance() = TutorialFragment2()
    }
}