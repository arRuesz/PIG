package com.example.myapplication.multimedia

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemGalleryBinding
import java.io.File

class GalleryAdapter(
    private var mediaFiles: MutableList<File>,
    private val context: Context
) : RecyclerView.Adapter<GalleryAdapter.MediaViewHolder>() {

    inner class MediaViewHolder(private val binding: ItemGalleryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(file: File) {
            binding.filePathTextView.text = file.absolutePath
            binding.filePathTextView.setOnClickListener {
                showConfirmationDialog(file)
            }
        }

        private fun showConfirmationDialog(file: File) {
            AlertDialog.Builder(context)
                .setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to delete this file?")
                .setPositiveButton("Yes") { dialog, which ->
                    if (file.delete()) {
                        Toast.makeText(context, "File deleted: ${file.absolutePath}", Toast.LENGTH_SHORT).show()
                        removeFile(file)
                    } else {
                        Toast.makeText(context, "Failed to delete file: ${file.absolutePath}", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("No", null)
                .show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val binding = ItemGalleryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MediaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        holder.bind(mediaFiles[position])
    }

    override fun getItemCount(): Int = mediaFiles.size

    fun removeFile(file: File) {
        mediaFiles.remove(file)
        notifyDataSetChanged()
    }
}