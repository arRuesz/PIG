package com.example.myapplication.logins

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityFotosBinding
import com.squareup.picasso.Picasso
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class ActivityFotos : AppCompatActivity() {
    private lateinit var binding: ActivityFotosBinding
    private var selectedGender: String = "male"

    private val apiService: IconApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://randomuser.me/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IconApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFotosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val genders = listOf("Hombre", "Mujer")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genders)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerGenre.adapter = adapter
        binding.spinnerGenre.setOnItemSelectedListener { position ->
            selectedGender = if (position == 0) "male" else "female"
            fetchPhotos()
        }

        // Refrescar
        binding.refresh.setOnClickListener {
            fetchPhotos()
        }
        fetchPhotos()
    }

    private fun intentPhotos(icon: ImageView, imageUrl: String) {
        icon.setOnClickListener {
            val intent = Intent(this@ActivityFotos, Register::class.java)
            intent.putExtra("IMAGE_URL", imageUrl)
            startActivity(intent)
            finish()
        }
    }

    private fun fetchPhotos() {
        apiService.getRandomIcons(selectedGender).enqueue(object : Callback<IconApiResponse> {
            override fun onResponse(call: Call<IconApiResponse>, response: Response<IconApiResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val results = response.body()?.results
                    if (!results.isNullOrEmpty()) {
                        val url1 = results[0].picture.large
                        val url2 = results[1].picture.large
                        val url3 = results[2].picture.large

                        Picasso.get().load(url1).resize(400, 400).into(binding.foto1)
                        Picasso.get().load(url2).resize(400, 400).into(binding.foto2)
                        Picasso.get().load(url3).resize(400, 400).into(binding.foto3)

                        intentPhotos(binding.foto1, url1)
                        intentPhotos(binding.foto2, url2)
                        intentPhotos(binding.foto3, url3)
                    }
                } else {
                    Toast.makeText(this@ActivityFotos, "Error al obtener datos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<IconApiResponse>, t: Throwable) {
                Log.e("ActivityFotos", "Error: ${t.message}")
                Toast.makeText(this@ActivityFotos, "Fallo en la conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun Spinner.setOnItemSelectedListener(onItemSelected: (Int) -> Unit) {
        this.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: android.widget.AdapterView<*>?,
                view: android.view.View?,
                position: Int,
                id: Long
            ) {
                onItemSelected(position)
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
        }
    }
}