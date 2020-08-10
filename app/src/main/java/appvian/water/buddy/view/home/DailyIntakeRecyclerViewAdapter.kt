package appvian.water.buddy.view.home


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import appvian.water.buddy.R
import appvian.water.buddy.model.data.Intake
import appvian.water.buddy.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.daily_intake_recyclerview_item.view.*
import kotlinx.android.synthetic.main.modal_category_recyclerview_item.view.*
import java.text.SimpleDateFormat

class DailyIntakeRecyclerViewAdapter (val context : Context, val isDeleteClicked: Boolean, val viewModel: HomeViewModel, val intakeList : List<Intake>, val itemClick: (Intake) -> Unit) :
    RecyclerView.Adapter<DailyIntakeRecyclerViewAdapter.IntakeViewHolder>(){

    val categoryString = arrayListOf<String>("물","커피","차","우유","탄산음료","주스","주류","이온음료","기타")
    inner class IntakeViewHolder(itemView : View, itemClick: (Intake) -> Unit) : RecyclerView.ViewHolder(itemView) {

        fun bind (intake: Intake, position: Int, context: Context){
            when(intake.category){
                0 -> {
                    val resourceId = context.resources.getIdentifier("icon_water", "drawable", context.packageName)
                    itemView.icon.setImageResource(resourceId)
                }
                1 -> {
                    val resourceId = context.resources.getIdentifier("icon_coffee", "drawable", context.packageName)
                    itemView.icon.setImageResource(resourceId)
                }
                2 -> {
                    val resourceId = context.resources.getIdentifier("icon_tea", "drawable", context.packageName)
                    itemView.icon.setImageResource(resourceId)
                }
                3 -> {
                    val resourceId = context.resources.getIdentifier("icon_milk", "drawable", context.packageName)
                    itemView.icon.setImageResource(resourceId)
                }
                4 -> {
                    val resourceId = context.resources.getIdentifier("icon_carbon", "drawable", context.packageName)
                    itemView.icon.setImageResource(resourceId)
                }
                5 -> {
                    val resourceId = context.resources.getIdentifier("icon_juice", "drawable", context.packageName)
                    itemView.icon.setImageResource(resourceId)
                }
                6 -> {
                    val resourceId = context.resources.getIdentifier("icon_alcohol", "drawable", context.packageName)
                    itemView.icon.setImageResource(resourceId)
                }
                7 -> {
                    val resourceId = context.resources.getIdentifier("icon_ion", "drawable", context.packageName)
                    itemView.icon.setImageResource(resourceId)
                }
                8 -> {
                    val resourceId = context.resources.getIdentifier("icon_etc", "drawable", context.packageName)
                    itemView.icon.setImageResource(resourceId)
                }
            }
            itemView.categoryText.text = categoryString[intake.category]
            itemView.amountText.text = String.format("%dml",intake.amount)
            itemView.am_pm_text.text = getAmORPm(intake.date)
            itemView.time_text.text = getTime(intake.date)
            if (isDeleteClicked){
                itemView.delete_checkbox.visibility = View.VISIBLE
            } else{
                itemView.delete_checkbox.visibility = View.GONE
            }
            if(itemView.delete_checkbox.isPressed){
                viewModel.addDeleteList(intake)
            } else{
                viewModel.cancelDeleteList(intake)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntakeViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.daily_intake_recyclerview_item,parent,false)
        return IntakeViewHolder(view,itemClick)
    }

    override fun onBindViewHolder(holder: IntakeViewHolder, position: Int) {
        holder.bind(intakeList[position],position,context)
    }

    override fun getItemCount(): Int {
        return intakeList.size
    }

    private fun getAmORPm(date: Long): String{
        val hourFormat = SimpleDateFormat("HH")
        val hourString = hourFormat.format(date)
        val hourInt = Integer.parseInt(hourString)
        var amORpm = ""
        when(hourInt) {
            in 12..24 -> {
                amORpm = "오후"
            }
            else -> {
                amORpm = "오전"
            }
        }
        return amORpm
    }

    private fun getTime(date: Long): String{
        val timeFormat = SimpleDateFormat("hh:mm")
        val timeString = timeFormat.format(date)
        return timeString
    }
}