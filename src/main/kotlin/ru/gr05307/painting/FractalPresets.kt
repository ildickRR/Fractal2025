package ru.gr05307.painting

import androidx.compose.ui.graphics.Color
import ru.gr05307.math.Complex
import ru.gr05307.fractal.Mandelbrot
import ru.gr05307.fractal.Julia
import ru.gr05307.fractal.NewtonFractal
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.pow

// Функция для множества Мандельброта
val mandelbrotFunc: FractalFunction = { z, nMax ->
    val m = Mandelbrot(nMax = nMax)
    m.isInSet(z).coerceIn(0f, 1f)
}

// Функция для множества Жюлиа (теперь создаем экземпляр класса Julia)
val juliaFunc: FractalFunction = { z, nMax ->
    // Используем стандартные параметры для Julia
    val j = Julia(nMax = nMax)
    j.iterate(z).coerceIn(0f, 1f)
}

// Функция для фрактала Ньютона
val newtonFunc: FractalFunction = { z, nMax ->
    val n = NewtonFractal(nMax)
    n.iterate(z).coerceIn(0f, 1f)
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
/*val fireGradient: ColorFunction = { p ->
    val pp = p.coerceIn(0f, 1f)
    Color(
        red = pp,
        green = (pp * 0.5f).coerceIn(0f, 1f),
        blue = 0f
    )
}*/

val iceGradient: ColorFunction = { p ->
    val pp = p.coerceIn(0f, 1f)
    Color(
        red = (pp * 0.5f).coerceIn(0f, 1f),
        green = pp,
        blue = 1f
    )
}
val newtonColor: ColorFunction = { p ->
    val pp = p.coerceIn(0f, 1f)

    // Мерцание для звёзд
    val twinkle = abs(sin(15f * pp) * cos(9f * pp))

    // ЖЁЛТЫЙ НА ПЕТЛЯХ - делаем нелинейное усиление
    // Чем ближе pp к 1, тем более жёлтый цвет
    val yellowOnLoops = when {
        pp > 0.9f -> (pp - 0.9f) * 10f  // Максимально жёлтые петли
        pp > 0.7f -> (pp - 0.7f) * 5f   // Сильно жёлтые
        pp > 0.5f -> (pp - 0.5f) * 2f   // Умеренно жёлтые
        else -> 0f
    }

    // Затемняем фон сильнее
    val darkBackground = (1f - pp).pow(3f) * 0.15f

    Color(
        red = (0.8f * pp + 0.4f * twinkle + yellowOnLoops).coerceIn(0f, 1f),
        green = (0.75f * pp + 0.3f * twinkle + yellowOnLoops * 0.85f).coerceIn(0f, 1f),
        blue = (0.3f * pp + 0.2f * twinkle + darkBackground).coerceIn(0f, 1f)
    )
}

/*package ru.gr05307.painting

import androidx.compose.ui.graphics.Color
import ru.gr05307.math.Complex
import ru.gr05307.fractals.mandelbrot
import ru.gr05307.fractals.mandelbrotParallel
import ru.gr05307.fractal.Julia
import ru.gr05307.fractal.NewtonFractal
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.pow

// Функция для множества Мандельброта (оптимизированная версия)
val mandelbrotFunc: FractalFunction = { z, nMax ->
    // Встраиваем алгоритм без создания объектов Complex
    var re = 0.0
    var imZ = 0.0
    val cRe = z.re
    val cIm = z.im
    var iter = 0

    while (re * re + imZ * imZ <= 4.0 && iter < nMax) {
        val newRe = re * re - imZ * imZ + cRe
        imZ = 2 * re * imZ + cIm
        re = newRe
        iter++
    }

    (if (iter == nMax) 1f else iter.toFloat() / nMax).coerceIn(0f, 1f)
}

// Функция для множества Жюлиа (остаётся без изменений)
val juliaFunc: FractalFunction = { z, nMax ->
    val j = Julia(nMax = nMax)
    j.iterate(z).coerceIn(0f, 1f)
}

// Функция для фрактала Ньютона (остаётся без изменений)
val newtonFunc: FractalFunction = { z, nMax ->
    val n = NewtonFractal(nMax)
    n.iterate(z).coerceIn(0f, 1f)
}

// Оптимизированный вариант для быстрого вычисления всего изображения Мандельброта
fun createMandelbrotImage(
    width: Int,
    height: Int,
    maxIter: Int,
    xMin: Double,
    xMax: Double,
    yMin: Double,
    yMax: Double,
    colorFunc: ColorFunction
): Array<ColorArray> {

    // Используем новую оптимизированную функцию
    val iterations = mandelbrotParallel(
        width = width,
        height = height,
        maxIter = maxIter,
        xMin = xMin,
        xMax = xMax,
        yMin = yMin,
        yMax = yMax
    )

    // Конвертируем итерации в цвета
    val colors = Array(height) { ColorArray(width) }

    for (y in 0 until height) {
        for (x in 0 until width) {
            val iter = iterations[y][x]
            val prob = if (iter == maxIter) 1f else iter.toFloat() / maxIter
            colors[y][x] = colorFunc(prob)
        }
    }

    return colors
}

// Вспомогательный класс для массива цветов
class ColorArray(val size: Int) {
    private val array = Array<Color?>(size) { null }

    operator fun get(index: Int): Color = array[index]!!
    operator fun set(index: Int, value: Color) {
        array[index] = value
    }
}

// --- Цветовые схемы остаются без изменений ---
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

val iceGradient: ColorFunction = { p ->
    val pp = p.coerceIn(0f, 1f)
    Color(
        red = (pp * 0.5f).coerceIn(0f, 1f),
        green = pp,
        blue = 1f
    )
}

val newtonColor: ColorFunction = { p ->
    val pp = p.coerceIn(0f, 1f)

    // Мерцание для звёзд
    val twinkle = abs(sin(15f * pp) * cos(9f * pp))

    // ЖЁЛТЫЙ НА ПЕТЛЯХ - делаем нелинейное усиление
    // Чем ближе pp к 1, тем более жёлтый цвет
    val yellowOnLoops = when {
        pp > 0.9f -> (pp - 0.9f) * 10f  // Максимально жёлтые петли
        pp > 0.7f -> (pp - 0.7f) * 5f   // Сильно жёлтые
        pp > 0.5f -> (pp - 0.5f) * 2f   // Умеренно жёлтые
        else -> 0f
    }

    // Затемняем фон сильнее
    val darkBackground = (1f - pp).pow(3f) * 0.15f

    Color(
        red = (0.8f * pp + 0.4f * twinkle + yellowOnLoops).coerceIn(0f, 1f),
        green = (0.75f * pp + 0.3f * twinkle + yellowOnLoops * 0.85f).coerceIn(0f, 1f),
        blue = (0.3f * pp + 0.2f * twinkle + darkBackground).coerceIn(0f, 1f)
    )
}

// Дополнительная цветовая схема, которая хорошо работает с новой оптимизацией
val smoothRainbow: ColorFunction = { p ->
    val pp = p.coerceIn(0f, 1f)
    val hue = pp * 360f
    val saturation = 0.8f + 0.2f * sin(pp * 3.14159f * 4f) // Небольшая пульсация насыщенности
    val brightness = if (pp < 0.99f) 1f else 0f // Черный внутри множества

    // Конвертируем HSV в RGB
    val c = brightness * saturation
    val x = c * (1 - abs((hue / 60f) % 2f - 1))
    val m = brightness - c

    val (r, g, b) = when {
        hue < 60f -> Triple(c, x, 0f)
        hue < 120f -> Triple(x, c, 0f)
        hue < 180f -> Triple(0f, c, x)
        hue < 240f -> Triple(0f, x, c)
        hue < 300f -> Triple(x, 0f, c)
        else -> Triple(c, 0f, x)
    }

    Color(r + m, g + m, b + m)
}*/