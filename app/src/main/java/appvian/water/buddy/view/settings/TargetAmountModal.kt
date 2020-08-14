package appvian.water.buddy.view.settings

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import appvian.water.buddy.R
import appvian.water.buddy.viewmodel.SettingViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.target_amount_bottom_sheet.*
import kotlinx.android.synthetic.main.target_amount_bottom_sheet.view.*
import kotlin.math.roundToInt

class TargetAmountModal(var textView: TextView, var checkBox: CheckBox) : BottomSheetDialogFragment(), TextWatcher {
    //ontext changed 결과값
    var result = ""
    private lateinit var settingViewModel: SettingViewModel
    private var target_amount = 0
    override fun getTheme(): Int = R.style.RoundBottomSheetDialog
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireActivity(),theme)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val regex = Regex("[^0-9]")
        target_amount = textView.text.replace(regex, "").toInt()
        val v = inflater.inflate(R.layout.target_amount_bottom_sheet, container, false)
        settingViewModel = ViewModelProvider(this).get(SettingViewModel::class.java)
        v.img_cancel.setOnClickListener{
            onDestroyView()
            onDestroy()
        }

        v.edt_target_amount.setText(target_amount.toString() + "ml")
        v.edt_target_amount.addTextChangedListener(this)
        v.edt_target_amount.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){
                Log.d("TAG","focus")
            }else{
                Log.d("TAG","un focus")
                hideKeyBoard()
            }
        }
        v.edt_target_amount.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_DONE){
                Log.d("TAG","ime action done")
                hideKeyBoard()
                v.edt_target_amount.clearFocus()
                true
            }else false
        }
        v.edt_target_amount.post(Runnable {
            v.edt_target_amount.isFocusableInTouchMode = true
            v.edt_target_amount.requestFocus()
            v.edt_target_amount.setSelection(target_amount.toString().length)
            upKeyboard()
        })

        v.btn_save.setOnClickListener {
            val regex = Regex("[^0-9]")
            val result_int_val = edt_target_amount.text.toString().replace(regex,"").toInt()
            textView.text = getString(R.string.target_amount, result_int_val.toString())
            checkBox.isChecked = false
            onDestroyView()
            onDestroy()
        }
        return v
    }

    //키보드 올리기
    private fun upKeyboard(){
        var imm = (requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
        imm.showSoftInput(edt_target_amount, InputMethodManager.SHOW_FORCED)
    }
    //키보드 숨기기
    private fun hideKeyBoard(){
        var imm = (requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
        imm.hideSoftInputFromWindow(edt_target_amount.windowToken, 0)
    }
    private fun saveBtnClickable(){
        btn_save.setBackgroundColor(resources.getColor(R.color.btnBlueNormal, null))
        btn_save.setTextColor(resources.getColor(R.color.white, null))
        btn_save.isEnabled = true
    }
    private fun saveBtnUnClickable(){
        btn_save.setBackgroundColor(resources.getColor(R.color.btnBlueInactive, null))
        btn_save.setTextColor(resources.getColor(R.color.grey_1, null))
        btn_save.isEnabled = false
    }

    override fun afterTextChanged(s: Editable?) {
        if(s.toString().equals(target_amount.toString() + "ml")){
            edt_target_amount.setTextColor(resources.getColor(R.color.grey_7, null))
            saveBtnUnClickable()
        }else{
            edt_target_amount.setTextColor(resources.getColor(R.color.settingTxtBlack, null))
            saveBtnClickable()
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        var cur = s.toString()
        if(!cur.equals(result)){//오버플로우 방지
            val regex = Regex("[^0-9]")
            cur = cur.replace(regex, "")
            if(cur.isBlank()){
                edt_target_amount.text.clear()
                result = ""
                saveBtnUnClickable()
            }else {
                result = cur + "ml"
                edt_target_amount.setText(result)
                edt_target_amount.setSelection(result.length - 2)
                saveBtnClickable()
            }
        }
    }
    override fun onDestroyView() {
        hideKeyBoard()
        super.onDestroyView()
    }


}