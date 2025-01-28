package com.example.myapplication.firebase

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityAuthBinding
import com.example.myapplication.logins.AppDatabase
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private val signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(Exception::class.java)!!
                firebaseAuthWithGoogle(account)
            } catch (e: Exception) {
                Log.e("AuthActivity", "Google sign in failed", e)
                Toast.makeText(this, "Fallo en la autenticación", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val username = intent.getStringExtra("username")
        if (!username.isNullOrEmpty()) {
            loadUserProfile(username)
        }
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.signInButton.setOnClickListener {
            signInWithGoogle()
        }
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        signInLauncher.launch(signInIntent)
    }


    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Autenticación exitosa", Toast.LENGTH_SHORT).show()
                    showForm()
                } else {
                    Toast.makeText(this, "Error de autenticación", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun showForm() {
        binding.tVUsername.text = "Usuario autenticado: ${auth.currentUser?.displayName}"
        binding.formLayout.visibility = android.view.View.VISIBLE

        binding.submitButton.setOnClickListener {
            val nia = binding.tfNIA.text.toString().trim()
            val nif = binding.tfNIF.text.toString().trim()
            val nombre = binding.tfNombre.text.toString().trim()
            val apellido = binding.tfApellido.text.toString().trim()
            val mediaExpediente = binding.tfMediaExpediente.text.toString().toDoubleOrNull()

            if (nia.isEmpty() || nif.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || mediaExpediente == null) {
                Toast.makeText(this, "Completa todos los campos correctamente", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Primero verificamos si el NIA existe
            db.collection("alumnosARR")
                .whereEqualTo("nia", nia)
                .get()
                .addOnSuccessListener { niaQuerySnapshot ->
                    if (niaQuerySnapshot.isEmpty) {
                        db.collection("alumnosARR")
                            .whereEqualTo("nif", nif)
                            .get()
                            .addOnSuccessListener { nifQuerySnapshot ->
                                if (nifQuerySnapshot.isEmpty) {
                                    insertAlumno(nia, nif, nombre, apellido, mediaExpediente)
                                } else {
                                    Toast.makeText(this, "Ya existe un alumno con este NIF", Toast.LENGTH_SHORT).show()
                                }
                            }
                            .addOnFailureListener { e ->
                                Log.e("AuthActivity", "Error al verificar NIF", e)
                                Toast.makeText(this, "Error al verificar NIF", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(this, "Ya existe un alumno con este NIA", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("AuthActivity", "Error al verificar NIA", e)
                    Toast.makeText(this, "Error al verificar NIA", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun insertAlumno(nia: String, nif: String, nombre: String, apellido: String, mediaExpediente: Double) {
        val alumno = hashMapOf(
            "nia" to nia,
            "nif" to nif,
            "nombre" to nombre,
            "apellido" to apellido,
            "mediaexpediente" to mediaExpediente
        )

        db.collection("alumnosARR").document(nia)
            .set(alumno)
            .addOnSuccessListener {
                Toast.makeText(this, "Datos guardados correctamente", Toast.LENGTH_SHORT).show()
                clearForm()
            }
            .addOnFailureListener { e ->
                Log.e("AuthActivity", "Error al guardar datos", e)
                Toast.makeText(this, "Error al guardar datos", Toast.LENGTH_SHORT).show()
            }
    }

    private fun clearForm() {
        binding.tfNIA.text.clear()
        binding.tfNIF.text.clear()
        binding.tfNombre.text.clear()
        binding.tfApellido.text.clear()
        binding.tfMediaExpediente.text.clear()
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