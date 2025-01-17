package com.example.login.presentation.ui.binding

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.login.R


@BindingAdapter("android:text")
    fun setDoubleText(view: TextView, value: Double?) {
        value?.let {
            view.text = "${it.toInt()}°C"
        }
    }

@BindingAdapter("android:src")
fun loadImage(view: ImageView, icon: String?) {
    if (icon != null) {
        val url = "https://openweathermap.org/img/wn/${icon}@2x.png"
        Glide.with(view.context).load(url).into(view)
    }
}

@BindingAdapter("android:text")
fun setFormattedTemperature(view: TextView, temperature: Double?) {
    temperature?.let {
        view.text = String.format("%.1f°C", it)
    }
}

@BindingAdapter("imageUrl")
fun loadIcon(view: ImageView, url: String?) {
    if (url != null && url.isNotEmpty()) {
        Glide.with(view.context)
            .load(url)
            .apply(RequestOptions().placeholder(R.drawable.loading).error(R.drawable.alerticon))
            .into(view)
    }
}