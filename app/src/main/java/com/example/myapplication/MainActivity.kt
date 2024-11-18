package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var numJugadores: Int = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarSpinner()
    }

    private fun configurarSpinner() {
        val numJ = listOf("Seleccione el número de jugadores", 2, 3, 4)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, numJ)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position == 0) {
                    return
                } else {
                    numJugadores = numJ[position] as Int

                    // Obtiene el número de rondas
                    val numRondas = binding.numJug.text.toString().toIntOrNull()

                    if (numRondas == null) {
                        Toast.makeText(this@MainActivity, "Introduce un número válido de rondas", Toast.LENGTH_SHORT).show()
                    } else {
                        // Inicia la actividad si hay una selección válida
                        val intent = Intent(this@MainActivity, PlayersActivity::class.java)
                        intent.putExtra("NUM_RONDAS", numRondas)
                        intent.putExtra("NUM_JUGADORES", numJugadores)
                        startActivity(intent)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No hacer nada
            }
        }
    }


}


