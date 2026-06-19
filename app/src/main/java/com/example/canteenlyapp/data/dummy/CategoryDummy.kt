package com.example.canteenlyapp.data.dummy

import com.example.canteenlyapp.R
import com.example.canteenlyapp.data.model.Category

object CategoryDummy {

    val categories = listOf(

        Category(
            name = "All",
            image = R.drawable.all_category
        ),

        Category(
            name = "Main Course",
            image = R.drawable.main_course
        ),

        Category(
            name = "Bakery",
            image = R.drawable.bakery
        ),

        Category(
            name = "Desserts",
            image = R.drawable.desserts
        ),

        Category(
            name = "Drinks",
            image = R.drawable.drinks
        )
    )
}