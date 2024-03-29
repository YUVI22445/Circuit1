package com.example.circuit.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Item(
        val name: String,
        val price: String,
        val description: String?,
        val imageUrl: String?,
        var bought: Boolean,
        @PrimaryKey(autoGenerate = true) val id: Int? = null
)
