package com.example.circuit

import android.app.Application
import androidx.room.Room
import com.example.circuit.data.db.CircuitDb

class CircuitApp : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        db = Room.databaseBuilder(this, CircuitDb::class.java, "circuit.sqlite").build()
    }

    companion object {
        lateinit var instance: CircuitApp
        lateinit var db: CircuitDb
    }
}