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
    private lateinit var callback: OnBackPressedCallback
    lateinit var edittextKg : EditText
    lateinit var edittextCm : EditText
    lateinit var nextbtn : Button
    private var kgtext: String = ""
    private var cmtext: String = ""
    private var targetamountFloat :  Float = 0.0F
    private var targetamountInt : Int = 0
    private var target_amount_change_flag_kg = false
    private var target_amount_change_flag_cm = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.intro_third_fragment, container, false)
        edittextKg = view.kg_edittext
        edittextCm = view.height_edittext
        nextbtn = view.nextbtn2

        view.nextbtn2.setOnClickListener {
            (activity as IntroActivity).replaceFragment(IntroFourthFragment.newInstance())
            targetamountFloat = ((kgtext.toFloat() + cmtext.toFloat()) / 100 ).toFloat()
            targetamountInt = ((kgtext.toInt() + cmtext.toInt()) * 10 )
            introViewModel.setHeightLiveData(edittextCm.text.toString().toInt())
            introViewModel.setWeightLiveData(edittextKg.text.toString().toInt())
            introViewModel.TargetAmountSetText(targetamountFloat)
            introViewModel.setTargetAmountFloatLiveData(targetamountFloat)
            introViewModel.setTargetAmountIntLiveData(targetamountInt)

        }
        return view
    }
    //뒤로 가기 버튼 클릭 시 이전 프레그먼트로 이
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                (activity as IntroActivity).replaceFragment(IntroSecondFragment.newInstance())
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

        edittextKg.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                kgtext = s.toString()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            @SuppressLint("SetTextI18n")
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val count: Int = edittextKg.text.toString().length

                target_amount_change_flag_kg = count > 0
                saveBtnClickable()
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
                val count: Int = edittextCm.text.toString().length

                target_amount_change_flag_cm = count > 0
                saveBtnClickable()
            }
        })


    }
    private fun saveBtnClickable() {
        if (target_amount_change_flag_kg && target_amount_change_flag_cm) {
            nextbtn.background = resources.getDrawable(R.drawable.setting_save_btn, null)
            nextbtn.setTextColor(resources.getColor(R.color.white, null))
            nextbtn.isEnabled = true
        } else {
            nextbtn.background = resources.getDrawable(R.drawable.setting_save_btn_inactive, null)
            nextbtn.setTextColor(resources.getColor(R.color.grey_1, null))
            nextbtn.isEnabled = false
        }

    }

}
