package com.sova.fleet.manager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class VehicleCardAdapter(
    private val onOpen: (String) -> Unit,
    private val onEdit: (String) -> Unit,
    private val onDelete: (String) -> Unit,
) : RecyclerView.Adapter<VehicleCardAdapter.VehicleCardViewHolder>() {
    private val items = mutableListOf<Vehicle>()

    fun submit(list: List<Vehicle>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleCardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_vehicle_card, parent, false)
        return VehicleCardViewHolder(view, onOpen, onEdit, onDelete)
    }

    override fun onBindViewHolder(holder: VehicleCardViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class VehicleCardViewHolder(
        itemView: View,
        private val onOpen: (String) -> Unit,
        private val onEdit: (String) -> Unit,
        private val onDelete: (String) -> Unit,
    ) : RecyclerView.ViewHolder(itemView) {
        private val title = itemView.findViewById<TextView>(R.id.vehicleTitle)
        private val meta = itemView.findViewById<TextView>(R.id.vehicleMeta)
        private val status = itemView.findViewById<TextView>(R.id.vehicleStatus)
        private val fleet = itemView.findViewById<TextView>(R.id.vehicleFleet)
        private val editButton = itemView.findViewById<ImageButton>(R.id.btnEdit)
        private val deleteButton = itemView.findViewById<TextView>(R.id.btnDelete)

        fun bind(item: Vehicle) {
            title.text = item.model
            meta.text = "VIN ${item.vin}"
            status.text = "Статус: ${item.status}"
            fleet.text = "Парк: ${ManagerRepository.fleetName(item.fleetId)}"

            itemView.setOnClickListener { onOpen(item.id) }
            editButton.setOnClickListener { onEdit(item.id) }
            deleteButton.setOnClickListener { onDelete(item.id) }
        }
    }
}
