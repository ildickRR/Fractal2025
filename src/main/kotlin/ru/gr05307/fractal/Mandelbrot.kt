package ru.gr05307.fractal

import ru.gr05307.math.Complex

class Mandelbrot(
    val nMax: Int = 200,
    val r: Double = 2.0,
) {
    fun isInSet(c: Complex): Float{
        val z = Complex()
        repeat(nMax) { n ->
            z *= z
            z += c
            if (z.abs2 >= r * r) return n.toFloat() / nMax
        }
        return 1f
    }
}

/*package ru.gr05307.fractals

import kotlin.math.sqrt
import kotlin.concurrent.thread

fun mandelbrot(
    width: Int,
    height: Int,
    maxIter: Int,
    xMin: Double,
    xMax: Double,
    yMin: Double,
    yMax: Double
): Array<IntArray> {

    val result = Array(height) { IntArray(width) }

    // Расчет шагов
    val dx = (xMax - xMin) / width
    val dy = (yMax - yMin) / height

    // Массивы координат c
    val cRe = DoubleArray(width)
    val cIm = DoubleArray(height)

    for (i in 0 until width) cRe[i] = xMin + i * dx
    for (j in 0 until height) cIm[j] = yMin + j * dy

    // Горячий цикл без объектов, чистые Double
    for (j in 0 until height) {
        val im = cIm[j]
        for (i in 0 until width) {
            var re = 0.0
            var imZ = 0.0
            val cReVal = cRe[i]

            var iter = 0
            while (re * re + imZ * imZ <= 4.0 && iter < maxIter) {
                val newRe = re * re - imZ * imZ + cReVal
                imZ = 2 * re * imZ + im
                re = newRe
                iter++
            }
            result[j][i] = iter
        }
    }

    return result
}

fun mandelbrotParallel(
    width: Int,
    height: Int,
    maxIter: Int,
    xMin: Double,
    xMax: Double,
    yMin: Double,
    yMax: Double,
    nThreads: Int = Runtime.getRuntime().availableProcessors()
): Array<IntArray> {

    val result = Array(height) { IntArray(width) }
    val dx = (xMax - xMin) / width
    val dy = (yMax - yMin) / height

    val cRe = DoubleArray(width) { i -> xMin + i * dx }
    val cIm = DoubleArray(height) { j -> yMin + j * dy }

    val threads = ArrayList<Thread>()

    val rowsPerThread = (height + nThreads - 1) / nThreads

    for (t in 0 until nThreads) {
        val startRow = t * rowsPerThread
        val endRow = minOf(startRow + rowsPerThread, height)

        val thread = thread {
            for (j in startRow until endRow) {
                val im = cIm[j]
                for (i in 0 until width) {
                    var re = 0.0
                    var imZ = 0.0
                    val cReVal = cRe[i]

                    var iter = 0
                    while (re*re + imZ*imZ <= 4.0 && iter < maxIter) {
                        val newRe = re*re - imZ*imZ + cReVal
                        imZ = 2*re*imZ + im
                        re = newRe
                        iter++
                    }
                    result[j][i] = iter
                }
            }
        }
        threads.add(thread)
    }

    threads.forEach { it.join() } // ждем завершения всех потоков
    return result
}
*/