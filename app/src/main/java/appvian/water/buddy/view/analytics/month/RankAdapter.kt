package appvian.water.buddy.view.analytics.month

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import appvian.water.buddy.R
import appvian.water.buddy.databinding.ItemMonthIntakeBinding
import appvian.water.buddy.model.data.CalendarData

class RankAdapter : RecyclerView.Adapter<RankAdapter.RankViewHolder>() {
    private lateinit var binding: ItemMonthIntakeBinding
    private val rankList = IntArray(10){ i -> i + 1}

    inner class RankViewHolder(private val binding: ItemMonthIntakeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Int) {
            binding.monthRankNum.text = item.toString()
        }
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
        holder.bind(rankList[position])
    }

}