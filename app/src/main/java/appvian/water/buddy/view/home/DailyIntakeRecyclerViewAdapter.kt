package appvian.water.buddy.view.home


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import appvian.water.buddy.R
import appvian.water.buddy.model.data.Intake
import appvian.water.buddy.util.DrinkMapper
import appvian.water.buddy.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.daily_intake_recyclerview_item.view.*
import java.text.SimpleDateFormat

class DailyIntakeRecyclerViewAdapter (val context : Context, val activity: AppCompatActivity, val isDeleteClicked: Boolean, val intakeList : List<Intake>, val itemClick: (Intake) -> Unit) :
    RecyclerView.Adapter<DailyIntakeRecyclerViewAdapter.IntakeViewHolder>(){
    private var viewModel= ViewModelProvider(activity).get(HomeViewModel::class.java)
    val categoryString = context.resources.getStringArray(R.array.drink_category)
    inner class IntakeViewHolder(itemView : View, itemClick: (Intake) -> Unit) : RecyclerView.ViewHolder(itemView) {

        fun bind (intake: Intake, position: Int, context: Context){
            itemView.icon.setImageResource(DrinkMapper.drinkResources[intake.category])
            itemView.categoryText.text = categoryString[intake.category]
            itemView.amountText.text = String.format("%dml",intake.amount)
            itemView.am_pm_text.text = getAmORPm(intake.date)
            itemView.time_text.text = getTime(intake.date)
            itemView.delete_checkbox.setOnClickListener {
                if(itemView.delete_checkbox.isChecked){
                    viewModel.addDeleteList(intake)
                } else{
                    viewModel.cancelDeleteList(intake)
                }
            }
            if (isDeleteClicked){
                itemView.delete_checkbox.visibility = View.VISIBLE
            } else{
                itemView.delete_checkbox.visibility = View.GONE
                viewModel.cancelDeleteList(intake)
            }
            itemView.setOnClickListener {
                itemClick(intake)
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