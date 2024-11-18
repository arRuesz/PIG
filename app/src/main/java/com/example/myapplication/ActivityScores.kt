package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityScoresBinding

class ActivityScores : AppCompatActivity() {

    private lateinit var binding: ActivityScoresBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScoresBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener datos del Intent
        val playerNames = intent.getStringArrayListExtra("playerNames") ?: arrayListOf()
        val playerScores = intent.getIntegerArrayListExtra("playerScores") ?: arrayListOf()
        val winnerName = intent.getStringExtra("winner") ?: "Nadie"

        // Mostrar el ganador
        binding.textViewGanador.text = "Ganador: $winnerName"

        // Mostrar las puntuaciones de los jugadores
        val scoresDisplay = StringBuilder()
        for (i in playerNames.indices) {
            scoresDisplay.append("${playerNames[i]}: ${playerScores[i]}\n")
        }
        binding.textViewScores.text = scoresDisplay.toString()
    }
    fun reiniciarJuego(view: android.view.View) {
        val intent = Intent(this, MainActivity::class.java) // Cambia MainActivity por la actividad que desees
        startActivity(intent) // Inicia la actividad
        finish() // Finaliza la actividad actual
    }
}
