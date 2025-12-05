package ru.gr05307.painting

import androidx.compose.ui.graphics.Color
import ru.gr05307.math.Complex
import ru.gr05307.fractal.Mandelbrot
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin


val mandelbrotFunc: FractalFunction = { z, nMax ->
    val m = Mandelbrot(nMax = nMax)
    m.isInSet(z).coerceIn(0f, 1f)
}

// --- Цветовые схемы ---
val grayscale: ColorFunction = { p ->
    val v = p.coerceIn(0f, 1f)
    Color(v, v, v)
}

val rainbow: ColorFunction = { p ->
    val pp = p.coerceIn(0f, 1f)
    if (pp == 1f) Color.Black else Color(
        red = abs(cos(7 * pp)),
        green = abs(sin(12 * (1f - pp))),
        blue = abs(sin(4 * pp) * cos(4 * (1 - pp)))
    )
}
val fireGradient: ColorFunction = { p ->
    val pp = p.coerceIn(0f, 1f)
    Color(
        red = pp,
        green = (pp * 0.5f).coerceIn(0f, 1f),
        blue = 0f
    )
}
val iceGradient: ColorFunction = { p ->
    val pp = p.coerceIn(0f, 1f)
    Color(
        red = (pp * 0.5f).coerceIn(0f, 1f),
        green = pp,
        blue = 1f
    )
}




