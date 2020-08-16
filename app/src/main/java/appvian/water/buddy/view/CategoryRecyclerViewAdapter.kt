package appvian.water.buddy.view


import android.content.Context
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import appvian.water.buddy.R
import appvian.water.buddy.model.data.Category
import kotlinx.android.synthetic.main.activity_favorite_drink_setting.view.*
import kotlinx.android.synthetic.main.modal_category_recyclerview_item.view.*
import java.util.*
import kotlin.collections.ArrayList

class CategoryRecyclerViewAdapter (val context : Context, val categoryList : ArrayList<Category>, val itemClick: (Category) -> Unit) :
    RecyclerView.Adapter<CategoryRecyclerViewAdapter.CategoryViewHolder>() {

    private var selectedPosition = -1
    private var selected: Array<Boolean> = Array(categoryList.size, { i ->
        if (i == 0) true
        else false
    })

    inner class CategoryViewHolder(itemView: View, itemClick: (Category) -> Unit) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(category: Category, position: Int, context: Context) {
            val isSelected = selected[position]
            if (isSelected) {
                itemView.img_check.visibility = View.VISIBLE
                itemView.categoryImg.setColorFilter(Color.parseColor("#33030303"))
            } else {
                itemView.img_check.visibility = View.INVISIBLE
                itemView.categoryImg.colorFilter = null
            }
            if (category.icon != "") {
                val resourceId =
                    context.resources.getIdentifier(category.icon, "drawable", context.packageName)
                itemView.categoryImg.setImageResource(resourceId)
            } else {
                itemView.categoryImg.setImageResource(R.mipmap.ic_launcher_round)
            }
            itemView.cname.text = category.name
            itemView.setOnClickListener {
                itemClick(category)
                selectedPosition = position
                setCategory(position)

            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.modal_category_recyclerview_item, parent, false)
        return CategoryViewHolder(view, itemClick)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categoryList[position], position, context)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    fun setCategory(position: Int){
        Arrays.fill(selected, false)
        selected[position] = true
        notifyDataSetChanged()
    }
}