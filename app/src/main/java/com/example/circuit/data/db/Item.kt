package com.example.circuit.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Item(
    val name: String,
    val price: String,
    val imageUri: String? = null,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)
