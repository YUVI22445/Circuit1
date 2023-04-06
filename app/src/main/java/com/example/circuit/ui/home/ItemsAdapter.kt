package com.example.circuit.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.circuit.R
import com.example.circuit.data.db.Item
import com.example.circuit.databinding.LayoutListItemBinding

class ItemsAdapter : RecyclerView.Adapter<ItemsAdapter.ViewHolder>() {

    private var items: List<Item> = listOf()

    class ViewHolder(val binding: LayoutListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(LayoutListItemBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(items: List<Item>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.binding.title.text = item.name
        holder.binding.price.text = "Price:" + item.price

        val popupMenu = PopupMenu(holder.binding.root.context, holder.binding.moreBtn)
        popupMenu.inflate(R.menu.item_menu)

        holder.binding.moreBtn.setOnClickListener {
            popupMenu.show()
        }
    }
}