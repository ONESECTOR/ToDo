package com.sector.todo.fragments.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.sector.todo.R
import com.sector.todo.data.models.Priority
import com.sector.todo.data.models.ToDoData
import kotlinx.android.synthetic.main.row_layout.view.*

class ListAdapter: RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    var dataList = emptyList<ToDoData>()

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_layout, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        with(holder) {
            itemView.tvTitle.text = dataList[position].title
            itemView.tvDescription.text = dataList[position].description

            itemView.rowBackground.setOnClickListener {
                val action = ListFragmentDirections.actionListFragmentToUpdateFragment(dataList[position])
                itemView.findNavController().navigate(action)
            }

            when(dataList[position].priority) {
                Priority.HIGH -> itemView.priorityIndicator.setCardBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.red
                    )
                )
                Priority.MEDIUM -> itemView.priorityIndicator.setCardBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.yellow
                    )
                )
                Priority.LOW -> itemView.priorityIndicator.setCardBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.green
                    )
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setData(toDoData: List<ToDoData>) {
        this.dataList = toDoData
        notifyDataSetChanged()
    }
}