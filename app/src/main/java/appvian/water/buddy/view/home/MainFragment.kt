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
import appvian.water.buddy.databinding.FragmentMainBinding
import appvian.water.buddy.view.DailyIntakeListActivity
import appvian.water.buddy.view.SetIntakeModal


class MainFragment : Fragment() {

    val requiredAmount = 2000
    val waterStartY = Resources.getSystem().displayMetrics.heightPixels.toFloat() - 100F * (Resources.getSystem().displayMetrics.densityDpi).toFloat() / DisplayMetrics.DENSITY_DEFAULT
    var waterCurrentY = waterStartY
    var characterCurrentY = 0F
    var currentPercent = 0F
    val characterEndY = -(Resources.getSystem().displayMetrics.heightPixels.toFloat() - 300F * (Resources.getSystem().displayMetrics.densityDpi).toFloat() / DisplayMetrics.DENSITY_DEFAULT)

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

        setFirstCharacter()

        binding.intakeListButton.setOnClickListener {
            val intent = Intent(activity,
                DailyIntakeListActivity::class.java)
            startActivity(intent)
        }

        homeViewModel.dailyAmount?.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if(it!=null) {
                adjustAnimation(it)
                changeText(it)
                setCharacter(it)
            } else{
                adjustAnimation(0)
                changeText(0)
                setCharacter(0)
            }
        })
        return binding.root
    }


    private fun adjustAnimation(drinkedAmount: Int){
        binding.percent.text = currentPercent.toInt().toString()
        val percent: Float = (drinkedAmount*100/requiredAmount).toFloat()
        val goalY1 = waterStartY - waterStartY * percent / 100
        val goalY2 = characterEndY * percent / 100
        var goalPercentY = startPercentTextY - waterStartY * percent / 100
        if (goalPercentY<0F){
            goalPercentY=0F
        }
        val newanim1 = TranslateAnimation(0F, 0F, waterCurrentY, goalY1)
        val newanim2 = TranslateAnimation(0F, 0F, characterCurrentY, goalY2)
        val newanim_percent_text = TranslateAnimation(0F,0F,currentPercentTextY,goalPercentY)
        newanim2.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationEnd(animation: Animation?) {
                binding.animationCharacter.layout(animation_character.left,
                    animation_character.top+goalY2.toInt(),
                    animation_character.right,
                    animation_character.bottom+goalY2.toInt())
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
        binding.animationCharacter.startAnimation(newanim2)
        binding.percentText.startAnimation(newanim_percent_text)
        waterCurrentY = goalY1
        characterCurrentY = goalY2
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
    private fun setCharacter(drinkedAmount: Int){
        val percent: Float = (drinkedAmount*100/requiredAmount).toFloat()
        binding.animationCharacter.setOnClickListener {
            val bottomSheet = SetIntakeModal()
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