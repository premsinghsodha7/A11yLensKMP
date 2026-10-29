package com.premsingh.a11ylens.kmp

import androidx.compose.ui.graphics.Color

data class ContrastPair(
    val name: String,
    val foreground: Color,
    val background: Color,
)

fun contrastRatio(foreground: Color, background: Color): Double {
    val first = relativeLuminance(foreground)
    val second = relativeLuminance(background)
    val lighter = maxOf(first, second)
    val darker = minOf(first, second)
    return (lighter + 0.05) / (darker + 0.05)
}

private fun relativeLuminance(color: Color): Double {
    val red = linearize(color.red.toDouble())
    val green = linearize(color.green.toDouble())
    val blue = linearize(color.blue.toDouble())
    return 0.2126 * red + 0.7152 * green + 0.0722 * blue
}

private fun linearize(channel: Double): Double =
    if (channel <= 0.04045) channel / 12.92 else ((channel + 0.055) / 1.055).let { it * it * it }

fun roundedRatio(ratio: Double): String = ((ratio * 10).toInt() / 10.0).toString()
