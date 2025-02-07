package com.example.login.presentation.ui.weather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.login.R
import com.example.login.data.model.ForecastItem

class HourlyForecastAdapter(private val hourlyForecastList: List<ForecastItem>) :
    RecyclerView.Adapter<HourlyForecastAdapter.HourlyForecastViewHolder>() {

    inner class HourlyForecastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        val ivWeatherIcon: ImageView = itemView.findViewById(R.id.ivWeatherIcon)
        val tvTemp: TextView = itemView.findViewById(R.id.tvTemp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyForecastViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.wa_item_hourly, parent, false)
        return HourlyForecastViewHolder(view)
    }

    override fun onBindViewHolder(holder: HourlyForecastViewHolder, position: Int) {
        val item = hourlyForecastList[position]

        holder.tvTime.text = item.dateTime.split(" ")[1]
        holder.tvTemp.text = "${item.main.temperature}°C"
        val iconUrl = "https://openweathermap.org/img/wn/${item.weather[0].icon}@2x.png"
        Glide
            .with(holder.itemView.context)
            .load(iconUrl)
            .into(holder.ivWeatherIcon)
    }

    override fun getItemCount(): Int {
        return hourlyForecastList.size
    }
}