package ru.netology.mymapmarkers.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.mymapmarkers.databinding.CardMarkerBinding
import ru.netology.mymapmarkers.dto.Marker

class MarkerAdapter : ListAdapter<Marker, MarkerViewHolder>(MarkerDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarkerViewHolder {
        val binding = CardMarkerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MarkerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MarkerViewHolder, position: Int) {
        val marker = getItem(position)
        holder.bind(marker)
    }
}

class MarkerViewHolder(
    private val binding: CardMarkerBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(marker: Marker) {
        binding.apply {
            markerName.text = marker.name
            markerDescription.text = marker.description
            markerCoordinates.text = "${marker.latitude}, ${marker.longitude}"
        }
    }
}

class MarkerDiffCallback : DiffUtil.ItemCallback<Marker>() {
    override fun areItemsTheSame(oldItem: Marker, newItem: Marker): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Marker, newItem: Marker): Boolean {
        return oldItem == newItem
    }
}
