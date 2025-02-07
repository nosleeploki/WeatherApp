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
import java.text.SimpleDateFormat
import java.util.Locale

class DailyForecastAdapter(private val dailyForecastList: List<ForecastItem>) :
    RecyclerView.Adapter<DailyForecastAdapter.DailyForecastViewHolder>() {

    inner class DailyForecastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val ivDailyWeatherIcon: ImageView = itemView.findViewById(R.id.ivDailyWeatherIcon)
        val tvDailyTemp: TextView = itemView.findViewById(R.id.tvDailyTemp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyForecastViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.wa_item_daily, parent, false)
        return DailyForecastViewHolder(view)
    }

    override fun onBindViewHolder(holder: DailyForecastViewHolder, position: Int) {
        val item = dailyForecastList[position]

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = dateFormat.parse(item.dateTime.split(" ")[0])
        val formattedDate = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(date)
        holder.tvDate.text = formattedDate
        holder.tvDailyTemp.text = "${(item.main.temperature).toInt()}°C"
        val iconUrl = "https://openweathermap.org/img/wn/${item.weather[0].icon}@2x.png"
        Glide.with(holder.itemView.context).load(iconUrl).into(holder.ivDailyWeatherIcon)
    }

    override fun getItemCount(): Int {
        return dailyForecastList.size
    }
}