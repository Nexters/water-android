package appvian.water.buddy.view.settings

import android.content.Context
import android.content.Intent
import android.hardware.input.InputManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import appvian.water.buddy.R
import appvian.water.buddy.databinding.ActivityProfileEditBinding
import appvian.water.buddy.databinding.FragmentSettingBinding
import appvian.water.buddy.util.DrinkMapper
import appvian.water.buddy.utilities.ProfileImgMapper
import appvian.water.buddy.viewmodel.SettingViewModel
import kotlinx.android.synthetic.main.activity_profile_edit.*
import java.util.*

class ProfileEditActivity : AppCompatActivity(), TextWatcher {
    private lateinit var profileEditBinding: ActivityProfileEditBinding
    private lateinit var settingviewModel: SettingViewModel
    private val CODE_POPUP = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        profileEditBinding = DataBindingUtil.setContentView(this@ProfileEditActivity, R.layout.activity_profile_edit)
        settingviewModel = ViewModelProvider(this).get(SettingViewModel::class.java)
        profileEditBinding.viewModel = settingviewModel
        initUI()
    }
    private fun initUI(){

        edt_profile_edt.addTextChangedListener(this)
        edt_profile_edt.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){
                Log.d("TAG","focus")
                txt_profile_edt_cnt.visibility = View.VISIBLE
            }else{
                Log.d("TAG","un focus")
                txt_profile_edt_cnt.visibility = View.INVISIBLE
                hideKeyBoard()
            }
        }
        edt_profile_edt.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_DONE){
                Log.d("TAG","ime action done")
                hideKeyBoard()
                edt_profile_edt.clearFocus()
                true
            }else false
        })
        upKeyboard()
        settingviewModel.profile_img_live_data.observe(this, Observer {
            profileEditBinding.imgProfileEdt.setImageDrawable(resources.getDrawable(ProfileImgMapper.profile_imgs.get(it), null))
        })
        settingviewModel.nameLiveData.observe(this, Observer {

        })
        settingviewModel.name_cnt.observe(this, Observer {
            profileEditBinding.txtProfileEdtCnt.text = String.format(getString(R.string.edtLenCntFormat, it))
        })
        img_profile_edt_arrow.setOnClickListener{
            onBackPressed()
        }
        btn_save.setOnClickListener {
            settingviewModel.setNameLiveData(edt_profile_edt.text.toString())
            var intent = Intent(this@ProfileEditActivity, PopupActivity::class.java)
            intent.putExtra("Message", getString(R.string.nickname_change))
            startActivityForResult(intent, CODE_POPUP)
        }
    }


    override fun afterTextChanged(s: Editable?) {
        Log.d("TAG", "after text changed")
        //todo: 만약 s와 viewmodel의 name이 같거나 s가 글자가0개 또는 7개면 저장 버튼 비활성화
        if(s.isNullOrBlank() || settingviewModel.nameLiveData.value.equals(s.toString())){
            btn_save.isEnabled = false
            btn_save.background = resources.getDrawable(R.drawable.setting_save_btn_inactive, null)
        }else{
            btn_save.isEnabled = true
            btn_save.background = resources.getDrawable(R.drawable.setting_save_btn, null)
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        Log.d("TAG", "before text changed")
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if(s != null) {
            settingviewModel.name_cnt.value = s.length
        }
    }
    //키보드 올리기
    private fun upKeyboard(){
        var imm = (applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)

    }
    //키보드 숨기기
    private fun hideKeyBoard(){
        var imm = (applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
        imm.hideSoftInputFromWindow(edt_profile_edt.windowToken, 0)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            CODE_POPUP -> {
                finish()
            }
        }
    }

    override fun onPause() {
        hideKeyBoard()
        super.onPause()
    }
}