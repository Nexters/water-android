package appvian.water.buddy.view.Intro

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import appvian.water.buddy.R
import appvian.water.buddy.viewmodel.IntroViewModel
import kotlinx.android.synthetic.main.intro_second_fragment.view.*
import kotlinx.android.synthetic.main.intro_third_fragment.view.*

class IntroThirdFragment: Fragment() {
    companion object {
        fun newInstance() = IntroThirdFragment()
    }
    private lateinit var introViewModel: IntroViewModel
    lateinit var edittextKg : EditText
    lateinit var edittextCm : EditText

    var kgtext: String = ""
    private var cmtext: String = ""
    private var targetamount : Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.intro_third_fragment, container, false)
        edittextKg = view.kg_edittext
        edittextCm = view.height_edittext
        view.nextbtn2.setOnClickListener {
            (activity as IntroActivity).replaceFragment(IntroFourthFragment.newInstance())
            targetamount = (kgtext.toInt() + cmtext.toInt()) / 100

            introViewModel.TargetAmountSetText(targetamount)
        }
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        introViewModel = ViewModelProvider(activity!!).get(IntroViewModel::class.java)

        edittextKg.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                kgtext = s.toString()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            @SuppressLint("SetTextI18n")
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        edittextCm.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                cmtext = s.toString()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            @SuppressLint("SetTextI18n")
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })


    }


}
