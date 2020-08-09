package appvian.water.buddy.view.analytics.chart.daily

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import appvian.water.buddy.R
import appvian.water.buddy.databinding.ItemDailyLegendBinding
import appvian.water.buddy.util.DrinkMapper

class DailyLegendAdapter : RecyclerView.Adapter<DailyLegendAdapter.LegendVh>() {
    private val legends = ArrayList<Int>()

    inner class LegendVh(val binding: ItemDailyLegendBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindLegends(item: Int) {
            binding.dailyLegendImg.setBackgroundColor(binding.root.context.getColor(DrinkMapper.drinkColor[item]))
            binding.dailyLegendTxt.text = binding.root.resources.getStringArray(DrinkMapper.drinkName)[item]
        }

    }

    fun addData(item: Int){
        legends.add(item)
        notifyDataSetChanged()
    }

    fun clearData(){
        legends.clear()
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
        holder.bindLegends(legends[position])
    }
}