package com.example.climasync.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.climasync.databinding.ItemForecastDayBinding
import com.example.climasync.model.ForecastResponse.ForecastDay
import java.text.SimpleDateFormat
import java.util.*

class ForecastAdapter(
    private val items: MutableList<ForecastDay> = mutableListOf()

) : RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {

    inner class ForecastViewHolder(val binding: ItemForecastDayBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val binding = ItemForecastDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ForecastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        val forecast = items[position]

        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("EEEE", Locale("es", "MX"))
        val date = inputFormat.parse(forecast.date)
        val dayName = outputFormat.format(date ?: Date())

        with(holder.binding) {
            tvDay.text = dayName.replaceFirstChar { it.uppercaseChar() }
            tvMaxTemp.text = "${forecast.day.maxTempC.toInt()}°"
            tvMinTemp.text = "${forecast.day.minTempC.toInt()}°"

            Glide.with(imgWeatherIcon.context)
                .load("https:${forecast.day.condition.icon}")
                .into(imgWeatherIcon)
        }
    }

    override fun getItemCount() = items.size

    fun updateForecast(newItems: List<ForecastDay>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}
