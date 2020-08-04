package appvian.water.buddy.view.analytics.chart.daily

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import appvian.water.buddy.R
import appvian.water.buddy.databinding.ItemDailyIntakeListBinding
import appvian.water.buddy.model.data.Intake

class DailyAdapter : RecyclerView.Adapter<DailyAdapter.DailyVh>() {
    private var dailyItem = ArrayList<Intake>()
    var totalSum:Int = 0

    fun setData(listItem: List<Intake>) {
        dailyItem.clear()
        dailyItem.addAll(listItem)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyVh {
        val binding = DataBindingUtil.inflate<ItemDailyIntakeListBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_daily_intake_list,
            parent,
            false
        )
        return DailyVh(binding)
    }

    override fun getItemCount(): Int {
        return dailyItem.size ?: 0
    }

    override fun onBindViewHolder(holder: DailyVh, position: Int) {
        holder.bind(dailyItem[position])
    }

    inner class DailyVh(val binding: ItemDailyIntakeListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Intake) {
            binding.intake = item
            binding.total = totalSum
            binding.dailyItemName.text = item.category.toString()
        }
    }
}