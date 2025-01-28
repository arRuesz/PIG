package com.example.myapplication.firebase

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityBuscarAlumnoBinding
import com.example.myapplication.logins.AppDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class BuscarAlumnoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBuscarAlumnoBinding
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuscarAlumnoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val username = intent.getStringExtra("username")
        if (!username.isNullOrEmpty()) {
            loadUserProfile(username)
        }
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == binding.rbNIF.id) {
                mostrarNIF()
            } else if (checkedId == binding.rbNIA.id) {
                mostrarNIA()
            }
        }
        binding.bBuscarNIF.setOnClickListener {
            val nif = binding.tfNIF.text.toString().trim()
            if (nif.isNotEmpty()) {
                buscarAlumnoPorNIF(nif)
            } else {
                Toast.makeText(this, "Introduce un NIF válido", Toast.LENGTH_SHORT).show()
            }
        }
        binding.bBuscarNIA.setOnClickListener {
            val nia = binding.tfNIA.text.toString().trim()
            if (nia.isNotEmpty()) {
                buscarAlumnoPorNIA(nia)
            } else {
                Toast.makeText(this, "Introduce un NIA válido", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun mostrarNIF() {
        binding.tfNIF.visibility = View.VISIBLE
        binding.bBuscarNIF.visibility = View.VISIBLE
        binding.tfNIA.visibility = View.GONE
        binding.bBuscarNIA.visibility = View.GONE
    }

    private fun mostrarNIA() {
        binding.tfNIF.visibility = View.GONE
        binding.bBuscarNIF.visibility = View.GONE
        binding.tfNIA.visibility = View.VISIBLE
        binding.bBuscarNIA.visibility = View.VISIBLE
    }

    private fun buscarAlumnoPorNIF(nif: String) {
        db.collection("alumnosARR")
            .whereEqualTo("nif", nif)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val alumno = documents.first().toObject(Alumno::class.java)
                    mostrarAlumno(alumno)
                } else {
                    binding.tvResult.text = "No se encontró ningún alumno con ese NIF."
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al buscar el alumno: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun buscarAlumnoPorNIA(nia: String) {
        db.collection("alumnosARR")
            .whereEqualTo("nia", nia)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val alumno = documents.first().toObject(Alumno::class.java)
                    mostrarAlumno(alumno)
                } else {
                    binding.tvResult.text = "No se encontró ningún alumno con ese NIA."
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al buscar el alumno: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun mostrarAlumno(alumno: Alumno) {
        val resultado = """
            Nombre: ${alumno.nombre}
            Apellido: ${alumno.apellido}
            Media Expediente: ${alumno.mediaexpediente}
        """.trimIndent()
        binding.tvResult.text = resultado
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