package appvian.water.buddy.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import appvian.water.buddy.R
import kotlinx.android.synthetic.main.intro_second_fragment.view.*

class IntroThirdFragment: Fragment() {
    companion object {
        fun newInstance() = IntroThirdFragment()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.intro_third_fragment, container, false)


        return view
    }

}
