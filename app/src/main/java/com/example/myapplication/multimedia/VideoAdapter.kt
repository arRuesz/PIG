package com.example.myapplication.multimedia

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class VideoAdapter(
    private var videoFiles: MutableList<File>,
    private val onVideoClick: (File) -> Unit
) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    inner class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val videoName: TextView = itemView.findViewById(android.R.id.text1)

        fun bind(videoFile: File) {
            videoName.text = videoFile.name
            itemView.setOnClickListener {
                onVideoClick(videoFile)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return VideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(videoFiles[position])
    }

    override fun getItemCount(): Int = videoFiles.size

    fun updateVideos(newVideos: List<File>) {
        videoFiles.clear()
        videoFiles.addAll(newVideos)
        notifyDataSetChanged()
    }
}