package appvian.water.buddy.view.modal.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import appvian.water.buddy.R
import appvian.water.buddy.databinding.WbCalendarItemBinding
import appvian.water.buddy.util.TimeUtil

class CalendarAdapter(val calendarDayListener: CalendarDayListener) : RecyclerView.Adapter<CalendarAdapter.CalendarVh>() {
    private var dayList = arrayListOf<Int?>()
    private lateinit var binding: WbCalendarItemBinding
    private val curDay = TimeUtil.day
    var checkedPos = -1

    inner class CalendarVh(binding: WbCalendarItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Int?) {
            item?.let {
                binding.calDayTv.text = item.toString()

                if(checkedPos != -1) {
                    if (checkedPos == adapterPosition)
                        binding.calDayView.background =
                            binding.root.resources.getDrawable(R.drawable.circle, null)
                    else
                        binding.calDayView.setBackgroundColor(
                            binding.root.resources.getColor(
                                R.color.transparent,
                                null
                            )
                        )
                }

                binding.calDayView.setOnClickListener {
                    binding.calDayView.background = binding.root.resources.getDrawable(R.drawable.circle, null)
                    if(checkedPos != adapterPosition) {
                        checkedPos = adapterPosition
                        notifyDataSetChanged()

                        calendarDayListener.getCalendarDay(item)
                    }
                }
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