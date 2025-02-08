package com.example.myapplication

import android.animation.Animator
import android.content.Intent
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.example.myapplication.chuck.Chuckmemes
import com.example.myapplication.databinding.ActivityHubBinding
import com.example.myapplication.firebase.FirebaseActivity
import com.example.myapplication.juegopig.MainActivity
import com.example.myapplication.logins.AppDatabase
import com.example.myapplication.multimedia.PreCam
import com.example.myapplication.multimedia.SoundActivity
import com.example.myapplication.multimedia.VideoPlayerActivity
import com.squareup.picasso.Picasso

class Hub : AppCompatActivity() {
    private lateinit var binding: ActivityHubBinding
    private lateinit var soundPool: SoundPool
    private var soundId: Int = 0
    private var streamId: Int = 0
    private var isPlaying = false
    private lateinit var animationCup: LottieAnimationView
    private lateinit var animationPausePlay: LottieAnimationView
    private lateinit var animationClick: LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHubBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar las vistas de Lottie
        animationCup = binding.animationCup
        animationPausePlay = binding.animationPausePlay
        animationClick = binding.animationClick

        // Configurar listeners para las animaciones
        binding.cupImg.setOnClickListener {
            binding.cupImg.visibility = View.INVISIBLE
            animationCup.visibility = View.VISIBLE
            animationCup.playAnimation()
            animationCup.addAnimatorListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationEnd(animation: Animator) {
                    animationCup.visibility = View.INVISIBLE
                    binding.cupImg.visibility = View.VISIBLE
                }

                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })
        }

        binding.btnMute.setOnClickListener {
            if (isPlaying) {
                // Reproducir animación de pausa
                animationPausePlay.setAnimation(R.raw.pauseani)
                animationPausePlay.visibility = View.VISIBLE
                animationPausePlay.playAnimation()
                soundPool.pause(streamId)
                binding.btnMute.text = "Activar"
            } else {
                // Reproducir animación de play
                animationPausePlay.setAnimation(R.raw.playani)
                animationPausePlay.visibility = View.VISIBLE
                animationPausePlay.playAnimation()
                streamId = soundPool.play(soundId, 1f, 1f, 1, -1, 1f)
                binding.btnMute.text = "Desactivar"
            }
            isPlaying = !isPlaying

            // Ocultar la animación después de que termine
            animationPausePlay.addAnimatorListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationEnd(animation: Animator) {
                    animationPausePlay.visibility = View.INVISIBLE
                }

                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })
        }

        binding.soundApp.setOnClickListener {
            animationClick.visibility = View.VISIBLE
            animationClick.playAnimation()
            animationClick.addAnimatorListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationEnd(animation: Animator) {
                    animationClick.visibility = View.INVISIBLE
                }

                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })
            goToSounds()
        }

        // Resto del código...
        binding.fotoPig.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.imagenChuck.setOnClickListener { goToChuck() }
        binding.fotoFirebase.setOnClickListener {
            val username = intent.getStringExtra("name")
            val intent = Intent(this@Hub, FirebaseActivity::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }

        binding.fotoCamara.setOnClickListener { goToCamera() }
        selectPhoto()

        binding.videoPlayer.setOnClickListener { goToPlayer() }

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(1)
            .setAudioAttributes(audioAttributes)
            .build()

        soundId = soundPool.load(this, R.raw.tapion_dbz, 1)

        soundPool.setOnLoadCompleteListener { _, _, _ ->
            streamId = soundPool.play(soundId, 1f, 1f, 1, -1, 1f)
            isPlaying = true
        }
    }

    private fun selectPhoto() {
        val username = intent.getStringExtra("name")
        Thread {
            val userDao = AppDatabase.getInstance(this).userDao()
            val picUrl = username?.let { userDao.getUrl(it) }
            runOnUiThread {
                if (!picUrl.isNullOrEmpty()) {
                    Picasso.get().load(picUrl).resize(100, 100).into(binding.imageViewProfile)
                } else {
                    binding.imageViewProfile.setImageResource(R.drawable.perfilsinfoto)
                }
                binding.tVUsername.text = username
            }
        }.start()
    }

    private fun goToChuck() {
        val intent = Intent(this@Hub, Chuckmemes::class.java)
        startActivity(intent)
    }

    private fun goToCamera() {
        val username = intent.getStringExtra("name")
        val intent = Intent(this@Hub, PreCam::class.java)
        intent.putExtra("username", username)
        startActivity(intent)
    }

    private fun goToPlayer() {
        val username = intent.getStringExtra("name")
        val intent = Intent(this@Hub, VideoPlayerActivity::class.java)
        intent.putExtra("username", username)
        startActivity(intent)
    }

    private fun goToSounds() {
        val intent = Intent(this@Hub, SoundActivity::class.java)
        startActivity(intent)
    }

    override fun onPause() {
        super.onPause()
        soundPool.autoPause()
    }

    override fun onStop() {
        super.onStop()
        soundPool.autoPause()
    }

    override fun onResume() {
        super.onResume()
        soundPool.autoResume()
        isPlaying = true
    }
}