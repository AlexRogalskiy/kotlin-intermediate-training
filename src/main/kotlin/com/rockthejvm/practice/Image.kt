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
        for (i in 0..(width * height))
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
            for (i in 0..(width * height))
                pixelsInts[i] = pixels[i].toInt()

            bfImage.setRGB(0,0,width,height, pixelsInts, 0, width)
            return Image(bfImage)
        }
    }

    /**
     * TODO write an Image class that wraps a BufferedImage
     *  - constructor(BufferedImage)
     *  - width, height, pixels: List<Color>, get[x,y]: Color
     *  - write(file: String): Unit = stores a picture file
     *
     * TODO write a Filter class
     *  - process(Image): Image
     *
     * TODO implement some instances of Filter
     *  - Blend(fgImag) -> mix fgImage with image in the process function
     *      Transparency - average of colors
     *      Multiply - (colorA / 255) * (colorB / 255) * 255
     *  - Crop(x,y,w,h) -> new (smaller) image
     */


    /*
        write a BufferedImage:
            ImageIO.write(bufferedImage, "JPG", File(...))
        read a BufferedImage
            bfImg = ImageIO.read(File(...))
     */
}
