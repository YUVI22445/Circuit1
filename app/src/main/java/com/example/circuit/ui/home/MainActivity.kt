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
import com.example.circuit.data.db.Item
import com.example.circuit.databinding.ActivityMainBinding
import com.example.circuit.ui.addedititem.AddEditItemActivity
import com.example.circuit.ui.auth.AuthActivity
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import android.net.Uri
import android.widget.Toast


/**
 * Home Page of our App.
 */
class MainActivity : AppCompatActivity(), ItemsAdapter.OnItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private val vm: HomeViewModel by viewModels()
    private val adapter = ItemsAdapter(this)

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

    // Implement OnItemClickListener methods
    override fun onEditClick(item: Item) {
        // Add the code to handle edit click
        startActivity(AddEditItemActivity.getIntent(this, item))
    }

    override fun onDeleteClick(item: Item) {
        // Add the code to handle delete click
        deleteItem(item)
    }

    override fun onShareClick(item: Item) {
        //code to handle share click
        val smsBody = """
        Can you buy this item for me:
        Name: ${item.name}
        Price: ${item.price}
        Description: ${item.description ?: "No description available"}
    """.trimIndent()

        val smsIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, smsBody)
        }

        if (smsIntent.resolveActivity(packageManager) != null) {
            startActivity(smsIntent)
        } else {
            Toast.makeText(this, "No SMS app found.", Toast.LENGTH_SHORT).show()
        }
    }
    private fun deleteItem(item: Item) {
        lifecycleScope.launch {
            vm.deleteItem(item)
        }
    }
}
