package ru.gr05307.ExportFractal

import ru.gr05307.painting.convertation.Plain
import ru.gr05307.math.Complex
import ru.gr05307.fractal.Mandelbrot
import ru.gr05307.painting.convertation.Converter
import androidx.compose.ui.graphics.Color
import java.awt.Color as AwtColor
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.absoluteValue
import kotlin.math.cos
import kotlin.math.sin

class FractalExporter(
    private val plain: Plain
) {
    private val mandelbrot = Mandelbrot(nMax = 200)

    fun saveJPG(path: String) {
        val image = process()
        infoComplex(image)
        ImageIO.write(image, "jpg", File(path))
    }
    private fun CopyGetColor(probability: Float) = if (probability == 1f)
        Color.Black
    else Color(
        red = cos(7 * probability).absoluteValue,
        green = sin(12 * (1f - probability)).absoluteValue,
        blue = (sin(4 * probability) * cos(4 * (1 - probability))).absoluteValue
    )

    private fun process(): BufferedImage {
        val w = plain.width.toInt()
        val h = plain.height.toInt()
        val img = BufferedImage(w, h, BufferedImage.TYPE_INT_RGB)

        for (x in 0 until w) {
            for (y in 0 until h) {
                val cplx = Complex(
                    Converter.xScr2Crt(x.toFloat(), plain),
                    Converter.yScr2Crt(y.toFloat(), plain)
                )

                val prob = mandelbrot.isInSet(cplx)
                val color = CopyGetColor(prob)
                img.setRGB(x, y, toRGB(color).rgb)
            }
        }

        return img
    }


    private fun infoComplex(image: BufferedImage) {
        val g = image.createGraphics()
        g.color = AwtColor.BLACK
        g.drawString(
            "Re: [${plain.xMin}; ${plain.xMax}]  Im: [${plain.yMin}; ${plain.yMax}]",
            10,
            image.height - 10
        )
        g.dispose()
    }

    private fun toRGB(c: Color): AwtColor =
        AwtColor((c.red * 255).toInt(), (c.green * 255).toInt(), (c.blue * 255).toInt())
}
