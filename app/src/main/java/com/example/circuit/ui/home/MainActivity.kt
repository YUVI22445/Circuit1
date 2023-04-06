package com.example.circuit.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.circuit.R
import com.example.circuit.data.AuthManager
import com.example.circuit.databinding.ActivityMainBinding
import com.example.circuit.ui.addedititem.AddEditItemActivity
import com.example.circuit.ui.auth.AuthActivity

/**
 * Home Page of our App.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val vm: HomeViewModel by viewModels()
    private val adapter = ItemsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.itemsRv.adapter = adapter

        binding.addItem.setOnClickListener {
            startActivity(AddEditItemActivity.getIntent(this))
        }

        vm.items.observe(this) {
            binding.noItemsTv.isVisible = it.isEmpty()
            adapter.setItems(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.title == getString(R.string.logout)) {
            AuthManager.logout()
            navigateToAuth()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun navigateToAuth() {
        startActivity(Intent(this, AuthActivity::class.java))
        finish()
    }
}