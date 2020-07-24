package appvian.water.buddy.viewmodel


import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import appvian.water.buddy.R
import appvian.water.buddy.model.data.Category

class CategoryRecyclerViewAdapter (val context : Context, val categoryList : ArrayList<Category>, val itemClick: (Category) -> Unit) :
    RecyclerView.Adapter<CategoryRecyclerViewAdapter.CategoryViewHolder>(){

    private var selectedPosition = -1

    inner class CategoryViewHolder(itemView : View, itemClick: (Category) -> Unit) : RecyclerView.ViewHolder(itemView) {
        val cimg = itemView.findViewById<ImageView>(R.id.categoryImg)
        val ctext = itemView.findViewById<TextView>(R.id.cname)

        fun bind (category : Category, position: Int, context: Context){
            if(category.icon != ""){
                val resourceId = context.resources.getIdentifier(category.icon, "drawable", context.packageName)
                cimg.setImageResource(resourceId)
            } else{
                cimg.setImageResource(R.mipmap.ic_launcher_round)
            }
            ctext.text = category.name
            itemView.setOnClickListener {
                itemClick(category)
                selectedPosition=position
                notifyDataSetChanged()
            }
            if(selectedPosition==position){
                itemView.setBackgroundColor(Color.YELLOW)
            } else{
                itemView.setBackgroundColor(Color.WHITE)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.my_recyclerview_item,parent,false)
        return CategoryViewHolder(view,itemClick)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categoryList[position],position,context)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }
}