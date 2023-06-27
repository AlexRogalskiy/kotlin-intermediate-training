package com.rockthejvm.practice

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

// int = 00000000rrrrrrrrggggggggbbbbbbbb
class Color(r: Int, g: Int, b: Int) {
    val red = r.coerceIn(0..255)
    val green = g.coerceIn(0..255)
    val blue = b.coerceIn(0..255)

    infix operator fun plus(another: Color) =
        Color(
            (red + another.red),
            (green + another.green),
            (blue + another.blue)
        )

    infix operator fun times(factor: Double) =
        Color(
            (red * factor).toInt(),
            (green * factor).toInt(),
            (blue * factor).toInt(),
        )

    fun toInt() =
        (red shl 16) or (green shl 8) or blue

    fun draw(width: Int, height: Int, path: String) {
        val color = toInt()
        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        val pixels = IntArray(width * height) { color }
        image.setRGB(0,0,width,height, pixels, 0, width)
        ImageIO.write(image, "JPG", File(path))
    }
}

fun Int.toColor(): Color =
    Color((this and 0xFF0000) shr 16, (this and 0xFF00) shr 8, this and 0xFF)

object ColorDemo {
    @JvmStatic
    fun main(args: Array<String>) {
        val red = Color(255, 0, 0)
        val green = Color(0, 255, 0)
        val yellow = red + green
        yellow.draw(100,100, "src/main/resources/colors/yellow.jpg")
    }
}