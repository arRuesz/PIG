package com.example.myapplication.multimedia

import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ActivityVideoPlayerBinding
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import java.io.File

class VideoPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVideoPlayerBinding
    private lateinit var videoRecyclerView: RecyclerView
    private lateinit var videoAdapter: VideoAdapter
    private lateinit var playerView: PlayerView
    private var exoPlayer: ExoPlayer? = null
    private lateinit var currentUser: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentUser = intent.getStringExtra("username") ?: "default_user"

        // Inicializar vistas
        videoRecyclerView = binding.videoRecyclerView
        playerView = binding.playerView

        // Configurar RecyclerView
        videoRecyclerView.layoutManager = LinearLayoutManager(this)
        videoAdapter = VideoAdapter(mutableListOf()) { videoFile ->
            playVideo(videoFile)
        }
        videoRecyclerView.adapter = videoAdapter

        // Cargar videos del usuario
        loadUserVideos()
    }

    private fun loadUserVideos() {
        val videosPath = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES),
            "CameraX-Video/$currentUser"
        )

        val videoFiles = getVideoFilesFromDirectory(videosPath)
        videoAdapter.updateVideos(videoFiles)
    }

    private fun getVideoFilesFromDirectory(directory: File): List<File> {
        val videoFiles = mutableListOf<File>()

        if (directory.exists() && directory.isDirectory) {
            val files = directory.listFiles { file ->
                file.isFile && (file.name.endsWith(".mp4") || file.name.endsWith(".mkv"))
            }
            if (files != null) {
                videoFiles.addAll(files)
            }
        } else {
            Log.d("VideoPlayerActivity", "Directory does not exist: ${directory.absolutePath}")
        }

        return videoFiles
    }

    private fun playVideo(videoFile: File) {
        // Liberar el reproductor anterior si existe
        releasePlayer()

        // Inicializar ExoPlayer
        exoPlayer = ExoPlayer.Builder(this).build()
        playerView.player = exoPlayer

        // Configurar el MediaItem
        val mediaItem = MediaItem.fromUri(Uri.fromFile(videoFile))
        exoPlayer?.setMediaItem(mediaItem)
        exoPlayer?.prepare()
        exoPlayer?.play()
    }

    private fun releasePlayer() {
        exoPlayer?.release()
        exoPlayer = null
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }
}