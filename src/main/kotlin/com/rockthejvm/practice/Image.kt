package com.rockthejvm.practice

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

class Image(val bfImage: BufferedImage) {
    val width = bfImage.width
    val height = bfImage.height
    val pixels: MutableList<Color> = mutableListOf()

    init {
        val pixelsInts = bfImage.getRGB(0, 0, width, height, IntArray(width * height), 0, width)
        for (i in 0 until (width * height))
            pixels.add(pixelsInts[i].toColor())
    }

    operator fun get(x: Int, y: Int): Color =
        pixels[y * width + x]

    fun write(path: String) =
        ImageIO.write(bfImage, "JPG", File(path))

    constructor(path: String): this(ImageIO.read(File(path)))

    companion object {
        fun fromPixels(width: Int, height: Int, pixels: List<Color>): Image {
            val bfImage = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
            val pixelsInts = IntArray(width * height)
            for (i in 0 until (width * height))
                pixelsInts[i] = pixels[i].toInt()

            bfImage.setRGB(0,0,width,height, pixelsInts, 0, width)
            return Image(bfImage)
        }
    }
}
