package com.example.myapplication.logins


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.Hub
import com.example.myapplication.databinding.ActivityRegisterBinding
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class Register : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private var selectedImageUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val campoFecha = binding.fecha
        campoFecha.setOnClickListener { showDatePickerDialog() }
        binding.botonLogin.setOnClickListener { validaryGuardar() }
        binding.opFoto.setOnClickListener { chooseIcon() }
        selectedImageUrl = intent.getStringExtra("IMAGE_URL")
        if (!selectedImageUrl.isNullOrEmpty()) {
            Picasso.get().load(selectedImageUrl).resize(400, 400).into(binding.opFoto)
        }
    }

    private fun chooseIcon() {
        val intent = Intent(this@Register, ActivityFotos::class.java)
        startActivity(intent)
    }

    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment { day, month, year -> onDateSelected(day, month, year) }
        datePicker.show(supportFragmentManager, "datePicker")
    }

    private fun onDateSelected(day: Int, month: Int, year: Int) {
        val campoFecha = binding.fecha
        campoFecha.setText("$day/${month + 1}/$year")
    }

    private fun validaryGuardar() {
        val nom = binding.userName.text.toString()
        val pw = binding.password.text.toString()
        val rPW = binding.repeatPassword.text.toString()
        val fecha = binding.fecha.text.toString()

        // Validaciones
        if (!validarNombre(nom)) {
            binding.errorName.visibility = android.view.View.VISIBLE
            return
        } else binding.errorName.visibility = android.view.View.GONE

        if (!validarPassword(pw)) {
            binding.errorPw.visibility = android.view.View.VISIBLE
            return
        } else binding.errorPw.visibility = android.view.View.GONE

        if (pw != rPW) {
            binding.errorPw2.visibility = android.view.View.VISIBLE
            return
        } else binding.errorPw2.visibility = android.view.View.GONE

        if (!validarEdad(fecha)) {
            binding.errorDate.visibility = android.view.View.VISIBLE
            return
        } else binding.errorDate.visibility = android.view.View.GONE

        if (!binding.condiciones.isChecked) {
            binding.errorCond.visibility = android.view.View.VISIBLE
            return
        } else binding.errorCond.visibility = android.view.View.GONE

        guardarEnBD(nom, pw, selectedImageUrl)
    }

    private fun validarNombre(name: String): Boolean {
        return name.length in 4..10
    }

    private fun validarPassword(password: String): Boolean {
        val tieneNum = password.any { it.isDigit() }
        return password.length in 4..10 && tieneNum
    }

    private fun validarEdad(fechaNac: String): Boolean {
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val fecha = format.parse(fechaNac) ?: return false
        val calendario = Calendar.getInstance()
        calendario.add(Calendar.YEAR, -16)
        return !fecha.after(calendario.time)
    }

    private fun guardarEnBD(name: String, password: String, imageUrl: String?) {
        val user = User(name = name, password = password, imageUrl = imageUrl)

        CoroutineScope(Dispatchers.Main).launch {
            val usuarioExistente = withContext(Dispatchers.IO) {
                val userDao = AppDatabase.getInstance(this@Register).userDao()
                userDao.getUsuarioPorNombre(name)
            }

            if (usuarioExistente != null) {
                Snackbar.make(
                    binding.root,
                    "El nombre de usuario ya está registrado",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                withContext(Dispatchers.IO) {
                    val userDao = AppDatabase.getInstance(this@Register).userDao()
                    userDao.insertarUsuario(user)
                }
                Snackbar.make(
                    binding.root,
                    "Usuario creado exitosamente",
                    Snackbar.LENGTH_SHORT
                ).show()
                val intent = Intent(this@Register, Hub::class.java)
                intent.putExtra("name", user.name)
                startActivity(intent)
                finish()
            }
        }
    }
}