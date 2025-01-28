package com.example.myapplication.juegopig

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PlayerAdapter(
    private val playerNames: List<String>,
    private val onPlayerClick: (String) -> Unit
) : RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return PlayerViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        holder.bind(playerNames[position], onPlayerClick)
    }

    override fun getItemCount(): Int {
        return playerNames.size
    }

    class PlayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val playerNameTextView: TextView = itemView.findViewById(android.R.id.text1)

        fun bind(playerName: String, onPlayerClick: (String) -> Unit) {
            playerNameTextView.text = playerName
            itemView.setOnClickListener {
                onPlayerClick(playerName)
            }
        }
    }
}
