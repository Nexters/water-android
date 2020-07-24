package appvian.water.buddy.view

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import appvian.water.buddy.R
import appvian.water.buddy.model.data.Category
import appvian.water.buddy.model.data.Intake
import appvian.water.buddy.viewmodel.HomeViewModel
import appvian.water.buddy.viewmodel.MyRecyclerviewAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.InternalCoroutinesApi


class MyModalBottomSheet : BottomSheetDialogFragment() {
    private lateinit var homeViewModel: HomeViewModel
    var categoryList = arrayListOf<Category>()
    override fun getTheme(): Int = R.style.RoundBottomSheetDialog

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(activity!!,theme)

    @InternalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        categoryList.add(Category(0,"Water",""))
        categoryList.add(Category(1,"Coffee",""))
        categoryList.add(Category(2,"Juice",""))
        categoryList.add(Category(3,"Coke",""))
        categoryList.add(Category(4,"Alcohol",""))
        categoryList.add(Category(5,"Milk",""))
        val v = inflater.inflate(R.layout.bottom_sheet_modal,container,false)
        val recyclerview : RecyclerView = v.findViewById(R.id.recyclerview)
        val numberPicker : NumberPicker = v.findViewById(R.id.numberpicker)
        val cancelText : TextView = v.findViewById(R.id.cancelButton)
        val setButton : Button = v.findViewById(R.id.setButton)
        var typeofDrink = -1
        recyclerview.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            adapter = MyRecyclerviewAdapter(context,categoryList) {category ->
                typeofDrink = category.id
            }
        }

        val num_of_value = 10
        var displayedValues = arrayOf("0ml","50ml","100ml","150ml","200ml","250ml","300ml","350ml","400ml","450ml","500ml")
        numberPicker.minValue = 0
        numberPicker.maxValue = num_of_value
        numberPicker.displayedValues = displayedValues

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        setButton.setBackgroundColor(Color.CYAN)
        setButton.setOnClickListener {
            val pickedNum = numberPicker.value*50
            val now = System.currentTimeMillis()
            val intake = Intake(now,typeofDrink,pickedNum)
            homeViewModel.insert(intake)
            onDestroyView()
            onDestroy()
        }

        cancelText.setOnClickListener{
            onDestroyView()
            onDestroy()
        }
        return v
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }
}