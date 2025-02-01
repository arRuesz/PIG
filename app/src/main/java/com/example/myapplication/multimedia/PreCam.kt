package com.example.myapplication.multimedia

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityPreCamBinding

class PreCam : AppCompatActivity() {
    private lateinit var binding: ActivityPreCamBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPreCamBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnOpenCamera.setOnClickListener { openCamera() }
        binding.btnShowGallery.setOnClickListener { showGallery() }
    }

    private fun openCamera() {
        val currentUser = intent.getStringExtra("username") ?: "default_user"
        val intent = Intent(this, CameraActivity::class.java)
        intent.putExtra(
            "username",
            currentUser
        )
        startActivity(intent)
    }

    private fun showGallery() {
        val currentUser = intent.getStringExtra("username") ?: "default_user"
        val intent = Intent(this, GalleryActivity::class.java)
        intent.putExtra(
            "username",
            currentUser
        )
        startActivity(intent)
    }
}