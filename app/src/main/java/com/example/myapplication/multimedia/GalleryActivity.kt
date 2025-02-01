package com.example.myapplication.multimedia

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityGalleryBinding
import java.io.File

class GalleryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGalleryBinding
    private lateinit var adapter: GalleryAdapter
    private lateinit var currentUser: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentUser = intent.getStringExtra("username") ?: "default_user"

        binding.recyclerViewGallery.layoutManager = GridLayoutManager(this, 1)

        val picturesPath = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            "CameraX-Image/$currentUser"
        )
        val videosPath = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES),
            "CameraX-Video/$currentUser"
        )

        // Cargar los archivos del usuario
        val mediaFiles = getFilesFromDirectory(picturesPath) + getFilesFromDirectory(videosPath)

        if (mediaFiles.isNotEmpty()) {
            adapter = GalleryAdapter(mediaFiles.toMutableList(), this)
            binding.recyclerViewGallery.adapter = adapter
        } else {
            Toast.makeText(this, "No hay archivos para el usuario: $currentUser", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getFilesFromDirectory(directory: File): List<File> {
        val files = mutableListOf<File>()

        if (directory.exists() && directory.isDirectory) {
            val fileList = directory.listFiles()
            if (fileList != null) {
                files.addAll(fileList)
            }
        } else {
            Log.d("GalleryActivity", "La carpeta no existe: ${directory.absolutePath}")
        }

        return files
    }
}