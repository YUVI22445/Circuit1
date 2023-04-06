package com.example.circuit.ui.home

import androidx.lifecycle.ViewModel
import com.example.circuit.CircuitApp

class HomeViewModel : ViewModel() {

    val items = CircuitApp.db.itemDao().getItems()
}