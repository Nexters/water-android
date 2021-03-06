package appvian.water.buddy.view.analytics.month

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import appvian.water.buddy.R
import appvian.water.buddy.databinding.ItemMonthIntakeBinding
import appvian.water.buddy.model.data.Intake
import appvian.water.buddy.util.DrinkMapper

class RankAdapter : RecyclerView.Adapter<RankAdapter.RankViewHolder>() {
    private lateinit var binding: ItemMonthIntakeBinding
    private val rankList = ArrayList<Intake>()
    private val totalList = ArrayList<Intake>()

    inner class RankViewHolder(private val binding: ItemMonthIntakeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            val item = rankList[position]
            binding.pos = position + 1
            binding.intake = item
            binding.rankImg.setImageResource(DrinkMapper.drinkResources[item.category])
            binding.rankName.text =
                binding.root.resources.getTextArray(DrinkMapper.drinkName)[item.category]
        }
    }

    fun setData(rankData: List<Intake>) {
        totalList.clear()
        totalList.addAll(rankData)

        setSubData()
    }

    fun setSubData() {
        val subSize = if (totalList.size < 4) totalList.size else 3
        rankList.clear()
        rankList.addAll(totalList.subList(0, subSize))

        notifyDataSetChanged()
    }

    fun setAllData() {
        rankList.clear()
        rankList.addAll(totalList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_month_intake,
            parent,
            false
        )

        return RankViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return rankList.size
    }

    override fun onBindViewHolder(holder: RankViewHolder, position: Int) {
        holder.bind(position)
    }

}