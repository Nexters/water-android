package appvian.water.buddy.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import appvian.water.buddy.R
import appvian.water.buddy.databinding.ActivityMainBinding
import appvian.water.buddy.model.data.Intake
import appvian.water.buddy.util.DrinkMapper
import appvian.water.buddy.utilities.CategoryMapper
import appvian.water.buddy.utilities.Code
import appvian.water.buddy.view.home.DailyIntakeListActivity
import appvian.water.buddy.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var anim_fab_open : Animation
    private lateinit var anim_fab_closed : Animation
    private var isFabOpen = false
    private var isFav1 = false
    private var isFav2 = false
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)
        viewModel = MainViewModel(this)
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
            val st = StringTokenizer(binding.txtFav1.text.toString())
            val category_num = CategoryMapper.getCategoryNum(st.nextToken())
            val tmp = st.nextToken()
            viewModel.addIntake(Intake(System.currentTimeMillis(), category_num, tmp.substring(0, tmp.length-2).toInt()))
            fabAnim()
        }
        binding.fabFav2.setOnClickListener{
            //바로 등록
            val st = StringTokenizer(binding.txtFav2.text.toString())
            val category_num = CategoryMapper.getCategoryNum(st.nextToken())
            val tmp = st.nextToken()
            viewModel.addIntake(Intake(System.currentTimeMillis(), category_num, tmp.substring(0, tmp.length-2).toInt()))
            fabAnim()
        }
        viewModel.fav_1_liveData.observe(this, Observer {
            if(it.isNullOrBlank()){
                isFav1 = false
                binding.fabFav1.setImageDrawable(null)
                binding.txtFav1.text = ""
            }else{
                isFav1 = true
                val st = StringTokenizer(it)
                val category_num = st.nextToken().toInt()
                val amount = st.nextToken().toInt()
                val category_name = CategoryMapper.getCategoryName(category_num)

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
                val st = StringTokenizer(it)
                val category_num = st.nextToken().toInt()
                val amount = st.nextToken().toInt()
                val category_name = CategoryMapper.getCategoryName(category_num)
                binding.fabFav2.setImageDrawable(resources.getDrawable(DrinkMapper.drinkResources.get(category_num),null))
                binding.txtFav2.text = getString(R.string.fab_favorite, category_name, amount)
            }
        })
        initToast()
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

    private fun initToast(){
        viewModel.showInsertToast.observeForever( Observer {
            if (it){
                val intake = viewModel.latestIntake
                Snackbar.make(fragment,getString(R.string.insert_toast_text, resources.getStringArray(DrinkMapper.drinkName)[intake.category], intake.amount),Snackbar.LENGTH_SHORT).apply{
                    this.setBackgroundTint(getColor(R.color.black))
                    this.setAction("항목보기",View.OnClickListener {
                        val intent = Intent(this@MainActivity,DailyIntakeListActivity::class.java)
                        startActivity(intent)
                    })
                    this.view.minimumHeight = 150
                    this.view.foregroundGravity = Gravity.CENTER
                    this.setTextColor(getColor(R.color.White))
                    this.setActionTextColor(getColor(R.color.White))
                    this.show()
                }
                viewModel.showInsertToast.value = false
            }
        })

    }
}
