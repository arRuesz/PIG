package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ActivityJugadoresBinding

class PlayersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJugadoresBinding
    private val playerNames = listOf(
        "Aitor Tilla", "Ana Conda", "Armando Broncas", "Aurora Boreal",
        "Bartolo Mesa", "Carmen Mente", "Dolores Delirio", "Elsa Pato",
        "Enrique Cido", "Esteban Dido", "Elba Lazo", "Fermin Tado",
        "Lola Mento", "Luz Cuesta", "Margarita Flores", "Paco Tilla",
        "Pere Gil", "Pío Nono", "Salvador Tumbado", "Zoila Vaca"
    )

    private lateinit var jugSeleccionados: MutableList<String?>
    private var indiceActual = 0
    private var numRondas: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJugadoresBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val numJugadores = intent.getIntExtra("NUM_JUGADORES", 2)
        numRondas = intent.getIntExtra("NUM_RONDAS", 1)

        jugSeleccionados = MutableList(numJugadores) { null }

        setupRecyclerView()
        esconderRecyclerExtras(numJugadores)
    }

    private fun setupRecyclerView() {
        binding.recyclerView1.layoutManager = LinearLayoutManager(this)
        val adapter = PlayerAdapter(playerNames) { playerName ->
            onPlayerSelected(playerName)
        }
        binding.recyclerView1.adapter = adapter
    }

    private fun onPlayerSelected(playerName: String) {
        jugSeleccionados[indiceActual] = playerName

        if (indiceActual < jugSeleccionados.size - 1) {
            indiceActual++
            mostrarSiguienteRecyclerView()
        } else {
            // Verificamos que todos los jugadores estén seleccionados
            if (jugSeleccionados.all { it != null }) {
                // Iniciar la siguiente actividad solo si todos los nombres están seleccionados
                val intent = Intent(this, GameActivity::class.java).apply {
                    putStringArrayListExtra("selectedPlayers", ArrayList(jugSeleccionados.filterNotNull()))
                    putExtra("NUM_RONDAS", numRondas)
                }
                startActivity(intent)
            }
        }
    }

    private fun mostrarSiguienteRecyclerView() {
        when (indiceActual) {
            1 -> {
                binding.recyclerView1.visibility = RecyclerView.GONE
                binding.recyclerView2.visibility = RecyclerView.VISIBLE
                crearSiguienteRecycler(binding.recyclerView2)
            }
            2 -> {
                binding.recyclerView2.visibility = RecyclerView.GONE
                binding.recyclerView3.visibility = RecyclerView.VISIBLE
                crearSiguienteRecycler(binding.recyclerView3)
            }
            3 -> {
                binding.recyclerView3.visibility = RecyclerView.GONE
                binding.recyclerView4.visibility = RecyclerView.VISIBLE
                crearSiguienteRecycler(binding.recyclerView4)
            }
        }
    }

    private fun esconderRecyclerExtras(numJugadores: Int) {
        when (numJugadores) {
            2 -> {
                binding.recyclerView3.visibility = RecyclerView.GONE
                binding.recyclerView4.visibility = RecyclerView.GONE
            }
            3 -> {
                binding.recyclerView4.visibility = RecyclerView.GONE
            }
        }
    }

    private fun crearSiguienteRecycler(recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = PlayerAdapter(playerNames) { playerName ->
            onPlayerSelected(playerName)
        }
        recyclerView.adapter = adapter
    }
}
