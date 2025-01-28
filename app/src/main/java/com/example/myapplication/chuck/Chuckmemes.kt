package com.example.myapplication.chuck

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityChuckmemesBinding
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class Chuckmemes : AppCompatActivity() {
    private lateinit var binding: ActivityChuckmemesBinding
    private val apiService: ChuckApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.chucknorris.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ChuckApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChuckmemesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val categories = listOf(
            "animal", "career", "celebrity", "dev", "explicit", "fashion", "food",
            "history", "money", "movie", "music", "political", "religion", "science",
            "sport", "travel"
        )

        val adapter = Adapter(categories) { category ->
            fetchMeme(category)
        }
        binding.recyclerViewCategories.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewCategories.adapter = adapter
    }

    private fun fetchMeme(category: String) {
        apiService.getRandomJoke(category).enqueue(object : Callback<ChuckApiResponse> {
            override fun onResponse(
                call: Call<ChuckApiResponse>,
                response: Response<ChuckApiResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val joke = response.body()?.value
                    binding.textViewMeme.text = joke
                } else {
                    Toast.makeText(this@Chuckmemes, "Error al obtener el meme", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ChuckApiResponse>, t: Throwable) {
                Toast.makeText(this@Chuckmemes, "Fallo en la conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
