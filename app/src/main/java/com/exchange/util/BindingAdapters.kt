package com.exchange.util

import android.net.Uri
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter

@BindingAdapter("isVisible")
fun setView(linearLayout: LinearLayout, visible: Boolean){
    linearLayout.isVisible = visible
}

@BindingAdapter("imageUri")
fun bindImage(imageView: ImageView, imageUri: Uri){
    imageView.setImageURI(imageUri)
}