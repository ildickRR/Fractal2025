package ru.gr05307.julia

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.rememberWindowState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.gr05307.math.Complex

// Простой менеджер для хранения параметров окон Жюлиа
object JuliaWindowManager {
    private val _windows = mutableListOf<Complex>()
    val windows: List<Complex> get() = _windows

    fun openWindow(c: Complex) {
        _windows.add(c)
    }

    fun closeWindow(c: Complex) {
        _windows.remove(c)
    }
}

@Composable
fun JuliaWindow(
    c: Complex,
    onClose: () -> Unit
) {
    var imageState by remember { mutableStateOf<List<List<Color>>?>(null) }
    var windowSize by remember { mutableStateOf(IntSize(800, 600)) }
    val scope = rememberCoroutineScope()
    val windowState = rememberWindowState() // Создаем state здесь

    LaunchedEffect(c, windowSize) {
        scope.launch(Dispatchers.Default) {
            imageState = renderJulia(c, windowSize.width, windowSize.height)
        }
    }

    Window(
        onCloseRequest = onClose,
        title = "Множество Жюлиа: c = ${c.re} + ${c.im}i",
        state = windowState
    ) {
        MaterialTheme {
            Canvas(Modifier.fillMaxSize()) {
                windowSize = IntSize(size.width.toInt(), size.height.toInt())
                val img = imageState ?: return@Canvas
                val w = size.width.toInt()
                val h = size.height.toInt()

                for (x in 0 until w) {
                    for (y in 0 until h) {
                        drawRect(
                            color = img[x][y],
                            topLeft = androidx.compose.ui.geometry.Offset(x.toFloat(), y.toFloat()),
                            size = androidx.compose.ui.geometry.Size(1f, 1f)
                        )
                    }
                }
            }
        }
    }
}

private fun renderJulia(c: Complex, w: Int, h: Int): List<List<Color>> {
    val maxIter = 300
    val result = List(w) { MutableList(h) { Color.Black } }

    val scale = 1.6
    for (xi in 0 until w) {
        val re = (xi - w/2.0) / (w/2.0) * scale
        for (yi in 0 until h) {
            val im = (yi - h/2.0) / (h/2.0) * scale
            var z = Complex(re, im)
            var iter = 0
            while (iter < maxIter && z.absoluteValue2 < 4) {
                z = z * z
                z = z + c
                iter++
            }
            val t = iter / maxIter.toFloat()
            result[xi][yi] = if (iter == maxIter) Color.Black
            else Color.hsv(t * 360f, 0.8f, 0.9f)
        }
    }
    return result
}