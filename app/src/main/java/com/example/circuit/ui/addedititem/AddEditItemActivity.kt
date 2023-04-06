package com.example.circuit.ui.addedititem

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.circuit.CircuitApp
import com.example.circuit.R
import com.example.circuit.data.db.Item
import com.example.circuit.data.gson
import com.example.circuit.databinding.ActivityAddEditItemBinding
import kotlinx.coroutines.launch

class AddEditItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditItemBinding
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Please wait")

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val item = intent.extras?.getString(ITEM, null)
        if (item != null) {
            setTitle(R.string.edit_item)
        } else {
            setTitle(R.string.add_item)
        }

        binding.save.setOnClickListener {
            val name = binding.nameField.text.toString()

            if (name.isBlank()) {
                Toast.makeText(this, "Name can not be left blank!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val price = binding.priceField.text.toString()

            if (price.isBlank()) {
                Toast.makeText(this, "Price can not be left blank!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                progressDialog.show()
                CircuitApp.db.itemDao().insert(Item(name, price))
                progressDialog.hide()
                finish()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return false
    }

    companion object {
        private const val ITEM = "item"
        fun getIntent(context: Context, item: Item? = null): Intent {
            val intent = Intent(context, AddEditItemActivity::class.java)
            if (item != null) {
                intent.putExtra(ITEM, gson.toJson(item))
            }
            return intent
        }
    }
}