package com.example.circuit.ui.addedititem

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.circuit.CircuitApp
import com.example.circuit.R
import com.example.circuit.data.db.Item
import com.example.circuit.data.gson
import com.example.circuit.databinding.ActivityAddEditItemBinding
import kotlinx.coroutines.launch
import androidx.appcompat.app.AlertDialog
import android.graphics.Bitmap
import java.io.ByteArrayOutputStream

class AddEditItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditItemBinding
    private lateinit var progressDialog: ProgressDialog

    private var selectedImageUri: Uri? = null

    private var itemId: Int? = null

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
            val currentItem = gson.fromJson(item, Item::class.java)
            if(currentItem.bought) {
                Toast.makeText(this, "Item Already bought", Toast.LENGTH_SHORT).show()
                finish()
            }
            itemId = currentItem.id
            binding.nameField.setText(currentItem.name)
            binding.priceField.setText(currentItem.price)
            binding.descriptionField.setText(currentItem.description)
            currentItem.imageUrl?.let { imageUrl ->
                Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_image_placeholder)
                    .error(R.drawable.ic_broken_image)
                    .into(binding.imageView)
            }
        } else {
            setTitle(R.string.add_item)
        }
        binding.addImageButton.setOnClickListener {
            showImagePicker()
        }

        binding.save.setOnClickListener {
            val name = binding.nameField.text.toString()

            if (name.isBlank()) {
                Toast.makeText(this, R.string.blank_name, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val price = binding.priceField.text.toString()

            if (price.isBlank()) {
                Toast.makeText(this, R.string.blank_price, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val description = binding.descriptionField.text.toString()

            lifecycleScope.launch {
                progressDialog.show()
                val newItem = if (itemId != null) {
                    Item(name, price, description, selectedImageUri?.toString(), false, itemId)
                } else {
                    Item(name, price, description, selectedImageUri?.toString(), false)
                }

                if (itemId != null) {
                    CircuitApp.db.itemDao().update(newItem)
                } else {
                    CircuitApp.db.itemDao().insert(newItem)
                }

                progressDialog.hide()
                finish()

            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return false
    }

//    private fun showImagePicker() {
//        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//        if (intent.resolveActivity(packageManager) != null) {
//            startActivityForResult(intent, REQUEST_PICK_IMAGE)
//        } else {
//            Toast.makeText(this, R.string.no_image_picker_app_found, Toast.LENGTH_SHORT).show()
//        }
//    }
private fun showImagePicker() {
    val options = arrayOf<CharSequence>(
        getString(R.string.pick_image_from_gallery),
        getString(R.string.take_photo),
        getString(R.string.cancel)
    )
    val builder = AlertDialog.Builder(this)
    builder.setTitle(R.string.select_image)
    builder.setItems(options) { dialog, item ->
        when {
            options[item] == getString(R.string.pick_image_from_gallery) -> {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                if (intent.resolveActivity(packageManager) != null) {
                    startActivityForResult(intent, REQUEST_PICK_IMAGE)
                } else {
                    Toast.makeText(this, R.string.no_image_picker_app_found, Toast.LENGTH_SHORT).show()
                }
            }
            options[item] == getString(R.string.take_photo) -> {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (intent.resolveActivity(packageManager) != null) {
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
                } else {
                    Toast.makeText(this, R.string.no_camera_app_found, Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                dialog.dismiss()
            }
        }
    }
    builder.show()
}

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_PICK_IMAGE -> {
                    selectedImageUri = data?.data
                    selectedImageUri?.let { uri ->
                        Glide.with(this)
                            .load(uri)
                            .placeholder(R.drawable.ic_image_placeholder)
                            .error(R.drawable.ic_broken_image)
                            .into(binding.imageView)
                    }
                }
                REQUEST_IMAGE_CAPTURE -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap?
                    imageBitmap?.let { bitmap ->
                        val bytes = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.PNG, 90, bytes)
                        val path = MediaStore.Images.Media.insertImage(this.contentResolver, bitmap, "Title", null)
                        selectedImageUri = Uri.parse(path)
                        Glide.with(this)
                            .load(selectedImageUri)
                            .placeholder(R.drawable.ic_image_placeholder)
                            .error(R.drawable.ic_broken_image)
                            .into(binding.imageView)
                    }
                }
            }
        }
    }


    companion object {
        private const val ITEM = "item"
        private const val REQUEST_PICK_IMAGE = 1
        private const val REQUEST_IMAGE_CAPTURE = 2
        fun getIntent(context: Context, item: Item? = null): Intent {
            val intent = Intent(context, AddEditItemActivity::class.java)
            if (item != null) {
                intent.putExtra(ITEM, gson.toJson(item))
            }
            return intent
        }
    }
}
