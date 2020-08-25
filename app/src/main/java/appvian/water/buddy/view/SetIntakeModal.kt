package appvian.water.buddy.view

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import appvian.water.buddy.R
import appvian.water.buddy.model.data.Category
import appvian.water.buddy.model.data.Intake
import appvian.water.buddy.util.DrinkMapper
import appvian.water.buddy.utilities.Code
import appvian.water.buddy.view.home.DailyIntakeListActivity
import appvian.water.buddy.view.settings.PopupActivity
import appvian.water.buddy.viewmodel.FavoriteViewModel
import appvian.water.buddy.viewmodel.HomeViewModel
import appvian.water.buddy.viewmodel.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet_modal.*
import kotlinx.android.synthetic.main.bottom_sheet_modal.view.*
import kotlinx.coroutines.InternalCoroutinesApi
import java.util.*


class SetIntakeModal(var parent_context_code : Int, var intake : Intake?) : BottomSheetDialogFragment() , TextWatcher{
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var favoriteViewModel: FavoriteViewModel
    var categoryList = arrayListOf<Category>()
    //ontext changed 결과값
    var result = ""
    private var selected : Array<Boolean> = Array(3) { i -> false }
    private var selected_idx = 0
    private lateinit var selected_btns : Array<Button>

    private val PAPER_CUP = "200"
    private val TALL_CUP = "350"
    private val TUMBLER_CUP = "600"
    override fun getTheme(): Int = R.style.RoundBottomSheetDialog

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireActivity(),theme)

    @InternalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v = inflater.inflate(R.layout.bottom_sheet_modal,container,false)

        var typeofDrink = 0
        setCategory()
        v.recyclerview.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            adapter = CategoryRecyclerViewAdapter(
                context,
                categoryList
            ) { category ->
                typeofDrink = category.id
                setBtnClickable()
            }
        }
        if(parent_context_code == Code.MAIN_FRAGMENT) {
            mainViewModel = MainViewModel(this.requireActivity())
        }
        else if(parent_context_code == Code.FAVORITE_DRINK_SETTING_ACTIVITY || parent_context_code == Code.FAVORITE_EDIT_1 || parent_context_code == Code.FAVORITE_EDIT_2){
            favoriteViewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)
            if(parent_context_code == Code.FAVORITE_EDIT_1){
                v.txt_top.text = getText(R.string.add_fav_modal)
                var stringTokenizer = StringTokenizer(favoriteViewModel.fav_1_livedata.value)
                var category = stringTokenizer.nextToken()
                var amount = stringTokenizer.nextToken()
                v.edt_amount.setText(amount + "ml")
                v.edt_amount.setSelection(amount.length)
                (v.recyclerview.adapter as CategoryRecyclerViewAdapter).setCategory(category.toInt())
                typeofDrink = category.toInt()
            }else if(parent_context_code == Code.FAVORITE_EDIT_2){
                v.txt_top.text = getText(R.string.add_fav_modal)
                var stringTokenizer = StringTokenizer(favoriteViewModel.fav_2_livedata.value)
                var category = stringTokenizer.nextToken()
                var amount = stringTokenizer.nextToken()
                v.edt_amount.setText(amount + "ml")
                v.edt_amount.setSelection(amount.length)
                (v.recyclerview.adapter as CategoryRecyclerViewAdapter).setCategory(category.toInt())
                typeofDrink = category.toInt()
            }
        }else if(parent_context_code == Code.HOME_FRAGMENT){
            homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
            v.txt_top.text = getString(R.string.modify_modal)
            v.edt_amount.setText(intake!!.amount.toString() + "ml")
            v.edt_amount.setSelection(intake!!.amount.toString().length)
            v.setButton.text = "수정하기"
            (v.recyclerview.adapter as CategoryRecyclerViewAdapter).setCategory(intake!!.category)
            typeofDrink = intake!!.category
        }

        v.setButton.setOnClickListener {
            val regex = Regex("[^0-9]")
            val pickedNum = edt_amount.text.toString().replace(regex,"").toInt()
            when(parent_context_code){
                Code.MAIN_FRAGMENT -> {
                    val intake = Intake(System.currentTimeMillis(), typeofDrink, pickedNum)
                    mainViewModel.addIntake(intake)
                    Snackbar.make(requireActivity().findViewById(android.R.id.content),getString(R.string.insert_toast_text, resources.getStringArray(
                        DrinkMapper.drinkName)[intake.category], intake.amount), Snackbar.LENGTH_SHORT).apply{
                        this.setBackgroundTint(getColor(context,R.color.black))
                        this.setAction("항목보기",View.OnClickListener {
                            val intent = Intent(context, DailyIntakeListActivity::class.java)
                            startActivity(intent)
                        })
                        this.view.minimumHeight = 150
                        this.view.foregroundGravity = Gravity.CENTER
                        this.setTextColor(getColor(context,R.color.White))
                        this.setActionTextColor(getColor(context,R.color.White))
                        this.show()
                    }
                }
                Code.FAVORITE_DRINK_SETTING_ACTIVITY -> {
                    if(favoriteViewModel.fav_1_livedata.value == ""){
                        favoriteViewModel.setFav1LiveData(typeofDrink, pickedNum)
                    }else{
                        favoriteViewModel.setFa2LiveData(typeofDrink, pickedNum)
                    }
                    //popup 띄우기
                    var intent = Intent(context, PopupActivity::class.java)
                    intent.putExtra("Message", getString(R.string.favorite_drink_add))
                    startActivity(intent)
                }
                Code.FAVORITE_EDIT_1 -> {
                    favoriteViewModel.setFav1LiveData(typeofDrink, pickedNum)
                }
                Code.FAVORITE_EDIT_2 -> {
                    favoriteViewModel.setFa2LiveData(typeofDrink, pickedNum)
                }
                Code.HOME_FRAGMENT -> {
                    //오늘 마신 물 수정
                    homeViewModel.modifyAmount(intake!!.date, pickedNum)
                    homeViewModel.modifyCategory(intake!!.date, typeofDrink)
                    Snackbar.make(requireActivity().findViewById(android.R.id.content),getString(R.string.modify_toast_text, resources.getStringArray(
                        DrinkMapper.drinkName)[typeofDrink], pickedNum), Snackbar.LENGTH_SHORT).apply{
                        this.setBackgroundTint(getColor(context,R.color.black))
                        this.view.minimumHeight = 150
                        this.view.foregroundGravity = Gravity.CENTER
                        this.setTextColor(getColor(context,R.color.White))
                        this.show()
                    }
                }
            }

            onDestroyView()
            onDestroy()
        }

        v.img_cancel.setOnClickListener{
            onDestroyView()
            onDestroy()
        }
        selected_btns = arrayOf(v.btn_papercup, v.btn_takeout_tall, v.btn_tumbler)
        v.btn_papercup.setOnClickListener {
            btnClick(0)

        }
        v.btn_takeout_tall.setOnClickListener {
            btnClick(1)
        }
        v.btn_tumbler.setOnClickListener {
            btnClick(2)
        }



        v.edt_amount.addTextChangedListener(this)
        v.edt_amount.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){
                Log.d("TAG","focus")
            }else{
                Log.d("TAG","un focus")
                hideKeyBoard()
            }
        }
        v.edt_amount.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_DONE){
                Log.d("TAG","ime action done")
                hideKeyBoard()
                v.edt_amount.clearFocus()
                true
            }else false
        })
        v.edt_amount.post(Runnable {
            v.edt_amount.isFocusableInTouchMode = true
            v.edt_amount.requestFocus()
            upKeyboard()
        })
        return v
    }
    private fun btnClick(button_pos : Int){
        var idx = 0
        if(!selected[button_pos]){
            for(btn in selected_btns){
                if(idx == button_pos){
                    selected[idx] = true
                    selected_btns[idx].background = resources.getDrawable(R.drawable.btn_bottom_sheet_blue, null)
                    selected_btns[idx].setTextColor(resources.getColor(R.color.white, null))
                    selected_idx = idx
                    when(idx){
                        0 -> {
                            result = PAPER_CUP + "ml"
                        }
                        1 -> {
                            result = TALL_CUP + "ml"
                        }
                        2 -> {
                            result = TUMBLER_CUP + "ml"
                        }
                    }
                    edt_amount.setText(result)
                    edt_amount.setSelection(result.length - 2)
                    setBtnClickable()
                    edt_amount.isClickable = false
                    edt_amount.isFocusable = false
                }else{
                    selected[idx] = false
                    selected_btns[idx].background = resources.getDrawable(R.drawable.btn_bottom_sheet, null)
                    selected_btns[idx].setTextColor(resources.getColor(R.color.grey_7, null))
                }
                idx++
            }
        }else{
            edt_amount.text.clear()
            edt_amount.isFocusableInTouchMode = true
            edt_amount.isFocusable= true
            setBtnUnClickable()
            selected[button_pos] = false
            selected_btns[button_pos].background = resources.getDrawable(R.drawable.btn_bottom_sheet, null)
            selected_btns[button_pos].setTextColor(resources.getColor(R.color.grey_7, null))
        }
    }

    private fun setCategory(){
        categoryList.add(Category(0,"물",DrinkMapper.drinkResources[0].toString()))
        categoryList.add(Category(1,"커피",DrinkMapper.drinkResources[1].toString()))
        categoryList.add(Category(2,"차",DrinkMapper.drinkResources[2].toString()))
        categoryList.add(Category(3,"우유",DrinkMapper.drinkResources[3].toString()))
        categoryList.add(Category(4,"탄산음료",DrinkMapper.drinkResources[4].toString()))
        categoryList.add(Category(5,"주스",DrinkMapper.drinkResources[5].toString()))
        categoryList.add(Category(6,"주류",DrinkMapper.drinkResources[6].toString()))
        categoryList.add(Category(7,"이온음료",DrinkMapper.drinkResources[7].toString()))
        categoryList.add(Category(8,"기타",DrinkMapper.drinkResources[8].toString()))

    }


    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }

    override fun afterTextChanged(s: Editable?) {

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        var cur = s.toString()
        if(!cur.equals(result)){//오버플로우 방지
            val regex = Regex("[^0-9]")
            cur = cur.replace(regex, "")
            if(cur.isBlank()){
                edt_amount.text.clear()
                result = ""
                setBtnUnClickable()
            }else {
                result = cur + "ml"
                edt_amount.setText(result)
                edt_amount.setSelection(result.length - 2)
                setBtnClickable()
            }
        }
    }
    private fun setBtnClickable(){
        setButton.setBackgroundColor(resources.getColor(R.color.btnBlueNormal, null))
        setButton.setTextColor(resources.getColor(R.color.white, null))
        setButton.isEnabled = true
    }
    private fun setBtnUnClickable(){
        setButton.setBackgroundColor(resources.getColor(R.color.btnBlueInactive, null))
        setButton.setTextColor(resources.getColor(R.color.grey_1, null))
        setButton.isEnabled = false
    }
    //키보드 올리기
    private fun upKeyboard(){
        Log.d("TAG", "up keyboard")
        var imm = (requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
        imm.showSoftInput(edt_amount, InputMethodManager.HIDE_IMPLICIT_ONLY)

    }
    //키보드 숨기기
    private fun hideKeyBoard(){
        Log.d("TAG","hide key board")
        var imm = (requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
        imm.hideSoftInputFromWindow(edt_amount.windowToken, 0)
    }

}