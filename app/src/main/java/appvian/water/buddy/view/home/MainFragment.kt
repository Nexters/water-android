package appvian.water.buddy.view.home

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
import appvian.water.buddy.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_main.*
import android.util.DisplayMetrics
import androidx.databinding.DataBindingUtil
import appvian.water.buddy.databinding.FragmentMainBinding
import appvian.water.buddy.view.DailyIntakeListActivity
import appvian.water.buddy.view.SetIntakeModal


class MainFragment : Fragment() {

    val requiredAmount = 2000
    val waterStartY = Resources.getSystem().displayMetrics.heightPixels.toFloat() - 100F * (Resources.getSystem().displayMetrics.densityDpi).toFloat() / DisplayMetrics.DENSITY_DEFAULT
    val startY2 = 0F
    var waterCurrentY = waterStartY
    var currentY2 = startY2
    var currentPercent = 0F
    val endY2 = -1300F

    val startPercentTextY : Float = Resources.getSystem().displayMetrics.heightPixels.toFloat() - 370F * (Resources.getSystem().displayMetrics.densityDpi).toFloat() / DisplayMetrics.DENSITY_DEFAULT
    var currentPercentTextY = startPercentTextY

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: FragmentMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        binding.homeViewModel = homeViewModel

        binding.animationView2.setOnClickListener {
            val bottomSheet = SetIntakeModal()
            val fragmentManager = childFragmentManager
            bottomSheet.show(fragmentManager,bottomSheet.tag)
        }

        binding.intakeListButton.setOnClickListener {
            val intent = Intent(activity,
                DailyIntakeListActivity::class.java)
            startActivity(intent)
        }

        setAnimation()
        homeViewModel.dailyAmount?.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if(it!=null) {
                adjustAnimation(it)
                changeText(it)
            } else{
                adjustAnimation(0)
                changeText(0)
            }
        })
        return binding.root
    }

    private fun setAnimation(){
        val anim_tranlate1 = TranslateAnimation(0F,0F,waterCurrentY,waterCurrentY)
        anim_tranlate1.duration = 2000
        anim_tranlate1.fillAfter = true
        binding.animationWaterFull.startAnimation(anim_tranlate1)

        val anim_translate_percentText = TranslateAnimation(0F,0F,currentPercentTextY,currentPercentTextY)
        anim_translate_percentText.duration = 0
        anim_translate_percentText.fillAfter = true
        binding.percentText.startAnimation(anim_translate_percentText)
    }

    private fun adjustAnimation(drinkedAmount: Int){
        binding.percent.text = currentPercent.toInt().toString()
        val percent: Float = (drinkedAmount*100/requiredAmount).toFloat()
        val goalY1 = waterStartY - waterStartY * percent / 100
        val goalY2 = startY2 - (startY2 - endY2) * percent / 100
        var goalPercentY = startPercentTextY - waterStartY * percent / 100
        if (goalPercentY<0F){
            goalPercentY=0F
        }
        val newanim1 = TranslateAnimation(0F, 0F, waterCurrentY, goalY1)
        val newanim2 = TranslateAnimation(0F, 0F, currentY2, goalY2)
        val newanim_percent_text = TranslateAnimation(0F,0F,currentPercentTextY,goalPercentY)
        newanim2.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationEnd(animation: Animation?) {
                binding.animationView2.layout(animation_view2.left,animation_view2.top-(startY2-goalY2).toInt(),animation_view2.right,animation_view2.bottom-(startY2-goalY2).toInt())
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
        binding.animationWaterFull.startAnimation(newanim1)
        binding.animationView2.startAnimation(newanim2)
        binding.percentText.startAnimation(newanim_percent_text)
        waterCurrentY = goalY1
        currentY2 = goalY2
        currentPercentTextY = goalPercentY
        currentPercent = percent
        anim_value.duration = 2000
        anim_value.addUpdateListener {
            binding.percent.text = it.animatedValue.toString()
        }
        anim_value.start()
    }

    private fun changeText(drinkedAmount: Int){
        val percent: Float = (drinkedAmount*100/requiredAmount).toFloat()
        when(percent){
            0F -> binding.homeText.text = getString(R.string.home_text_1)
            in 0F..35F -> binding.homeText.text = getString(R.string.home_text_2)
            in 35F..65F -> binding.homeText.text = getString(R.string.home_text_3)
            in 65F..99.9999F -> binding.homeText.text = getString(R.string.home_text_4)
            else -> binding.homeText.text = getString(R.string.home_text_5)
        }
        binding.intakeListButton.text = String.format("%.1fL 중 %.1fL 수분 섭취",requiredAmount.toDouble()/1000,drinkedAmount.toDouble()/1000)
    }

    companion object {

        @JvmStatic
        fun newInstance() = MainFragment()
    }
}