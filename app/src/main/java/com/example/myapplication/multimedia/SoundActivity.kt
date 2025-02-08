package com.example.myapplication.multimedia

import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivitySoundBinding
import android.os.Handler
import android.os.Looper
import kotlin.math.sin

class SoundActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySoundBinding
    private lateinit var soundPool: SoundPool
    private val soundMap = mutableMapOf<Int, Int>()
    private var currentStreamId: Int = -1
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySoundBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar SoundPool
        soundPool = SoundPool.Builder()
            .setMaxStreams(1)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            .build()

        // Cargar sonidos
        soundMap[R.raw.audio1] = soundPool.load(this, R.raw.audio1, 1)
        soundMap[R.raw.audio2] = soundPool.load(this, R.raw.audio2, 1)
        soundMap[R.raw.audio3] = soundPool.load(this, R.raw.audio3, 1)

        // Configurar botones
        binding.btnPlayAudio1.setOnClickListener { playAudio(R.raw.audio1) }
        binding.btnPlayAudio2.setOnClickListener { playAudio(R.raw.audio2) }
        binding.btnPlayAudio3.setOnClickListener { playAudio(R.raw.audio3) }
        binding.btnSpeedUp.setOnClickListener { changeSpeed(1.5f) }
        binding.btnSpeedDown.setOnClickListener { changeSpeed(0.5f) }
        binding.btnEqualizer.setOnClickListener { applyEchoEffect() }
        binding.btnReverb.setOnClickListener { applyDistortionEffect() }
        binding.btnStop.setOnClickListener { stopAudio() }
    }

    private fun playAudio(resId: Int) {
        stopAudio() // Detener audio anterior
        soundMap[resId]?.let {
            currentStreamId = soundPool.play(it, 1.0f, 1.0f, 1, 0, 1.0f)
        }
    }

    private fun changeSpeed(speed: Float) {
        if (currentStreamId != -1) {
            soundPool.setRate(currentStreamId, speed)
        }
    }

    private fun applyEchoEffect() {
        if (currentStreamId == -1) return
        handler.postDelayed({
            soundPool.play(currentStreamId, 0.6f, 0.6f, 1, 0, 1.0f)
        }, 300) // Retraso de 300 ms
        handler.postDelayed({
            soundPool.play(currentStreamId, 0.4f, 0.4f, 1, 0, 1.0f)
        }, 600) // Retraso de 600 ms
    }

    private fun applyDistortionEffect() {
        if (currentStreamId == -1) return
        var intensity = 1.5f
        for (i in 0..4) {
            handler.postDelayed({
                val modulatedVolume = (0.5f * sin(i * 0.5) + 0.5).toFloat() * intensity
                soundPool.setVolume(currentStreamId, modulatedVolume, modulatedVolume)
            }, i * 100L) // Simula una oscilación de la amplitud
        }
    }

    private fun stopAudio() {
        if (currentStreamId != -1) {
            soundPool.stop(currentStreamId)
            currentStreamId = -1
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        soundPool.release()
    }
}
