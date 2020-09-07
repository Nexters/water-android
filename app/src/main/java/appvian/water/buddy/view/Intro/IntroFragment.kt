package appvian.water.buddy.view.Intro

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import appvian.water.buddy.R
import appvian.water.buddy.R.drawable
import appvian.water.buddy.R.layout.intro_fragment
import appvian.water.buddy.view.CircleIndicator
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.intro_fragment.view.*

class IntroFragment : Fragment() {

    companion object {
        fun newInstance() = IntroFragment()
    }
    private lateinit var callback: OnBackPressedCallback
    var mBackWait:Long = 0
    lateinit var viewpager : ViewPager
    lateinit var circleindicator: CircleIndicator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(intro_fragment, container, false)
        val listImage: ArrayList<Int> = ArrayList()
        val listText:ArrayList<String> = ArrayList()

        listImage.add(drawable.ic_onboarding_1)
        listImage.add(drawable.ic_onboarding_2)
        listImage.add(drawable.ic_onboarding_3)
        listText.add(getString(R.string.intro_onboarding1))
        listText.add(getString(R.string.intro_onboarding2))
        listText.add(getString(R.string.intro_onboarding3))

        viewpager = view.viewPager
        circleindicator = view.circleindicator
        val pageAdapter = IntroViewpagerAdapter(childFragmentManager)
        viewpager.adapter = pageAdapter
        var index = 0
        for(i in listImage) {
            val imageFragment = onBoardingFragment()
            val bundle by lazy { Bundle() }
            bundle.putInt("imgRes", i)
            bundle.putString("textRes", listText[index++])
            imageFragment.arguments = bundle
            pageAdapter.addItem(imageFragment)
        }

        pageAdapter.notifyDataSetChanged()

        viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(p0: Int) {

            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

            }

            override fun onPageSelected(p0: Int) {
                circleindicator.selectDot(p0)
            }

        })

        //init indicator
        circleindicator.createDotPanel(3, drawable.indicator_dot_off, drawable.indicator_dot_on, 0)

        view.confirmbtn.setOnClickListener{

            (activity as IntroActivity).replaceFragment(IntroSecondFragment.newInstance())
        }
        return view

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
