package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityGameBinding
import kotlin.random.Random

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding
    private var numRondas: Int = 1
    private var rondaActual: Int = 1
    private var indiceJugActual = 0
    private var puntuacionTurno = 0
    private lateinit var ptsJugadores: MutableList<Int>
    private lateinit var nombres: List<String>

    // Array de drawables para los dados
    private val carasDado = arrayOf(
        R.drawable.d1,
        R.drawable.d2,
        R.drawable.d3,
        R.drawable.d4,
        R.drawable.d5,
        R.drawable.d6
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener datos del Intent
        numRondas = intent.getIntExtra("NUM_RONDAS", 1)
        nombres = intent.getStringArrayListExtra("selectedPlayers") ?: listOf()

        // Asignar orden aleatorio a los jugadores
        nombres = nombres.shuffled()
        ptsJugadores = MutableList(nombres.size) { 0 }

        // Configurar puntuaciones iniciales y ronda
        actualizarPts()
        actuRonda()

        // Listeners para botones
        binding.botonTirar.setOnClickListener { tirarDado() }
        binding.botonPasar.setOnClickListener { pasarTurno() }
    }

    private fun tirarDado() {
        val roll = Random.nextInt(1, 7) // Genera un número entre 1 y 6
        binding.imageView.setImageResource(carasDado[roll - 1]) // Accede directamente al drawable correspondiente

        if (roll == 1) {
            // Perder puntos y pasar al siguiente jugador
            puntuacionTurno = 0
            Toast.makeText(this, "¡Sacaste un 1! Pierdes los puntos de este turno.", Toast.LENGTH_SHORT).show()
            pasarTurno()
        } else {
            // Acumular puntos
            puntuacionTurno += roll
            Toast.makeText(this, "Sacaste un $roll. Puntuación actual: $puntuacionTurno", Toast.LENGTH_SHORT).show()
        }
    }

    private fun pasarTurno() {
        // Agregar puntos del turno al jugador actual y restablecer la puntuación del turno
        ptsJugadores[indiceJugActual] += puntuacionTurno
        puntuacionTurno = 0
        actualizarPts()

        // Cambiar al siguiente jugador
        indiceJugActual = (indiceJugActual + 1) % nombres.size

        // Si volvemos al primer jugador, avanzamos la ronda
        if (indiceJugActual == 0) {
            rondaActual++
            actuRonda()
        }

        // Verificar si se ha alcanzado el límite de rondas
        if (rondaActual > numRondas) {
            terminarPartida()
        }
    }

    private fun actualizarPts() {
        binding.player1Score.text = "${nombres[0]}: ${ptsJugadores[0]}"
        binding.player2Score.text = "${nombres[1]}: ${ptsJugadores[1]}"
        if (ptsJugadores.size > 2) {
            binding.player3Score.apply {
                text = "${nombres[2]}: ${ptsJugadores[2]}"
                visibility = android.view.View.VISIBLE
            }
        }
        if (ptsJugadores.size > 3) {
            binding.player4Score.apply {
                text = "${nombres[3]}: ${ptsJugadores[3]}"
                visibility = android.view.View.VISIBLE
            }
        }
    }

    private fun actuRonda() {
        binding.textRondaActual.text = "Ronda: $rondaActual"
    }

    private fun terminarPartida() {
        // Calcular el ganador
        val puntMax = ptsJugadores.maxOrNull() ?: 0
        val indiceGanador = ptsJugadores.indexOf(puntMax)
        val nombreGanador = nombres[indiceGanador]

        // Navegar a la pantalla final
        val intent = Intent(this, ActivityScores::class.java).apply {
            putStringArrayListExtra("playerNames", ArrayList(nombres))
            putIntegerArrayListExtra("playerScores", ArrayList(ptsJugadores))
            putExtra("winner", nombreGanador)
        }
        startActivity(intent)
        finish()
    }
}
