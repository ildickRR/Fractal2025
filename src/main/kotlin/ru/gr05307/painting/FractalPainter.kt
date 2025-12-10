package ru.gr05307.painting

import ru.gr05307.fractal.calculateIterations
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import ru.gr05307.math.Complex
import ru.gr05307.painting.convertation.Converter
import ru.gr05307.painting.convertation.Plain
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import org.jetbrains.skia.*

class FractalPainter(
    private val plain: Plain,
    var fractalFunc: FractalFunction,
    var colorFunc: ColorFunction,
) : Painter {

    override suspend fun paint(scope: DrawScope) {
        plain.width = scope.size.width
        plain.height = scope.size.height

        val w = plain.width.toInt()
        val h = plain.height.toInt()

        val nMax = calculateIterations(plain)

        // 4 bytes per pixel (RGBA)
        val pixels = ByteArray(w * h * 4)

        var index = 0
        for (y in 0 until h) {
            val cy = Converter.yScr2Crt(y.toFloat(), plain)

            for (x in 0 until w) {
                val cx = Converter.xScr2Crt(x.toFloat(), plain)

                val v = fractalFunc(Complex(cx, cy), nMax)
                val color = colorFunc(v).toArgb()

                // ARGB → RGBA
                pixels[index++] = ((color shr 16) and 0xFF).toByte() // R
                pixels[index++] = ((color shr 8) and 0xFF).toByte()  // G
                pixels[index++] = (color and 0xFF).toByte()          // B
                pixels[index++] = ((color shr 24) and 0xFF).toByte() // A
            }
        }

        val imageInfo = ImageInfo.makeN32Premul(w, h)
        val stride = w * 4

        val image = Image.makeRaster(imageInfo, pixels, stride)
        val bitmap: ImageBitmap = image.asImageBitmap()

        scope.drawImage(bitmap, Offset.Zero)
    }
}
