package appvian.water.buddy.view.Intro

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import appvian.water.buddy.R
import appvian.water.buddy.view.MainActivity
import appvian.water.buddy.viewmodel.IntroViewModel
import kotlinx.android.synthetic.main.intro_fourth_fragment.view.*


class IntroFourthFragment : Fragment() {
    private lateinit var introViewModel: IntroViewModel
    lateinit var nametext : TextView
    lateinit var tagetamounttext : TextView
    private lateinit var callback: OnBackPressedCallback

    companion object {
        fun newInstance() = IntroFourthFragment()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.intro_fourth_fragment, container, false)
        nametext = view.intro_fourth_name
        tagetamounttext = view.intro_fourth_Targetamount

        view.startbtn.setOnClickListener {
            startActivity(Intent(activity as IntroActivity, MainActivity::class.java))
            startActivity(Intent(activity as IntroActivity, TutorialActivity::class.java))
            (activity as IntroActivity).finish()
        }
        return view
    }


    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        introViewModel =
            ViewModelProvider(requireActivity()).get<IntroViewModel>(IntroViewModel::class.java)

        introViewModel.nameliveText.observe(viewLifecycleOwner,
        Observer<Any> { o -> nametext.text = o!!.toString() })

        introViewModel.targetamountliveText.observe(viewLifecycleOwner,
            Observer<Any> { o -> tagetamounttext.text = o!!.toString() + " L" })
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                (activity as IntroActivity).replaceFragment(IntroThirdFragment.newInstance())
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

}