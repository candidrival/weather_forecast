package com.app.live.weather.forecast.presentation.weeks_forecast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.live.weather.forecast.R
import com.app.live.weather.forecast.data.ForecastItem
import com.app.live.weather.forecast.databinding.ItemForecastBinding

class ForecastAdapter : ListAdapter<ForecastItem, ForecastAdapter.ForecastViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val binding = ItemForecastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ForecastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ForecastViewHolder(private val binding: ItemForecastBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ForecastItem) {
            binding.tvDate.text = item.dateText
            binding.tvTemp.text = binding.root.context.getString(R.string.temperature_today, item.main.temp.toString())
            binding.tvDesc.text = item.weather.firstOrNull()?.description ?: ""
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ForecastItem>() {
        override fun areItemsTheSame(oldItem: ForecastItem, newItem: ForecastItem): Boolean =
            oldItem.dt == newItem.dt

        override fun areContentsTheSame(oldItem: ForecastItem, newItem: ForecastItem): Boolean =
            oldItem == newItem
    }
}