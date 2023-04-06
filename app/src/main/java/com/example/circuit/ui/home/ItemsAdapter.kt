package com.example.circuit.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.circuit.R
import com.example.circuit.data.db.Item
import com.example.circuit.databinding.LayoutListItemBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ItemsAdapter(private val listener: OnItemClickListener) :
    RecyclerView.Adapter<ItemsAdapter.ViewHolder>() {

    var items: List<Item> = listOf()

    interface OnItemClickListener {
        fun onEditClick(item: Item)
        fun onDeleteClick(item: Item)
        fun onShareClick(item: Item)
    }
    fun removeItem(position: Int) {
        val removedItem = items[position]
        items = items.toMutableList().apply { removeAt(position) }
        notifyItemRemoved(position)
        listener.onDeleteClick(removedItem)
    }

    class ViewHolder(val binding: LayoutListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(LayoutListItemBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateItems(items: List<Item>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.binding.title.text = item.name
        holder.binding.price.text = "Price:" + item.price
        holder.binding.description.text= item.description
        holder.binding.bought.isChecked= item.bought
        Glide.with(holder.itemView.context)
            .load(item.imageUrl)
            .placeholder(R.drawable.ic_image_placeholder)
            .error(R.drawable.ic_broken_image)
            .into(holder.binding.image)

        val popupMenu = PopupMenu(holder.binding.root.context, holder.binding.moreBtn)
        popupMenu.inflate(R.menu.item_menu)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.edit_item -> {
                    listener.onEditClick(item)
                    true
                }
                R.id.delete_item -> {
                    listener.onDeleteClick(item)
                    true
                }
                R.id.share_item -> {
                    listener.onShareClick(item)
                    true
                }
                else -> false
            }
        }

        holder.binding.bought.setOnCheckedChangeListener { _, isChecked ->
            item.bought = isChecked
        }
        holder.binding.moreBtn.setOnClickListener {
            popupMenu.show()
        }
    }
}
