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

    val aspectRatio: Double
        get() = if (yMax - yMin != 0.0) (xMax - xMin) / (yMax - yMin) else 1.0

}
