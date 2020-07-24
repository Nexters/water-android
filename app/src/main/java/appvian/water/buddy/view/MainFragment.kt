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
import androidx.lifecycle.ViewModelProvider
import appvian.water.buddy.R
import appvian.water.buddy.viewmodel.HomeViewModel
import com.airbnb.lottie.LottieAnimationView
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
        val textV : TextView = view.findViewById(R.id.textview)
        val animV1 : LottieAnimationView = view.findViewById(R.id.animation_view1)
        val animV2 : LottieAnimationView = view.findViewById(R.id.animation_view2)
        animV2.setOnClickListener {
            val bottomSheet = SetIntakeModal()
            val fragmentManager = childFragmentManager
            bottomSheet.show(fragmentManager,bottomSheet.tag)
        }
        val anim_tranlate1 = TranslateAnimation(0F,0F,currentY1,currentY1)
        anim_tranlate1.duration = 2000
        anim_tranlate1.fillAfter = true
        animV1.startAnimation(anim_tranlate1)

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        homeViewModel.getDaily().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            drinkedAmount=0
            for(i in it){
                drinkedAmount+=i.amount
            }
            textV.text = currentPercent.toInt().toString()
            val percent: Float = (drinkedAmount*100/requiredAmount).toFloat()
            val goalY1 = startY1 - (startY1 - endY1) * percent / 100
            val goalY2 = startY2 - (startY2 - endY2) * percent / 100
            val newanim1 = TranslateAnimation(0F, 0F, currentY1, goalY1)
            val newanim2 = TranslateAnimation(0F, 0F, currentY2, goalY2)
            newanim2.setAnimationListener(object : Animation.AnimationListener{
                override fun onAnimationEnd(animation: Animation?) {
                    animV2.layout(animV2.left,animV2.top-(startY2-goalY2).toInt(),animV2.right,animV2.bottom-(startY2-goalY2).toInt())
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
            animV1.startAnimation(newanim1)
            animV2.startAnimation(newanim2)
            currentY1 = goalY1
            currentY2 = goalY2
            currentPercent = percent
            anim_value.duration = 2000
            anim_value.addUpdateListener {
                textV.text = it.animatedValue.toString()
            }
            anim_value.start()
        })
        return view
    }

    private fun getToday(): Long{
        val calendar = Calendar.getInstance()
        val today:Long = calendar.time.time
        return today
    }

    private fun getTomorrow(): Long{
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, 1)
        val tomorrow = calendar.time.time
        return tomorrow
    }
}