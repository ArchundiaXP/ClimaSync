package com.example.climasync.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.climasync.R
import com.example.climasync.databinding.FragmentClimaItemBinding
import com.example.climasync.viewModel.Clima

class ClimaAdapter(
    private val climas: MutableList<Clima>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<ClimaAdapter.ViewHolder>() { // <-- La clase empieza aquí

    private lateinit var context: Context


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = FragmentClimaItemBinding.bind(view)

        // Esta función configura cada item de la lista
        fun setUpUI(clima: Clima) {
            binding.dayTextView.text = clima.day
            binding.lowTempTextView.text = clima.temperatureMin
            binding.highTempTextView.text = clima.temperatureMax

            // El listener para clicks
            itemView.setOnClickListener {
                onItemClick(clima.day)
            }

            Glide.with(itemView.context)
                .load(clima.iconUrl)
                .placeholder(R.drawable.downloading_ic)
                .error(R.drawable.product_error_ic)
                .into(binding.weatherIconImageView)
        }
    }

    fun add(climaItems: List<Clima>) {
        climas.addAll(climaItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_clima_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return climas.size // Es un poco más eficiente usar .size en listas
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setUpUI(climas[position])
    }

}