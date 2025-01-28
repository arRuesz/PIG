package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.chuck.Chuckmemes
import com.example.myapplication.logins.AppDatabase
import com.example.myapplication.databinding.ActivityHubBinding
import com.example.myapplication.firebase.FirebaseActivity
import com.example.myapplication.juegopig.MainActivity
import com.squareup.picasso.Picasso

class Hub : AppCompatActivity() {
    private lateinit var binding: ActivityHubBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHubBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val fotoPig = binding.fotoPig
        fotoPig.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        selectPhoto()
        binding.imagenChuck.setOnClickListener { goToChuck() }
        binding.fotoFirebase.setOnClickListener {
            val username = intent.getStringExtra("name")
            val intent = Intent(this@Hub, FirebaseActivity::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }
    }

    private fun selectPhoto() {
        Thread {
            val userDao = AppDatabase.getInstance(this).userDao()
            val username = intent.getStringExtra("name")
            val picUrl = username?.let { userDao.getUrl(it) }
            runOnUiThread {
                if (!picUrl.isNullOrEmpty()) {
                    Picasso.get().load(picUrl).resize(100, 100).into(binding.imageViewProfile)
                } else {
                    binding.imageViewProfile.setImageResource(R.drawable.perfilsinfoto)
                }
                binding.tVUsername.text = username
            }
        }.start()
    }

    private fun goToChuck() {
        val intent = Intent(this@Hub, Chuckmemes::class.java)
        startActivity(intent)
    }
}