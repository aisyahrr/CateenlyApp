package com.example.canteenlyapp.utils

import android.content.Context
import com.example.canteenlyapp.R

fun getMenuImage(
    context: Context,
    imageKey: String
): Int {
    return context.resources.getIdentifier(
        imageKey,
        "drawable",
        context.packageName
    ).takeIf { it != 0 }
        ?: R.drawable.placeholder_food
}