package com.markusw.lambda.home.presentation

import com.markusw.lambda.R

enum class NavigationItem(
    val label: String,
    val icon: Int
) {

    Home(
        icon = R.drawable.ic_home,
        label = "Inicio"
    ),
    Shopping(
        icon = R.drawable.ic_shopping_cart,
        label = "Tienda"
    ),
    Exit(
        icon = R.drawable.ic_exit,
        label = "Salir"
    )
}