package com.example.login.presentation.ui.binding

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.login.R


@BindingAdapter("imageUrl")
fun loadIcon(view: ImageView, url: String?) {
    if (url != null && url.isNotEmpty()) {
        Glide.with(view.context)
            .load(url)
            .apply(RequestOptions().placeholder(R.drawable.loading).error(R.drawable.alerticon))
            .into(view)
    }
}