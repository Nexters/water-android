package appvian.water.buddy.view.home

import android.animation.Animator
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
import androidx.lifecycle.Observer
import appvian.water.buddy.databinding.FragmentMainBinding
import appvian.water.buddy.utilities.Code
import appvian.water.buddy.view.SetIntakeModal
import kotlinx.coroutines.*


class MainFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: FragmentMainBinding
    var isFirst = true
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

        setFirstCharacter()
        setFirstWater()
        binding.intakeListButton.setOnClickListener {
            val intent = Intent(activity,
                DailyIntakeListActivity::class.java)
            startActivity(intent)
        }

        homeViewModel.percent?.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if(it!=null) {
                if (isFirst) {
                    GlobalScope.launch(Dispatchers.Main) {
                        delay(1500L)
                        withContext(Dispatchers.Main) {
                            adjustAnimation(it)
                        }
                    }
                    isFirst = false
                } else{
                    adjustAnimation(it)
                }
                changeText(it)
                setCharacter(it)
            } else{
                if(isFirst){
                    GlobalScope.launch(Dispatchers.Main) {
                        delay(1500L)
                        withContext(Dispatchers.Main) {
                            adjustAnimation(0F)
                        }
                    }
                    isFirst = false
                } else{
                    adjustAnimation(0F)
                }
                changeText(0F)
                setCharacter(0F)
            }
        })

        return binding.root
    }

    private fun setFirstWater(){
        val waterTranslate = TranslateAnimation(0F,0F,homeViewModel.waterCurrentY,homeViewModel.waterCurrentY)
        waterTranslate.duration = 0
        waterTranslate.fillAfter = true
        binding.animationWaterFull.startAnimation(waterTranslate)
        binding.animationWaterLine.startAnimation(waterTranslate)
        val percentTranslate = TranslateAnimation(0F,0F,homeViewModel.currentPercentTextY,homeViewModel.currentPercentTextY)
        percentTranslate.duration = 0
        percentTranslate.fillAfter = true
        binding.percentText.startAnimation(percentTranslate)
    }

    private fun adjustAnimation(percent: Float){
        binding.percent.text = homeViewModel.currentPercent.toInt().toString()
        val waterGoalY = homeViewModel.waterStartY - homeViewModel.waterStartY * percent / 100
        var characterGoalY = homeViewModel.characterEndY * percent / 100
        var goalPercentY = homeViewModel.startPercentTextY - homeViewModel.waterStartY * percent / 100
        if (goalPercentY<0F){
            goalPercentY=0F
        }
        val waterTranslate = TranslateAnimation(0F, 0F, homeViewModel.waterCurrentY, waterGoalY)
        val newanim_percent_text = TranslateAnimation(0F,0F,homeViewModel.currentPercentTextY,goalPercentY)
        if(percent>40F&&percent<=60F){
            characterGoalY=-homeViewModel.waterStartY+waterGoalY+50F*(Resources.getSystem().displayMetrics.densityDpi).toFloat() / DisplayMetrics.DENSITY_DEFAULT
        }
        if(percent>60F&&percent<=80F){
            characterGoalY=-homeViewModel.waterStartY+waterGoalY+120F*(Resources.getSystem().displayMetrics.densityDpi).toFloat() / DisplayMetrics.DENSITY_DEFAULT
        }
        if(percent>80F){
            characterGoalY=-Resources.getSystem().displayMetrics.heightPixels.toFloat()/4
        }
        val characterTranslate = TranslateAnimation(0F, 0F, homeViewModel.characterCurrentY, characterGoalY)
        characterTranslate.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationEnd(animation: Animation?) {
                binding.animationCharacter.layout(animation_character.left,
                    animation_character.top+characterGoalY.toInt(),
                    animation_character.right,
                    animation_character.bottom+characterGoalY.toInt())
            }
            override fun onAnimationRepeat(animation: Animation?) {

            }
            override fun onAnimationStart(animation: Animation?) {
            }
        })
        characterTranslate.duration = 2500
        characterTranslate.isFillEnabled = true
        binding.animationCharacter.startAnimation(characterTranslate)
        val anim_value = ValueAnimator.ofInt(homeViewModel.currentPercent.toInt(), percent.toInt())
        waterTranslate.duration = 2000
        waterTranslate.fillAfter = true
        newanim_percent_text.duration = 2000
        newanim_percent_text.fillAfter = true
        binding.animationWaterFull.startAnimation(waterTranslate)
        binding.animationWaterLine.startAnimation(waterTranslate)
        binding.percentText.startAnimation(newanim_percent_text)
        homeViewModel.waterCurrentY = waterGoalY
        homeViewModel.characterCurrentY = characterGoalY
        homeViewModel.currentPercentTextY = goalPercentY
        homeViewModel.currentPercent = percent
        anim_value.duration = 2000
        anim_value.addUpdateListener {
            binding.percent.text = it.animatedValue.toString()
        }
        anim_value.start()
    }

    private fun changeText(percent: Float){
        when(percent){
            0F -> binding.homeText.text = getString(R.string.home_text_1)
            in 0F..35F -> binding.homeText.text = getString(R.string.home_text_2)
            in 35F..65F -> binding.homeText.text = getString(R.string.home_text_3)
            in 65F..99.9999F -> binding.homeText.text = getString(R.string.home_text_4)
            else -> binding.homeText.text = getString(R.string.home_text_5)
        }
        homeViewModel.dailyAmount?.observe(viewLifecycleOwner, Observer {
            if(it!=null) {
                binding.intakeListButton.text = String.format(
                    "%.1fL 중 %.1fL 수분 섭취",
                    homeViewModel.requiredAmount.toDouble() / 1000,
                    it.toDouble() / 1000
                )
            } else{
                binding.intakeListButton.text = String.format(
                    "%.1fL 중 0L 수분 섭취",
                    homeViewModel.requiredAmount.toDouble() / 1000)
            }
        })
    }

    private fun setFirstCharacter(){
        binding.animationFirstCharacter.speed = 0.2F
        binding.animationFirstCharacter.addAnimatorListener(object : Animator.AnimatorListener{
            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                binding.animationFirstCharacter.visibility = View.GONE
                binding.animationCharacter.visibility = View.VISIBLE
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }
        })
    }

    private fun setCharacter(percent: Float){
        binding.animationCharacter.setOnClickListener {
            val bottomSheet = SetIntakeModal(Code.MAIN_FRAGMENT)
            val fragmentManager = childFragmentManager
            bottomSheet.show(fragmentManager,bottomSheet.tag)
        }
        when(percent){
            in 0F..20F -> {
                binding.animationCharacter.setPadding(0,0,0,0)
                binding.animationCharacter.setAnimation("0-20/2/0-20-2.json")
                binding.animationCharacter.imageAssetsFolder = "0-20/2/images"
                binding.animationCharacter.playAnimation()
            }
            in 20F..40F -> {
                binding.animationCharacter.setPadding(0,0,0,0)
                binding.animationCharacter.setAnimation("20-40/20-40.json")
                binding.animationCharacter.imageAssetsFolder = "20-40/images"
                binding.animationCharacter.playAnimation()
            }
            in 40F..60F -> {
                binding.animationCharacter.setPadding(0,0,0,0)
                binding.animationCharacter.setAnimation("40-60/40-60.json")
                binding.animationCharacter.imageAssetsFolder = "40-60/images"
                binding.animationCharacter.playAnimation()
            }
            in 60F..80F -> {
                binding.animationCharacter.setPadding(60,60,60,60)
                binding.animationCharacter.setAnimation("60-80/60-80.json")
                binding.animationCharacter.imageAssetsFolder = "60-80/images"
                binding.animationCharacter.playAnimation()
            }
            else -> {
                binding.animationCharacter.setPadding(0,0,0,0)
                binding.animationCharacter.setAnimation("80-100/80-100.json")
                binding.animationCharacter.imageAssetsFolder = "80-100/images"
                binding.animationCharacter.playAnimation()
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = MainFragment()
    }
}