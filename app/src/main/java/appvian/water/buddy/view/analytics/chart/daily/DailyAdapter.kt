package appvian.water.buddy.view.analytics.chart.daily

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import appvian.water.buddy.R
import appvian.water.buddy.databinding.ItemDailyIntakeListBinding
import appvian.water.buddy.model.data.Intake
import appvian.water.buddy.util.DrinkMapper

class DailyAdapter : RecyclerView.Adapter<DailyAdapter.DailyVh>() {
    private var dailyItem = ArrayList<Intake>()
    private var totalSum: Int = 0

    fun setData(listItem: List<Intake>) {
        dailyItem.clear()
        dailyItem.addAll(listItem)

        totalSum = dailyItem.sumBy { it.amount }

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
        return dailyItem.size
    }

    override fun onBindViewHolder(holder: DailyVh, position: Int) {
        holder.bind(dailyItem[position])
    }

    inner class DailyVh(val binding: ItemDailyIntakeListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Intake) {
            binding.intake = item
            binding.total = totalSum
            binding.dailyItemIv.setImageResource(DrinkMapper.drinkResources[item.category])
            binding.dailyItemName.text = binding.root.resources.getStringArray(DrinkMapper.drinkName)[item.category]
        }
    }
}