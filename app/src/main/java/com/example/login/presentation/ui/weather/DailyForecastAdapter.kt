package com.example.login.presentation.ui.weather

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.login.R
import com.example.login.data.model.DailyForecastUiModel
import com.example.login.databinding.WaItemDailyBinding

class DailyForecastAdapter :
    ListAdapter<DailyForecastUiModel, DailyForecastAdapter.DailyViewHolder>(DailyForecastDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {
        val binding = DataBindingUtil.inflate<WaItemDailyBinding>(
            LayoutInflater.from(parent.context),
            R.layout.wa_item_daily,
            parent,
            false
        )
        return DailyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DailyViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }


    inner class DailyViewHolder(private val binding: WaItemDailyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dailyForecast: DailyForecastUiModel) {
            binding.tvDate.text = dailyForecast.date
            binding.tvDailyTemp.text = dailyForecast.temperature
            Glide.with(binding.root.context)
                .load(dailyForecast.iconUrl)
                .placeholder(R.drawable.loading)
                .error(R.drawable.alerticon)
                .into(binding.ivDailyWeatherIcon)
        }
    }

    class DailyForecastDiffCallback : DiffUtil.ItemCallback<DailyForecastUiModel>() {
        override fun areItemsTheSame(oldItem: DailyForecastUiModel, newItem: DailyForecastUiModel): Boolean {
            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(oldItem: DailyForecastUiModel, newItem: DailyForecastUiModel): Boolean {
            return oldItem == newItem
        }
    }
}
