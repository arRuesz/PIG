package com.example.myapplication.juegopig

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityMainPigBinding

class MainPig : AppCompatActivity() {
    private lateinit var binding: ActivityMainPigBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainPigBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}