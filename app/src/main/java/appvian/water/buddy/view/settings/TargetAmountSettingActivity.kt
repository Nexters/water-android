package appvian.water.buddy.view.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import appvian.water.buddy.R
import appvian.water.buddy.databinding.ActivityTargetAmountSettingBinding
import appvian.water.buddy.viewmodel.SettingViewModel
import kotlinx.android.synthetic.main.activity_target_amount_setting.*
import java.lang.Exception
import kotlin.math.roundToInt

//(키 + 몸무게 ) / 100 >
class TargetAmountSettingActivity : AppCompatActivity() {
    private lateinit var settingViewModel: SettingViewModel
    private lateinit var bindig : ActivityTargetAmountSettingBinding
    private var target_amount_change_flag = false
    private var weight_change_flag = false
    private var height_change_flag = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindig = DataBindingUtil.setContentView(this, R.layout.activity_target_amount_setting)
        settingViewModel = ViewModelProvider(this).get(SettingViewModel::class.java)
        bindig.viewModel = settingViewModel

        initUI()

    }
    private fun initUI(){
        if(settingViewModel.is_new_live_data.value!!){
            bindig.layoutBubble.visibility = View.VISIBLE
            settingViewModel.setIsNewLiveData(false)
        }else{
            bindig.layoutBubble.visibility = View.GONE
        }
        settingViewModel.target_amount_int_live_data.observe(this, Observer {
        })
        settingViewModel.heightLiveData.observe(this, Observer {

        })
        settingViewModel.weightLiveData.observe(this, Observer {

        })
        settingViewModel.is_auto_settiing_live_data.observe(this, Observer {
            bindig.checkbox.isChecked = it
        })

        txt_target_amount.setOnClickListener{
            //modal open
            val bottomSheet = TargetAmountModal(txt_target_amount, checkbox)
            val fragmentManager = supportFragmentManager
            bottomSheet.show(fragmentManager,bottomSheet.tag)
        }
        txt_target_amount.addTextChangedListener(TxtTargetWatcher(getString(R.string.target_amount,settingViewModel.target_amount_int_live_data.value.toString())))
        edt_height.addTextChangedListener(EdtHeightWatcher(settingViewModel.heightLiveData.value.toString()))
        edt_weight.addTextChangedListener(EdtWeightWatcher(settingViewModel.weightLiveData.value.toString()))

        checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                //목표량 자동 계산
                setTargetAmount()
            }
        }
        bindig.btnSave.setOnClickListener {
            val regex = Regex("[^0-9]")
            val result_int_val = txt_target_amount.text.toString().replace(regex,"").toInt()
            val result_float_val = ((result_int_val.toFloat()/1000f) * 10).roundToInt().toFloat() / 10f
            settingViewModel.setTargetAmountFloatLiveData(result_float_val)
            settingViewModel.setTargetAmountIntLiveData(result_int_val)
            settingViewModel.setIsAutoSettingLiveData(checkbox.isChecked)
            settingViewModel.setWeightLiveData(edt_weight.text.toString().toInt())
            settingViewModel.setHeightLiveData(edt_height.text.toString().toInt())
            finish()
        }
    }
    inner class TxtTargetWatcher(var initText : String) : TextWatcher{
        override fun afterTextChanged(s: Editable?) {
            //변경되었다면 저장버튼 활성화
            if(s.toString() != initText){
                saveBtnClickable()
                target_amount_change_flag = true
            }else{
                target_amount_change_flag = false
                if(!height_change_flag && !weight_change_flag){
                    saveBtnUnClickable()
                }
            }

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }
    inner class EdtHeightWatcher(var initText : String) : TextWatcher{
        override fun afterTextChanged(s: Editable?) {
            //변경되었다면 저장버튼 활성화
            if(s.toString() != initText){
                saveBtnClickable()
                height_change_flag = true
                edt_height.setTextColor(resources.getColor(R.color.settingTxtBlack, null))
            }else{
                height_change_flag = false
                if(!weight_change_flag && !target_amount_change_flag){
                    saveBtnUnClickable()
                }
                edt_height.setTextColor(resources.getColor(R.color.grey_6, null))
            }
            if(checkbox.isChecked){
                setTargetAmount()
            }
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

    }
    inner class EdtWeightWatcher(var initText: String) : TextWatcher{
        override fun afterTextChanged(s: Editable?) {
            //변경되었다면 저장버튼 활성화
            if(s.toString() != initText){
                saveBtnClickable()
                weight_change_flag = true
                edt_weight.setTextColor(resources.getColor(R.color.settingTxtBlack, null))
            }else{
                weight_change_flag = false
                if(!height_change_flag && !target_amount_change_flag){
                    saveBtnUnClickable()
                }
                edt_weight.setTextColor(resources.getColor(R.color.grey_6, null))
            }
            if(checkbox.isChecked){
                setTargetAmount()
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

    }
    private fun saveBtnClickable(){
        bindig.btnSave.background = resources.getDrawable(R.drawable.setting_save_btn, null)
        bindig.btnSave.setTextColor(resources.getColor(R.color.white, null))
        bindig.btnSave.isEnabled = true
    }
    private fun saveBtnUnClickable(){
        bindig.btnSave.background = resources.getDrawable(R.drawable.setting_save_btn_inactive, null)
        bindig.btnSave.setTextColor(resources.getColor(R.color.grey_1, null))
        bindig.btnSave.isEnabled = false
    }
    //현재 edit text에 보여지는 몸무게, 키 값으로 목표량을 계산해서 text view 수정
    private fun setTargetAmount(){
        try{
            txt_target_amount.text = getString(R.string.target_amount,((edt_weight.text.toString().toInt() + edt_height.text.toString().toInt()) * 10).toString())
        }catch (e : Exception){
            Log.e("TAG", e.message)
        }

    }

}