package com.cengizhanerturan.kotlinnewsapplication.core.util

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.cengizhanerturan.kotlinnewsapplication.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@BindingAdapter("imageUrl")
fun ImageView.loadImage(
    url: String?,
) {
    url?.let {
        Glide.with(context)
            .load(it)
            .into(this)
    }
}

fun View.clearFocusAndHideKeyboard() {
    this.setOnTouchListener { v, event ->
        if (event.action == MotionEvent.ACTION_DOWN) {
            v.clearFocus()
            v.hideKeyboard()
            v.performClick()
        }
        false
    }
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun String.isEmailValid(): Boolean {
    val emailRegex = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}"
    return emailRegex.toRegex().matches(this)
}

fun String.isPasswordValid(): Boolean {
    val passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$"
    return passwordRegex.toRegex().matches(this)
}

fun String.formatDate(): String {
    val inputFormatter = DateTimeFormatter.ISO_DATE_TIME
    val outputFormatter = DateTimeFormatter.ofPattern("MM.dd.yyyy HH:mm")
    val dateTime = LocalDateTime.parse(this, inputFormatter)
    return dateTime.format(outputFormatter)
}

fun LinearLayout.applyListLoading() {
    for (i in 1..10) {
        val loadingView = LayoutInflater.from(context)
            .inflate(R.layout.list_loading_item, this, false)
        this.addView(loadingView)
    }
}
