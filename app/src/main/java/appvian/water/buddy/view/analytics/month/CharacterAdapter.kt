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
    private var maxDay: Int = 31
    private var nonePositionList = arrayOf(0, 6, 28, 34)

    inner class CharacterVh(val binding: MonthCharacterListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        //0, 6 -> null
        //6
        //13
        //20
        //27
        //28 -> 1, 29 -> 2, 30 -> 4 31 -> 5

        fun bind(position: Int) {
            if (position in nonePositionList) {
                android.util.Log.d("adapter position", "position $position")
                binding.monthWabiView.visibility = View.INVISIBLE
            } else {
                val index = if(position in 1..27) {
                    position - 1
                } else {
                    if(maxDay == 31 || maxDay == 30)
                        position - 2
                    else
                        position - 3
                }

                characterList[index]?.let { setCharacter(it) }
            }
        }

        private fun setCharacter(item: Intake) {
            binding.monthWabiCharacter.setImageResource(CharacterMapper.characterIdList[item.category])
        }
    }

    fun setData(data: Map<Int, Intake>) {
        characterList.clear()
        characterList.putAll(data)

        notifyDataSetChanged()
    }

    fun setMaxDay(day: Int) {
        when (day) {
            28 -> {
                nonePositionList = arrayOf(0, 6, 28, 29, 32, 33, 34)
            }
            29 -> {
                nonePositionList = arrayOf(0, 6, 28, 29, 33, 34)
            }
            30 -> {
                nonePositionList = arrayOf(0, 6, 28, 29, 34)
            }
            31 -> {
                nonePositionList = arrayOf(0, 6, 28, 34)
            }
        }
    }

    fun getCharacterDataSize(): Int {
        return characterList.size
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
        return 35
    }

    override fun onBindViewHolder(holder: CharacterVh, position: Int) {
        holder.setIsRecyclable(false)
        holder.bind(position)
    }
}