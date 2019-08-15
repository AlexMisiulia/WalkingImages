package com.simplyfire.komoottesttask.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

fun ViewGroup.inflateAdapterView(layoutId: Int): View {
    return LayoutInflater.from(context).inflate(layoutId, this, false)
}