package appvian.water.buddy.view.settings

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import appvian.water.buddy.R
import appvian.water.buddy.databinding.ActivityFavoriteDrinkSettingBinding
import appvian.water.buddy.util.DrinkMapper
import appvian.water.buddy.utilities.Code
import appvian.water.buddy.view.SetIntakeModal
import appvian.water.buddy.viewmodel.FavoriteViewModel
import java.util.*

class FavoriteDrinkSettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteDrinkSettingBinding
    private lateinit var viewModel: FavoriteViewModel
    //쓰래기통 활성화 인지
    private var trash_flag = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@FavoriteDrinkSettingActivity, R.layout.activity_favorite_drink_setting)
        viewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)
        binding.viewModel = viewModel

        initUI()
    }
    private fun initUI(){
        setTrashVisibility()
        setAddMode()
        viewModel.fav_1_livedata.observe(this, Observer {
            if(it.isBlank()){
                binding.layoutFav1.visibility = View.GONE
                setTrashVisibility()
            }else{
                binding.layoutFav1.visibility = View.VISIBLE
                val strTokenizer = StringTokenizer(it)
                val category = strTokenizer.nextToken()
                val categoryInt = Integer.parseInt(category)
                binding.imgCategory.setImageDrawable(resources.getDrawable(DrinkMapper.drinkResources[categoryInt], null))
                binding.txtCategory.text = resources.getStringArray(DrinkMapper.drinkName)[categoryInt]
                binding.txtAmount.text = strTokenizer.nextToken() + "ml"
                setTrashVisibility()
            }

        })
        viewModel.fav_2_livedata.observe(this, Observer {
            if(it.isBlank()){
                binding.layoutFav2.visibility = View.GONE
                setTrashVisibility()
            }else{
                Log.d("TAG", "fav 2 : " + it)
                binding.layoutFav2.visibility = View.VISIBLE
                var strTokenizer = StringTokenizer(it)
                val category = strTokenizer.nextToken()
                val categoryInt = Integer.parseInt(category)
                binding.imgCategory2.setImageDrawable(resources.getDrawable(DrinkMapper.drinkResources[categoryInt], null))
                binding.txtCategory2.text = resources.getStringArray(DrinkMapper.drinkName)[categoryInt]
                binding.txtAmount2.text = strTokenizer.nextToken() + "ml"
                setTrashVisibility()
            }
        })

        binding.btnAdd.setOnClickListener{
            if(trash_flag){
                //체크된 거 삭제
                if(binding.checkbox1.isChecked){
                    viewModel.deleteFav1LiveData()
                }
                if(binding.checkbox2.isChecked){
                    viewModel.deleteFav2LiveData()
                }
                setAddMode()
            }else{
                if(viewModel.fav_1_livedata.value.isNullOrBlank() || viewModel.fav_2_livedata.value.isNullOrBlank()){
                    //modal open
                    openBottomSheet(Code.FAVORITE_DRINK_SETTING_ACTIVITY)
                }else{
                    //popup 띄우기
                    var intent = Intent(this@FavoriteDrinkSettingActivity, PopupActivity::class.java)
                    intent.putExtra("Message", getString(R.string.favorite_drink_full))
                    startActivity(intent)
                }
            }


        }

        binding.layoutFav1.setOnClickListener{
            openBottomSheet(Code.FAVORITE_EDIT_1)
        }
        binding.layoutFav2.setOnClickListener{
            openBottomSheet(Code.FAVORITE_EDIT_2)

        }
        binding.imgTrash.setOnClickListener{
            //맨 하단 버튼이 삭제하기로 변경 , 체크 박스 체크 다 해제
            if(!trash_flag) {
                setTrashMode()
            }else{
                setAddMode()
            }
        }
        binding.layoutBack.setOnClickListener{
            if(trash_flag){
                setAddMode()
            }
        }
        //체크 박스 이벤트
        binding.checkbox1.setOnCheckedChangeListener { buttonView, isChecked ->
            if(!isChecked && !binding.checkbox2.isChecked){
                setAddBtnInActive()
            }else{
                setAddBtnActive()
            }
        }
        binding.checkbox2.setOnCheckedChangeListener { buttonView, isChecked ->
            if(!isChecked && !binding.checkbox1.isChecked){
                setAddBtnInActive()
            }else{
                setAddBtnActive()
            }
        }
    }
    private fun openBottomSheet(code : Int){
        val bottomSheet = SetIntakeModal(code, null)
        val fragmentManager = supportFragmentManager
        bottomSheet.show(fragmentManager, bottomSheet.tag)
    }
    private fun setTrashVisibility(){
        if(viewModel.fav_1_livedata.value.isNullOrBlank() && viewModel.fav_2_livedata.value.isNullOrBlank()){
            binding.imgTrash.visibility = View.GONE
        }else{
            binding.imgTrash.visibility = View.VISIBLE
        }
    }
    private fun setTrashMode(){
        setAddBtnInActive()
        binding.layoutFav2.isEnabled = false
        binding.layoutFav1.isEnabled = false
        binding.btnAdd.setText("삭제하기")
        binding.imgEdit.visibility = View.GONE
        binding.imgEdit2.visibility = View.GONE
        binding.checkbox1.visibility = View.VISIBLE
        binding.checkbox2.visibility = View.VISIBLE
        binding.checkbox1.isChecked = false
        binding.checkbox2.isChecked = false
        trash_flag = true
    }
    private fun setAddMode(){
        setAddBtnActive()
        binding.layoutFav1.isEnabled = true
        binding.layoutFav2.isEnabled = true
        binding.btnAdd.setText("등록하기")
        binding.imgEdit.visibility = View.VISIBLE
        binding.imgEdit2.visibility = View.VISIBLE
        binding.checkbox1.visibility = View.GONE
        binding.checkbox2.visibility = View.GONE
        trash_flag = false
    }
    private fun setAddBtnInActive(){
        binding.btnAdd.isEnabled = false
        binding.btnAdd.background = resources.getDrawable(R.drawable.setting_save_btn_inactive, null)
        binding.btnAdd.setTextColor(resources.getColor(R.color.grey_1, null))
    }
    private fun setAddBtnActive(){
        binding.btnAdd.isEnabled = true
        binding.btnAdd.background = resources.getDrawable(R.drawable.setting_save_btn, null)
        binding.btnAdd.setTextColor(resources.getColor(R.color.white, null))
    }
}