package com.example.qodem.utils

import android.content.Context
import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.showSnackBar(
    view: View,
    msg: String,
    length: Int,
    actionMessage: CharSequence?,
    context: Context,
    action: (View) -> Unit
) {
    val snackBar = Snackbar.make(view, msg, length)
//    snackBar.apply {
//        setBackgroundTint(ContextCompat.getColor(context, R.color.secondaryLightColor))
//        setTextColor(ContextCompat.getColor(context, R.color.secondaryDarkColor))
//        setActionTextColor(ContextCompat.getColor(context, R.color.secondaryDarkColor))
//    }
    if (actionMessage != null) {
        snackBar.setAction(actionMessage) {
            action(this)
        }.show()
    } else {
        snackBar.show()
    }
}

val <T> T.exhaustive: T
    get() = this