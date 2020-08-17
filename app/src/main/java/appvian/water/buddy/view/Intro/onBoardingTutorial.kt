package appvian.water.buddy.view.Intro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import appvian.water.buddy.R
import kotlinx.android.synthetic.main.fragment_on_boarding.view.*
import kotlinx.android.synthetic.main.fragment_tutorial_on_boarding.view.*

class onBoardingTutorial : Fragment() {


    @NonNull
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tutorial_on_boarding, container, false)
        val imageView = view.tutorial_imageView

        if (arguments != null) {
            val args = arguments
            // MainActivity에서 받아온 Resource를 ImageView에 셋팅
            imageView.setImageResource(args!!.getInt("imgRes"))
        }

        return view
    }

}