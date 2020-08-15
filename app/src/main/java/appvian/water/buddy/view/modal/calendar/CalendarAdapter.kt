package appvian.water.buddy.view.modal.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import appvian.water.buddy.R
import appvian.water.buddy.databinding.WbCalendarItemBinding

class CalendarAdapter : RecyclerView.Adapter<CalendarAdapter.CalendarVh>() {
    private var dayList = arrayListOf<Int?>()
    private lateinit var binding: WbCalendarItemBinding

    inner class CalendarVh(binding: WbCalendarItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Int?) {
            item?.let {
                binding.calDayTv.text = item.toString()
            }
        }
    }

    fun setDayList(dayList: List<Int?>) {
        this.dayList.clear()
        this.dayList.addAll(dayList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarVh {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.wb_calendar_item,
            parent,
            false
        )
        return CalendarVh(binding)
    }

    override fun getItemCount(): Int {
        return dayList.size
    }

    override fun onBindViewHolder(holder: CalendarVh, position: Int) {
        holder.setIsRecyclable(false)
        holder.bind(dayList[position])
    }
}