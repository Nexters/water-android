package appvian.water.buddy.view.analytics.month

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import appvian.water.buddy.R
import appvian.water.buddy.model.data.Intake

class CharacterAdapter : RecyclerView.Adapter<CharacterAdapter.CharacterVh>() {
    val characterList = ArrayList<Intake>()

    inner class CharacterVh(itemView:View): RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterVh {
        return CharacterVh(LayoutInflater.from(parent.context).inflate(R.layout.month_character_list, parent, false))
    }

    override fun getItemCount(): Int {
        return characterList.size
    }

    override fun onBindViewHolder(holder: CharacterVh, position: Int) {
    }
}