package appvian.water.buddy.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import appvian.water.buddy.R
import appvian.water.buddy.databinding.WbCalendarItemBinding
import appvian.water.buddy.model.data.CalendarData

class WbCalcAdapter : RecyclerView.Adapter<WbCalcAdapter.WbViewHolder>() {
    private lateinit var binding: WbCalendarItemBinding
    private val dateList = ArrayList<CalendarData>()

    inner class WbViewHolder(private val binding: WbCalendarItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CalendarData) {
            binding.calDayTv.text = item.date
            item.img?.let { img ->
                binding.calDayImg.setImageResource(img)
            } ?: kotlin.run {
                binding.calDayImg.setBackgroundColor(binding.root.context.getColor(R.color.transparent))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WbViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.wb_calendar_item,
            parent,
            false
        )

        return WbViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dateList.size
    }

    override fun onBindViewHolder(holder: WbViewHolder, position: Int) {
        holder.bind(dateList[position])
    }

    fun setDateList(list: List<CalendarData>) {
        dateList.clear()
        dateList.addAll(list)
        notifyDataSetChanged()
    }
}