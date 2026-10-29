package com.premsingh.a11ylens.kmp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "A11y Lens") { App() }
}
