package ru.fefu.activitytracker.adapters
import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.new_activity_item.view.*
import ru.fefu.activitytracker.dataclasses.NewActivityData
import ru.fefu.activitytracker.R
class NewActivityTypeListAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var selected = -1
    private var activities = listOf<NewActivityData>(
        NewActivityData("Велосипед", false),
        NewActivityData("Бег", false ),
        NewActivityData("Шаг", false ))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.new_activity_item, parent, false)
        return NewActivityViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as NewActivityViewHolder).bind(activities[position])
    }

    override fun getItemCount(): Int = 3

    inner class NewActivityViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val typeOfActivity = itemView.findViewById<TextView>(R.id.activity_type)
        fun bind(activity: NewActivityData) {
            typeOfActivity.text = activity.type
            itemView.isSelected = activity.isSelected
            if (itemView.isSelected) {
                itemView.innerCL.setBackgroundResource(R.drawable.border_selected)
            } else {
                itemView.innerCL.setBackgroundResource(R.drawable.border)
            }
            itemView.setOnClickListener {
                activities[adapterPosition].isSelected = true
                if (selected != -1 && selected != adapterPosition) {
                    activities[selected].isSelected = false
                }
                selected = adapterPosition
                notifyDataSetChanged()
            }
        }
    }
}