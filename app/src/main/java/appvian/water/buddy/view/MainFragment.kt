package appvian.water.buddy.view

import android.animation.ValueAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import appvian.water.buddy.R
import appvian.water.buddy.model.data.Intake
import appvian.water.buddy.viewmodel.HomeViewModel
import com.airbnb.lottie.LottieAnimationView
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*
import kotlinx.coroutines.InternalCoroutinesApi
import java.util.*


class MainFragment : Fragment() {

    val requiredAmount = 2000
    var drinkedAmount = 0
    val startY1 = 650F
    val startY2 = 0F
    var currentY1 = startY1
    var currentY2 = startY2
    var currentPercent = 0F
    val endY1 = -600F
    val endY2 = -1300F

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @InternalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_main,null)

        view.animation_view2.setOnClickListener {
            val bottomSheet = SetIntakeModal()
            val fragmentManager = childFragmentManager
            bottomSheet.show(fragmentManager,bottomSheet.tag)
        }
        val anim_tranlate1 = TranslateAnimation(0F,0F,currentY1,currentY1)
        anim_tranlate1.duration = 2000
        anim_tranlate1.fillAfter = true
        view.animation_view1.startAnimation(anim_tranlate1)

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        homeViewModel.dailyIntake?.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            adjustAnimation(view,it)
        })
        return view
    }

    private fun adjustAnimation(view: View,it: List<Intake>){
        drinkedAmount=0
        for(i in it){
            drinkedAmount+=i.amount
        }
        view.textview.text = currentPercent.toInt().toString()
        val percent: Float = (drinkedAmount*100/requiredAmount).toFloat()
        val goalY1 = startY1 - (startY1 - endY1) * percent / 100
        val goalY2 = startY2 - (startY2 - endY2) * percent / 100
        val newanim1 = TranslateAnimation(0F, 0F, currentY1, goalY1)
        val newanim2 = TranslateAnimation(0F, 0F, currentY2, goalY2)
        newanim2.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationEnd(animation: Animation?) {
                view.animation_view2.layout(animation_view2.left,animation_view2.top-(startY2-goalY2).toInt(),animation_view2.right,animation_view2.bottom-(startY2-goalY2).toInt())
            }
            override fun onAnimationRepeat(animation: Animation?) {
            }
            override fun onAnimationStart(animation: Animation?) {
            }
        })
        val anim_value = ValueAnimator.ofInt(currentPercent.toInt(), percent.toInt())
        newanim1.duration = 2000
        newanim1.fillAfter = true
        newanim2.duration = 2500
        newanim2.isFillEnabled = true
        view.animation_view1.startAnimation(newanim1)
        view.animation_view2.startAnimation(newanim2)
        currentY1 = goalY1
        currentY2 = goalY2
        currentPercent = percent
        anim_value.duration = 2000
        anim_value.addUpdateListener {
            view.textview.text = it.animatedValue.toString()
        }
        anim_value.start()
    }
}