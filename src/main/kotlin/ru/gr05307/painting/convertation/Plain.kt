package ru.gr05307.painting.convertation

data class Plain(
    var xMin: Double,
    var xMax: Double,
    var yMin: Double,
    var yMax: Double,
    var width: Float = 0f,
    var height: Float = 0f,
) {
    // Плотность: сколько пикселей на единицу комплексной плоскости (Double)
    val xDen: Double
        get() = if (xMax - xMin != 0.0) width.toDouble() / (xMax - xMin) else 1.0

    val yDen: Double
        get() = if (yMax - yMin != 0.0) height.toDouble() / (yMax - yMin) else 1.0

    /** Соотношение сторон плоскости (ширина диапазона / высота диапазона). */
    val aspectRatio: Double
        get() = if (yMax - yMin != 0.0) (xMax - xMin) / (yMax - yMin) else 1.0

    /**
     * Вспомогательные методы для конвертации экранных координат в координаты плоскости.
     * (Экран: x вправо, y вниз.)
     */
    fun xScrToCrt(xScr: Float): Double {
        if (width == 0f) return xMin
        return xMin + (xScr.toDouble() / width.toDouble()) * (xMax - xMin)
    }

    fun yScrToCrt(yScr: Float): Double {
        if (height == 0f) return yMax
        // важный момент: экранная y растёт вниз, поэтому верхняя часть экрана соответствует yMax
        return yMax - (yScr.toDouble() / height.toDouble()) * (yMax - yMin)
    }
}
