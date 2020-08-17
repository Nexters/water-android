package appvian.water.buddy.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import appvian.water.buddy.R
import appvian.water.buddy.databinding.ActivityMainBinding
import appvian.water.buddy.model.data.Intake
import appvian.water.buddy.util.DrinkMapper
import appvian.water.buddy.utilities.CategoryMapper
import appvian.water.buddy.utilities.Code
import appvian.water.buddy.viewmodel.HomeViewModel
import appvian.water.buddy.viewmodel.MainViewModel
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var anim_fab_open : Animation
    private lateinit var anim_fab_closed : Animation
    private var isFabOpen = false
    private var isFav1 = false
    private var isFav2 = false
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =
            DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)
        val viewModel = MainViewModel(this)
        binding.mainViewModel = viewModel
        viewModel.showWhiteImage.observe(this, Observer {
            if(it){
                binding.bottomWhite.visibility = View.VISIBLE
            } else{
                binding.bottomWhite.visibility = View.GONE
            }
        })

        anim_fab_closed = AnimationUtils.loadAnimation(this, R.anim.fab_close)
        anim_fab_open = AnimationUtils.loadAnimation(this, R.anim.fab_open)

        binding.floatingButton.setOnClickListener{
            if(!isFav1 && !isFav2){
                //바로 추가 모달 띄우기
                openAddModal()
            }else {
                fabAnim()
            }
        }
        binding.layoutBack.setOnClickListener{
            fabAnim()
        }
        binding.fabAdd.setOnClickListener{
            //추가 모달 띄우기
            openAddModal()
            fabAnim()
        }
        binding.fabFav1.setOnClickListener{
            //바로 등록
            var st = StringTokenizer(binding.txtFav1.text.toString())
            var category_num = CategoryMapper.getCategoryNum(st.nextToken())
            var tmp = st.nextToken()
            viewModel.insert(Intake(System.currentTimeMillis(), category_num, tmp.substring(0, tmp.length-2).toInt()))
            fabAnim()
        }
        binding.fabFav2.setOnClickListener{
            //바로 등록
            var st = StringTokenizer(binding.txtFav2.text.toString())
            var category_num = CategoryMapper.getCategoryNum(st.nextToken())
            var tmp = st.nextToken()
            viewModel.insert(Intake(System.currentTimeMillis(), category_num, tmp.substring(0, tmp.length-2).toInt()))
            fabAnim()
        }
        viewModel.fav_1_liveData.observe(this, Observer {
            if(it.isNullOrBlank()){
                isFav1 = false
                binding.fabFav1.setImageDrawable(null)
                binding.txtFav1.text = ""
            }else{
                isFav1 = true
                var st = StringTokenizer(it)
                var category_num = st.nextToken().toInt()
                var amount = st.nextToken().toInt()
                var category_name = CategoryMapper.getCategoryName(category_num)

                binding.fabFav1.setImageDrawable(resources.getDrawable(DrinkMapper.drinkResources.get(category_num),null))
                binding.txtFav1.text = getString(R.string.fab_favorite, category_name, amount)
            }
        })
        viewModel.fav_2_liveData.observe(this, Observer {
            if(it.isNullOrBlank()){
                isFav2 = false
                binding.fabFav2.setImageDrawable(null)
                binding.txtFav2.text = ""
            }else{
                isFav2 = true
                var st = StringTokenizer(it)
                var category_num = st.nextToken().toInt()
                var amount = st.nextToken().toInt()
                var category_name = CategoryMapper.getCategoryName(category_num)
                binding.fabFav2.setImageDrawable(resources.getDrawable(DrinkMapper.drinkResources.get(category_num),null))
                binding.txtFav2.text = getString(R.string.fab_favorite, category_name, amount)
            }
        })

    }
    private fun fabAnim(){
        if(isFabOpen){
            binding.layoutBack.visibility = View.GONE
            binding.fabFav1.visibility = View.GONE
            binding.txtFav1.visibility = View.GONE
            isFabOpen = false
        }else{
            binding.layoutBack.visibility = View.VISIBLE
            binding.fabAdd.startAnimation(anim_fab_open)
            binding.txtFabAddLeft.startAnimation(anim_fab_open)
            //find favorite drinks
            if(isFav1){
                binding.fabFav1.visibility = View.VISIBLE
                binding.fabFav1.startAnimation(anim_fab_open)
                binding.txtFav1.visibility = View.VISIBLE
                binding.txtFav1.startAnimation(anim_fab_open)
            }
            if(isFav2){
                binding.fabFav2.visibility = View.VISIBLE
                binding.fabFav2.startAnimation(anim_fab_open)
                binding.txtFav2.visibility = View.VISIBLE
                binding.txtFav2.startAnimation(anim_fab_open)
            }
            isFabOpen = true
        }
    }
    private fun openAddModal(){
        val bottomSheet = SetIntakeModal(Code.MAIN_FRAGMENT, null)
        val fragmentManager = supportFragmentManager
        bottomSheet.show(fragmentManager,bottomSheet.tag)
    }

}
