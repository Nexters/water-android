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
import kotlinx.android.synthetic.main.intro_fragment.view.*
import kotlinx.android.synthetic.main.intro_second_fragment.view.*

class IntroSecondFragment: Fragment() {
    companion object {
        fun newInstance() = IntroSecondFragment()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.intro_second_fragment, container, false)
        val textcount = view.text_count
        val edittext = view.name_edittext
        edittext.addTextChangedListener(object:TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            @SuppressLint("SetTextI18n")
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val count: Int = edittext.text.toString().length
                textcount.text= count.toString() + getString(R.string.intro_second_textcount)

            }
        })
        view.nextbtn.setOnClickListener {
            (activity as IntroActivity).replaceFragment(IntroThirdFragment.newInstance())

        }
        return view
    }

}