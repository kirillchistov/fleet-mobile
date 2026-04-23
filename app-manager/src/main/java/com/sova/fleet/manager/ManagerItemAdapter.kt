package com.sova.fleet.manager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ManagerItemAdapter(
    private val onOpen: (String) -> Unit,
    private val onEdit: (String) -> Unit,
    private val onDelete: (String) -> Unit,
) : RecyclerView.Adapter<ManagerItemAdapter.ManagerItemViewHolder>() {

    private val items = mutableListOf<ManagerListItem>()

    fun submit(list: List<ManagerListItem>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManagerItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_manager_entity, parent, false)
        return ManagerItemViewHolder(view, onOpen, onEdit, onDelete)
    }

    override fun onBindViewHolder(holder: ManagerItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class ManagerItemViewHolder(
        itemView: View,
        private val onOpen: (String) -> Unit,
        private val onEdit: (String) -> Unit,
        private val onDelete: (String) -> Unit,
    ) : RecyclerView.ViewHolder(itemView) {
        private val title = itemView.findViewById<TextView>(R.id.itemTitle)
        private val subtitle = itemView.findViewById<TextView>(R.id.itemSubtitle)
        private val editButton = itemView.findViewById<ImageButton>(R.id.btnEdit)
        private val deleteButton = itemView.findViewById<ImageButton>(R.id.btnDelete)

        fun bind(item: ManagerListItem) {
            title.text = item.title
            subtitle.text = item.subtitle
            itemView.setOnClickListener { onOpen(item.id) }
            editButton.setOnClickListener { onEdit(item.id) }
            deleteButton.setOnClickListener { onDelete(item.id) }
        }
    }
}
