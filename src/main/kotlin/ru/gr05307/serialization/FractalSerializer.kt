package ru.gr05307.serialization

import ru.gr05307.painting.convertation.Plain
import ru.gr05307.painting.ColorFunction
import ru.gr05307.painting.*
import org.json.JSONObject
import java.io.File

data class FractalData(
    val plain: Plain,
    val fractalType: String,
    val colorType: String
)

class FractalSerializer {

    fun save(
        plain: Plain,
        fractalType: String,
        colorType: String,
        filePath: String
    ) {
        val json = JSONObject()

        json.put("xMin", plain.xMin)
        json.put("xMax", plain.xMax)
        json.put("yMin", plain.yMin)
        json.put("yMax", plain.yMax)
        json.put("width", plain.width)
        json.put("height", plain.height)
        json.put("fractalType", fractalType)
        json.put("colorType", colorType)

        File(filePath).writeText(json.toString())
    }

    fun load(filePath: String): FractalData {
        val json = JSONObject(File(filePath).readText())

        val plain = Plain(
            xMin = json.getDouble("xMin"),
            xMax = json.getDouble("xMax"),
            yMin = json.getDouble("yMin"),
            yMax = json.getDouble("yMax"),
            width = json.getDouble("width").toFloat(),
            height = json.getDouble("height").toFloat()
        )

        val fractalType = json.optString("fractalType", "mandelbrot")
        val colorType = json.getString("colorType")

        return FractalData(plain, fractalType, colorType)
    }
}
