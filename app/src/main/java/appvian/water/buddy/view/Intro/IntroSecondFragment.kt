package appvian.water.buddy.view.Intro

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
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
    private lateinit var callback: OnBackPressedCallback
    private var target_amount_change_flag = false
    lateinit var nextbtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.intro_second_fragment, container, false)
        textcount = view.text_count
        edittext = view.name_edittext
        nextbtn = view.nextbtn
        //introViewModel = ViewModelProvider(this).get(IntroViewModel::class.java)

        view.nextbtn.setOnClickListener {
            introViewModel.setNameLiveData(edittext.text.toString())
            (activity as IntroActivity).replaceFragment(IntroThirdFragment.newInstance())

        }
        return view
    }
    //뒤로 가기 버튼 클릭 시 이전 프레그먼트로 이
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                (activity as IntroActivity).replaceFragment(IntroFragment.newInstance())
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        introViewModel = ViewModelProvider(requireActivity()).get(IntroViewModel::class.java)

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
                target_amount_change_flag = if(count > 0){
                    saveBtnClickable()
                    true
                } else{
                    saveBtnUnClickable()
                    false
                }
            }
        })


    }

    private fun saveBtnClickable() {
        nextbtn.background = resources.getDrawable(R.drawable.setting_save_btn, null)
        nextbtn.setTextColor(resources.getColor(R.color.white, null))
        nextbtn.isEnabled = true
    }
    private fun saveBtnUnClickable() {
        nextbtn.background = resources.getDrawable(R.drawable.setting_save_btn_inactive, null)
        nextbtn.setTextColor(resources.getColor(R.color.grey_1, null))
        nextbtn.isEnabled = false
    }

}