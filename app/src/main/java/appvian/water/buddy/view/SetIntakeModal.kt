package appvian.water.buddy.view

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import appvian.water.buddy.R
import appvian.water.buddy.model.data.Category
import appvian.water.buddy.model.data.Intake
import appvian.water.buddy.viewmodel.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_modal.*
import kotlinx.android.synthetic.main.bottom_sheet_modal.view.*
import kotlinx.coroutines.InternalCoroutinesApi


class SetIntakeModal : BottomSheetDialogFragment() , TextWatcher{
    private lateinit var homeViewModel: HomeViewModel
    var categoryList = arrayListOf<Category>()
    //ontext changed 결과값
    var result = ""
    override fun getTheme(): Int = R.style.RoundBottomSheetDialog

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireActivity(),theme)

    @InternalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v = inflater.inflate(R.layout.bottom_sheet_modal,container,false)

        var typeofDrink = -1
        setCategory()
        v.recyclerview.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            adapter = CategoryRecyclerViewAdapter(
                context,
                categoryList
            ) { category ->
                typeofDrink = category.id
            }
        }

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        v.setButton.setOnClickListener {
            val regex = Regex("[^0-9]")
            val pickedNum = edt_amount.text.toString().replace(regex,"").toInt()
            val now = System.currentTimeMillis()
            val intake = Intake(now,typeofDrink,pickedNum)
            homeViewModel.insert(intake)
            onDestroyView()
            onDestroy()
        }

        v.img_cancel.setOnClickListener{
            onDestroyView()
            onDestroy()
        }



        //dialog!!.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        v.edt_amount.addTextChangedListener(this)
        return v
    }

    private fun setCategory(){
        categoryList.add(Category(0,"물","icon_water"))
        categoryList.add(Category(1,"커피","icon_coffee"))
        categoryList.add(Category(2,"차","icon_tea"))
        categoryList.add(Category(3,"우유","icon_milk"))
        categoryList.add(Category(4,"탄산음료","icon_carbon"))
        categoryList.add(Category(5,"주스","icon_juice"))
        categoryList.add(Category(6,"주류","icon_alcohol"))
        categoryList.add(Category(6,"이온음료","icon_ion"))
        categoryList.add(Category(6,"기타","icon_etc"))

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
                setButton.setBackgroundColor(resources.getColor(R.color.btnBlueInactive, null))
                setButton.setTextColor(resources.getColor(R.color.grey_1, null))
                setButton.isEnabled = false
            }else {
                result = cur + "ml"
                edt_amount.setText(result)
                edt_amount.setSelection(result.length - 2)
                setButton.setBackgroundColor(resources.getColor(R.color.btnBlueNormal, null))
                setButton.setTextColor(resources.getColor(R.color.white, null))
                setButton.isEnabled = true
            }
        }
    }
}