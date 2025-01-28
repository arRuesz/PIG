package com.example.myapplication.firebase

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityFirebaseBinding
import com.example.myapplication.logins.AppDatabase
import com.squareup.picasso.Picasso

class FirebaseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFirebaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirebaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra("username")
        if (!username.isNullOrEmpty()) {
            loadUserProfile(username)
        }
        binding.buttonAuth.setOnClickListener{
            val intent = Intent(this@FirebaseActivity, AuthActivity::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }
        binding.buttonNoAuth.setOnClickListener {
            val intent = Intent(this@FirebaseActivity, NonAuthActivity::class.java)
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
