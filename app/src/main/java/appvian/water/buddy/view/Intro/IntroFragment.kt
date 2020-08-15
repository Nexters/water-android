package appvian.water.buddy.view.Intro

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import appvian.water.buddy.R
import appvian.water.buddy.R.layout.intro_fragment
import appvian.water.buddy.viewmodel.IntroViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.intro_fragment.view.*

class IntroFragment : Fragment() {

    companion object {
        fun newInstance() = IntroFragment()
    }
    private lateinit var callback: OnBackPressedCallback
    var mBackWait:Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(intro_fragment, container, false)

        view.confirmbtn.setOnClickListener{
            (activity as IntroActivity).replaceFragment(IntroSecondFragment.newInstance())
        }
        return view

    }
    //뒤로 가기 버튼 클릭 시 앱 종
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(System.currentTimeMillis() - mBackWait >=2000 ) {
                    mBackWait = System.currentTimeMillis()
                    Snackbar.make(activity!!.findViewById(android.R.id.content),"뒤로가기 버튼을 한번 더 누르면 종료됩니다.",Snackbar.LENGTH_LONG).show()
                } else {
                    activity?.finish()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

}