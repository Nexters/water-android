package appvian.water.buddy.view.analytics.month

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import appvian.water.buddy.R
import appvian.water.buddy.databinding.MonthCharacterListBinding
import appvian.water.buddy.model.data.Intake
import appvian.water.buddy.util.CharacterMapper

class CharacterAdapter : RecyclerView.Adapter<CharacterAdapter.CharacterVh>() {
    private val characterList = HashMap<Int, Intake>()
    var maxDay: Int = 31

    inner class CharacterVh(val binding: MonthCharacterListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            characterList[position]?.let {
                setCharacter(it)
            }
        }

        private fun setCharacter(item: Intake) {
            android.util.Log.d("character", "data $item")
            binding.monthWabiCharacter.setImageResource(CharacterMapper.characterIdList[item.category])
        }
    }

    fun setData(data: Map<Int, Intake>) {
        android.util.Log.d("character", "data $data")
        characterList.clear()
        characterList.putAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterVh {
        val binding = DataBindingUtil.inflate<MonthCharacterListBinding>(
            LayoutInflater.from(parent.context),
            R.layout.month_character_list,
            parent,
            false
        )
        return CharacterVh(binding)
    }

    override fun getItemCount(): Int {
        return maxDay
    }

    override fun onBindViewHolder(holder: CharacterVh, position: Int) {
        holder.setIsRecyclable(false)
        holder.bind(position)
    }
}