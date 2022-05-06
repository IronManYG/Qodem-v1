package com.example.qodem.utils

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import com.example.qodem.R
import com.google.android.material.snackbar.Snackbar

fun View.showSnackbar(
    view: View,
    msg: String,
    length: Int,
    actionMessage: CharSequence?,
    context: Context,
    action: (View) -> Unit
) {
    val snackbar = Snackbar.make(view, msg, length)
//    snackbar.apply {
//        setBackgroundTint(ContextCompat.getColor(context, R.color.secondaryLightColor))
//        setTextColor(ContextCompat.getColor(context, R.color.secondaryDarkColor))
//        setActionTextColor(ContextCompat.getColor(context, R.color.secondaryDarkColor))
//    }
    if (actionMessage != null) {
        snackbar.setAction(actionMessage) {
            action(this)
        }.show()
    } else {
        snackbar.show()
    }
}

val <T> T.exhaustive: T
    get() = this