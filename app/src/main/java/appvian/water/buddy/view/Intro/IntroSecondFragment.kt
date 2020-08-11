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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import appvian.water.buddy.R
import appvian.water.buddy.viewmodel.IntroViewModel
import kotlinx.android.synthetic.main.intro_second_fragment.view.*

class IntroSecondFragment: Fragment() {
    companion object {
        fun newInstance() = IntroSecondFragment()
    }
    private lateinit var introViewModel: IntroViewModel
    lateinit var textcount:TextView
    lateinit var edittext:EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.intro_second_fragment, container, false)
        textcount = view.text_count
        edittext = view.name_edittext

        //introViewModel = ViewModelProvider(this).get(IntroViewModel::class.java)

        view.nextbtn.setOnClickListener {
            (activity as IntroActivity).replaceFragment(IntroThirdFragment.newInstance())

        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        introViewModel = ViewModelProvider(activity!!).get(IntroViewModel::class.java)

        edittext.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                introViewModel.nameSetText(s.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            @SuppressLint("SetTextI18n")
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val count: Int = edittext.text.toString().length
                textcount.text = count.toString() + getString(R.string.intro_second_textcount)

            }
        })


    }

}