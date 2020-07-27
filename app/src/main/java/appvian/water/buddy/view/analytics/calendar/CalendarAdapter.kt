package appvian.water.buddy.view.analytics.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import appvian.water.buddy.R
import appvian.water.buddy.databinding.WbCalendarItemBinding
import appvian.water.buddy.model.data.CalendarData

class CalendarAdapter : RecyclerView.Adapter<CalendarAdapter.CalViewHolder>() {
    private lateinit var binding: WbCalendarItemBinding
    private val dateList = ArrayList<CalendarData>()

    inner class CalViewHolder(private val binding: WbCalendarItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CalendarData) {
            binding.calDayTv.text = item.date
            item.img?.let { img ->
                binding.calDayImg.setImageResource(img)
            } ?: kotlin.run {
                binding.calDayImg.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.wb_calendar_item,
            parent,
            false
        )

        return CalViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dateList.size
    }

    override fun onBindViewHolder(holder: CalViewHolder, position: Int) {
        holder.bind(dateList[position])
    }

    fun setDateList(list: List<CalendarData>) {
        dateList.clear()
        dateList.addAll(list)
        notifyDataSetChanged()
    }
}