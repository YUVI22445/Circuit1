package com.example.circuit.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.circuit.CircuitApp
import com.example.circuit.data.db.Item
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    val items = CircuitApp.db.itemDao().getItems()

    // Add a function to delete an item
    fun deleteItem(item: Item) {
        viewModelScope.launch {
            CircuitApp.db.itemDao().delete(item)
        }
    }
}
