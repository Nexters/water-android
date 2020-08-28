package appvian.water.buddy.view.modal.calendar

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import appvian.water.buddy.R
import appvian.water.buddy.databinding.WbCalendarItemBinding
import appvian.water.buddy.viewmodel.modal.CalendarViewModel

class CalendarAdapter(val calendarVm:CalendarViewModel, val calendarDayListener: CalendarDayListener) :
    RecyclerView.Adapter<CalendarAdapter.CalendarVh>() {
    private var dayList = arrayListOf<Int?>()
    private lateinit var binding: WbCalendarItemBinding

    var checkedPos = -1

    inner class CalendarVh(binding: WbCalendarItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Int?) {
            item?.let {
                binding.calDayTv.text = item.toString()

                if(calendarVm.isFutureDay(item)) {
                   setTextColor(binding.root.resources.getColor(R.color.grey_7, null))
                    return@bind
                }

                setDayCheck()
                setViewClick(item)
            }
        }

        private fun setDayCheck() {
            if (checkedPos != -1) {
                if (checkedPos == adapterPosition) {
                    setBackground(true)
                    setTextColor(Color.WHITE)
                } else {
                    setBackground(false)
                    setTextColor(Color.BLACK)
                }
            }
        }

        private fun setViewClick(item:Int) {
            binding.calDayView.setOnClickListener {
                if (checkedPos != adapterPosition) {
                    checkedPos = adapterPosition
                    notifyDataSetChanged()

                    calendarDayListener.getCalendarDay(item)
                }
            }
        }

        private fun setTextColor(color:Int) {
            binding.calDayTv.setTextColor(color)
        }

        private fun setBackground(isChecked:Boolean) {
            if(isChecked) {
                binding.calDayView.background =
                    binding.root.resources.getDrawable(R.drawable.circle, null)
            } else {
                binding.calDayView.setBackgroundColor(
                    binding.root.resources.getColor(
                        R.color.transparent,
                        null
                    )
                )
            }
        }

    }

    fun setDayList(dayList: List<Int?>) {
        this.dayList.clear()
        this.dayList.addAll(dayList)

        initItemCheckPosition()
        notifyDataSetChanged()
    }

    private fun initItemCheckPosition() {
        checkedPos = -1
        calendarDayListener.getCalendarDay(-1)
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