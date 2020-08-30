package appvian.water.buddy.view.Intro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import appvian.water.buddy.R
import kotlinx.android.synthetic.main.fragment_on_boarding.view.*

class onBoardingFragment : Fragment() {


    @NonNull
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_on_boarding, container, false)
        val imageView = view.imageView
        val textView= view.onboarding_text
        val args = arguments
        if (arguments != null) {
            imageView.setImageResource(args!!.getInt("imgRes"))
        }
        if (arguments != null) {
            textView.text = args!!.getString("textRes")
        }

        return view
    }

}