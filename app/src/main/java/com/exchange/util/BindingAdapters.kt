package com.exchange.util

import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter

@BindingAdapter("isVisible")
fun setView(linearLayout: LinearLayout, visible: Boolean){
    linearLayout.isVisible = visible
}