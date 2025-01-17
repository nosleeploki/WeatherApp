package com.example.login.presentation.ui.weather

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.login.R
import com.example.login.data.model.HourlyForecast
import com.example.login.databinding.WaItemHourlyBinding

class HourlyForecastAdapter :
    ListAdapter<HourlyForecast, HourlyForecastAdapter.HourlyViewHolder>(HourlyForecastDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyViewHolder {
        val binding = DataBindingUtil.inflate<WaItemHourlyBinding>(
            LayoutInflater.from(parent.context),
            R.layout.wa_item_hourly,
            parent,
            false
        )
        return HourlyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HourlyViewHolder, position: Int) {
        val item = getItem(position)
        Log.d("HourlyForecastAdapter", "Binding item: $item")
        holder.bind(item)
    }

    inner class HourlyViewHolder(private val binding: WaItemHourlyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(hourlyForecast: HourlyForecast) {
            binding.tvTime.text = hourlyForecast.time
            binding.tvTemp.text = hourlyForecast.temperature
            Glide.with(binding.root.context)
                .load(hourlyForecast.iconUrl)
                .placeholder(R.drawable.loading)
                .error(R.drawable.alerticon)
                .into(binding.ivHourlyWeatherIcon)
            binding.executePendingBindings()
        }
    }

    class HourlyForecastDiffCallback : DiffUtil.ItemCallback<HourlyForecast>() {
        override fun areItemsTheSame(oldItem: HourlyForecast, newItem: HourlyForecast): Boolean {
            return oldItem.time == newItem.time
        }

        override fun areContentsTheSame(oldItem: HourlyForecast, newItem: HourlyForecast): Boolean {
            return oldItem == newItem
        }
    }
}
