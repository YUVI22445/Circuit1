package com.example.circuit.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Item::class], version = 4)
abstract class CircuitDb : RoomDatabase() {
    abstract fun itemDao(): ItemsDao
}