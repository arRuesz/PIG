package com.example.myapplication.firebase

import AlumnoAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityListaAlumnosBinding
import com.example.myapplication.logins.AppDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.squareup.picasso.Picasso

class ListaAlumnosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListaAlumnosBinding
    private lateinit var adapter: AlumnoAdapter
    private val alumnosList = mutableListOf<Alumno>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaAlumnosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val username = intent.getStringExtra("username")
        if (!username.isNullOrEmpty()) {
            loadUserProfile(username)
        }
        adapter = AlumnoAdapter(alumnosList)
        binding.recyclerViewAlumnos.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewAlumnos.adapter = adapter

        cargarAlumnos()

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
    private fun cargarAlumnos() {
        db.collection("alumnosARR")
            .get()
            .addOnSuccessListener { documents ->
                alumnosList.clear()
                for (document in documents) {
                    val alumno = document.toObject<Alumno>()
                    alumnosList.add(alumno)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
            }
    }



}