package com.example.myapplication.juegopig

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityScoresBinding

class ActivityScores : AppCompatActivity() {

    private lateinit var binding: ActivityScoresBinding

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScoresBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener datos del Intent
        val gameData: GameData? = intent.getParcelableExtra("data", GameData::class.java)

        // Mostrar el ganador
        binding.textViewGanador.text = "Ganador: ${gameData?.winner}"

        // Mostrar las puntuaciones de los jugadores
        val scoresDisplay = StringBuilder()
        for (i in gameData?.playerNames?.indices!!) {
            scoresDisplay.append("${gameData.playerNames[i]}: ${gameData.playerScores[i]}\n")
        }
        binding.textViewScores.text = scoresDisplay.toString()
    }
    fun reiniciarJuego(view: android.view.View) {
        val intent = Intent(this, MainActivity::class.java) // Cambia MainActivity por la actividad que desees
        startActivity(intent)
        finish()
    }
}
