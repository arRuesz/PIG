package com.example.myapplication.firebase

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityNonAuthBinding
import com.example.myapplication.logins.AppDatabase
import com.squareup.picasso.Picasso

class NonAuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNonAuthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNonAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val username = intent.getStringExtra("username")
        if (!username.isNullOrEmpty()) {
            loadUserProfile(username)
        }
        binding.button.setOnClickListener {
            val intent = Intent(this@NonAuthActivity,ListaAlumnosActivity::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }
        binding.button2.setOnClickListener {
            val intent = Intent(this@NonAuthActivity,BuscarAlumnoActivity::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }
    }
    private fun loadUserProfile(username: String) {
        Thread {
            val userDao = AppDatabase.getInstance(this).userDao()
            val picUrl = userDao.getUrl(username)
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
}