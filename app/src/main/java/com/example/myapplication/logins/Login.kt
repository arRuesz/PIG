package com.example.myapplication.logins

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.myapplication.Hub
import com.example.myapplication.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    val Context.dataStore by preferencesDataStore(name = "user_preferences")

    private val USERNAME_KEY = stringPreferencesKey("username")
    private val PASSWORD_KEY = stringPreferencesKey("password")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val regist = binding.botonRegister
        regist.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

        cargarCredencialesGuardadas()

        binding.botonLogin.setOnClickListener {
            val username = binding.userName.text.toString()
            val password = binding.password.text.toString()
            validarCredenciales(username, password)
        }

        // Listener para desmarcar el checkbox si el usuario cambia el nombre o la contraseña
        binding.userName.setOnFocusChangeListener { _, _ ->
            binding.recordar.isChecked = false
        }
        binding.password.setOnFocusChangeListener { _, _ ->
            binding.recordar.isChecked = false
        }
    }

    private fun cargarCredencialesGuardadas() {
        CoroutineScope(Dispatchers.Main).launch {
            val preferences = applicationContext.dataStore.data.first()
            val savedUsername = preferences[USERNAME_KEY] ?: ""
            val savedPassword = preferences[PASSWORD_KEY] ?: ""

            if (savedUsername.isNotEmpty() && savedPassword.isNotEmpty()) {
                binding.userName.setText(savedUsername)
                binding.password.setText(savedPassword)
                binding.recordar.isChecked = true
            }
        }
    }

    private fun guardarCredenciales(username: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val preferences = applicationContext.dataStore.edit { prefs ->
                prefs[USERNAME_KEY] = username
                prefs[PASSWORD_KEY] = password
            }
        }
    }

    private fun validarCredenciales(username: String, password: String) {
        if (username.isEmpty() || password.isEmpty()) {
            Snackbar.make(binding.root, "Por favor ingresa tus credenciales", Snackbar.LENGTH_SHORT).show()
            return
        }


        Thread {
            val userDao = AppDatabase.getInstance(this).userDao()
            val usuario = userDao.getUsuarioPorNombre(username)


            runOnUiThread {
                if (usuario != null && usuario.password == password) {

                    Snackbar.make(binding.root, "Bienvenido, $username", Snackbar.LENGTH_SHORT).show()


                    if (binding.recordar.isChecked) {
                        guardarCredenciales(username, password)
                    }

                    val intent = Intent(this, Hub::class.java)
                    intent.putExtra("name", username)
                    startActivity(intent)
                    finish()
                } else {
                    binding.errorLogin.visibility = android.view.View.VISIBLE
                }
            }
        }.start()
    }
}
