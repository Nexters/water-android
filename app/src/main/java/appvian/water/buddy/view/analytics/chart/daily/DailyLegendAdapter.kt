package appvian.water.buddy.view.analytics.chart.daily

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import appvian.water.buddy.R
import appvian.water.buddy.databinding.ItemDailyLegendBinding
import appvian.water.buddy.util.DrinkMapper

class DailyLegendAdapter : RecyclerView.Adapter<DailyLegendAdapter.LegendVh>() {
    private val legends = ArrayList<String>()

    inner class LegendVh(val binding: ItemDailyLegendBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindLegends(position: Int, item: String) {
            binding.dailyLegendImg.setBackgroundColor(binding.root.context.getColor(DrinkMapper.drinkColor[position]))
            binding.dailyLegendTxt.text = item
        }
    }

    fun addData(items: Array<String>){
        legends.clear()
        legends.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LegendVh {
        val binding = DataBindingUtil.inflate<ItemDailyLegendBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_daily_legend,
            parent,
            false
        )

        return LegendVh(binding)
    }

    override fun getItemCount(): Int {
        return legends.size
    }

    override fun onBindViewHolder(holder: LegendVh, position: Int) {
        holder.bindLegends(position, legends[position])
    }
}