package appvian.water.buddy.view.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import appvian.water.buddy.R
import appvian.water.buddy.databinding.ActivityFavoriteDrinkSettingBinding
import appvian.water.buddy.databinding.ActivityProfileEditBinding
import appvian.water.buddy.model.data.Cup
import appvian.water.buddy.utilities.Code
import appvian.water.buddy.view.SetIntakeModal
import appvian.water.buddy.viewmodel.FavoriteViewModel
import appvian.water.buddy.viewmodel.SettingViewModel
import java.util.*

class FavoriteDrinkSettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteDrinkSettingBinding
    private lateinit var viewModel: FavoriteViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@FavoriteDrinkSettingActivity, R.layout.activity_favorite_drink_setting)
        viewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)
        binding.viewModel = viewModel

        initUI()
    }
    private fun initUI(){
        //data 가져와서 recycler view 연결
        viewModel.fav_1_livedata.observe(this, Observer {
            if(it.isBlank()){
                binding.layoutFav1.visibility = View.GONE

            }else{
                binding.layoutFav1.visibility = View.VISIBLE
                var strTokenizer = StringTokenizer(it)
                when(strTokenizer.nextToken()){
                    "0" -> {
                        binding.imgCategory.setImageDrawable(resources.getDrawable(R.drawable.icon_water, null))
                        binding.txtCategory.text = "물"
                    }
                    "1" -> {
                        binding.imgCategory.setImageDrawable(resources.getDrawable(R.drawable.icon_coffee, null))
                        binding.txtCategory.text = "커피"
                    }
                    "2" -> {
                        binding.imgCategory.setImageDrawable(resources.getDrawable(R.drawable.icon_tea, null))
                        binding.txtCategory.text = "차"
                    }
                    "3" -> {
                        binding.imgCategory.setImageDrawable(resources.getDrawable(R.drawable.icon_milk, null))
                        binding.txtCategory.text = "우유"
                    }
                    "4" -> {
                        binding.imgCategory.setImageDrawable(resources.getDrawable(R.drawable.icon_carbon, null))
                        binding.txtCategory.text = "탄산음료"
                    }
                    "5" -> {
                        binding.imgCategory.setImageDrawable(resources.getDrawable(R.drawable.icon_juice, null))
                        binding.txtCategory.text = "주스"
                    }
                    "6" -> {
                        binding.imgCategory.setImageDrawable(resources.getDrawable(R.drawable.icon_alcohol, null))
                        binding.txtCategory.text = "주류"
                    }
                    "7" -> {
                        binding.imgCategory.setImageDrawable(resources.getDrawable(R.drawable.icon_ion, null))
                        binding.txtCategory.text = "이온음료"
                    }
                    "8" -> {
                        binding.imgCategory.setImageDrawable(resources.getDrawable(R.drawable.icon_etc, null))
                        binding.txtCategory.text = "기타"
                    }

                }
                binding.txtAmount.text = strTokenizer.nextToken() + "ml"
            }

        })
        viewModel.fav_2_livedata.observe(this, Observer {
            if(it.isBlank()){
                binding.layoutFav2.visibility = View.GONE
            }else{
                Log.d("TAG", "fav 2 : " + it)
                binding.layoutFav2.visibility = View.VISIBLE
                var strTokenizer = StringTokenizer(it)
                when(strTokenizer.nextToken()){
                    "0" -> {
                        binding.imgCategory2.setImageDrawable(resources.getDrawable(R.drawable.icon_water, null))
                        binding.txtCategory2.text = "물"
                    }
                    "1" -> {
                        binding.imgCategory2.setImageDrawable(resources.getDrawable(R.drawable.icon_coffee, null))
                        binding.txtCategory2.text = "커피"
                    }
                    "2" -> {
                        binding.imgCategory2.setImageDrawable(resources.getDrawable(R.drawable.icon_tea, null))
                        binding.txtCategory2.text = "차"
                    }
                    "3" -> {
                        binding.imgCategory2.setImageDrawable(resources.getDrawable(R.drawable.icon_milk, null))
                        binding.txtCategory2.text = "우유"
                    }
                    "4" -> {
                        binding.imgCategory2.setImageDrawable(resources.getDrawable(R.drawable.icon_carbon, null))
                        binding.txtCategory2.text = "탄산음료"
                    }
                    "5" -> {
                        binding.imgCategory2.setImageDrawable(resources.getDrawable(R.drawable.icon_juice, null))
                        binding.txtCategory2.text = "주스"
                    }
                    "6" -> {
                        binding.imgCategory2.setImageDrawable(resources.getDrawable(R.drawable.icon_alcohol, null))
                        binding.txtCategory2.text = "주류"
                    }
                    "7" -> {
                        binding.imgCategory2.setImageDrawable(resources.getDrawable(R.drawable.icon_ion, null))
                        binding.txtCategory2.text = "이온음료"
                    }
                    "8" -> {
                        binding.imgCategory2.setImageDrawable(resources.getDrawable(R.drawable.icon_etc, null))
                        binding.txtCategory2.text = "기타"
                    }

                }
                binding.txtAmount2.text = strTokenizer.nextToken() + "ml"
            }
        })

        binding.imgPlus.setOnClickListener{
            if(viewModel.fav_1_livedata.value.isNullOrBlank() || viewModel.fav_2_livedata.value.isNullOrBlank()){
                //modal open
                openBottomSheet(Code.FAVORITE_DRINK_SETTING_ACTIVITY)
            }else{
                //popup 띄우기

            }

        }

        binding.layoutFav1.setOnClickListener{
            openBottomSheet(Code.FAVORITE_EDIT_1)
        }
        binding.layoutFav2.setOnClickListener{
            openBottomSheet(Code.FAVORITE_EDIT_2)

        }
    }
    private fun openBottomSheet(code : Int){
        val bottomSheet = SetIntakeModal(code)
        val fragmentManager = supportFragmentManager
        bottomSheet.show(fragmentManager, bottomSheet.tag)
    }
}