package appvian.water.buddy.view

import android.content.res.Resources
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import androidx.lifecycle.ViewModelProvider
import appvian.water.buddy.R
import appvian.water.buddy.model.data.Intake
import appvian.water.buddy.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*
import kotlinx.coroutines.InternalCoroutinesApi
import android.util.DisplayMetrics


class MainFragment : Fragment() {

    val requiredAmount = 2000
    val startY1 = 650F
    val startY2 = 0F
    var currentY1 = startY1
    var currentY2 = startY2
    var currentPercent = 0F
    val endY1 = -600F
    val endY2 = -1300F

    val startPercentTextY : Float = 250F * (Resources.getSystem().displayMetrics.densityDpi).toFloat() / DisplayMetrics.DENSITY_DEFAULT
    var currentPercentTextY = startPercentTextY

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

        view.intake_list_button.setOnClickListener {
            val intent = Intent(activity,DailyIntakeListActivity::class.java)
            startActivity(intent)
        }

        setAnimation(view)

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        homeViewModel.dailyIntake.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            adjustAnimation(view,it)
            changeText(view,it)
        })
        return view
    }

    private fun setAnimation(view: View){
        val anim_tranlate1 = TranslateAnimation(0F,0F,currentY1,currentY1)
        anim_tranlate1.duration = 2000
        anim_tranlate1.fillAfter = true
        view.animation_view1.startAnimation(anim_tranlate1)

        val anim_translate_percentText = TranslateAnimation(0F,0F,currentPercentTextY,currentPercentTextY)
        anim_translate_percentText.duration = 0
        anim_translate_percentText.fillAfter = true
        view.percent_text.startAnimation(anim_translate_percentText)
    }

    private fun adjustAnimation(view: View,it: List<Intake>){
        var drinkedAmount=0
        for(i in it){
            drinkedAmount+=i.amount
        }
        view.percent.text = currentPercent.toInt().toString()
        val percent: Float = (drinkedAmount*100/requiredAmount).toFloat()
        val goalY1 = startY1 - (startY1 - endY1) * percent / 100
        val goalY2 = startY2 - (startY2 - endY2) * percent / 100
        var goalPercentY = startPercentTextY - (startY1 - endY1) * percent / 100
        if (goalPercentY<0F){
            goalPercentY=0F
        }
        val newanim1 = TranslateAnimation(0F, 0F, currentY1, goalY1)
        val newanim2 = TranslateAnimation(0F, 0F, currentY2, goalY2)
        val newanim_percent_text = TranslateAnimation(0F,0F,currentPercentTextY,goalPercentY)
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
        newanim_percent_text.duration = 2000
        newanim_percent_text.fillAfter = true
        view.animation_view1.startAnimation(newanim1)
        view.animation_view2.startAnimation(newanim2)
        view.percent_text.startAnimation(newanim_percent_text)
        currentY1 = goalY1
        currentY2 = goalY2
        currentPercentTextY = goalPercentY
        currentPercent = percent
        anim_value.duration = 2000
        anim_value.addUpdateListener {
            view.percent.text = it.animatedValue.toString()
        }
        anim_value.start()
    }

    private fun changeText(view: View,it: List<Intake>){
        var drinkedAmount=0
        for(i in it){
            drinkedAmount+=i.amount
        }
        val percent: Float = (drinkedAmount*100/requiredAmount).toFloat()
        if (percent==0F){
            view.home_text.text = getString(R.string.home_text_1)
        }
        if (percent>0F && percent<35F){
            view.home_text.text = getString(R.string.home_text_2)
        }
        if (percent>=35F && percent<65F){
            view.home_text.text = getString(R.string.home_text_3)
        }
        if (percent>=65F && percent<100F){
            view.home_text.text = getString(R.string.home_text_4)
        }
        if (percent>=100F){
            view.home_text.text = getString(R.string.home_text_5)
        }
        view.intake_list_button.text = String.format("%.1fL 중 %.1fL 수분 섭취",requiredAmount.toDouble()/1000,drinkedAmount.toDouble()/1000)
    }
}