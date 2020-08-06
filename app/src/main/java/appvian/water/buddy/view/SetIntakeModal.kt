package appvian.water.buddy.view

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
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


class SetIntakeModal : BottomSheetDialogFragment() {
    private lateinit var homeViewModel: HomeViewModel
    var categoryList = arrayListOf<Category>()
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

        v.setButton.setBackgroundColor(Color.CYAN)
        v.setButton.setOnClickListener {
            val pickedNum = edt_amount.text.toString().toInt()
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
}