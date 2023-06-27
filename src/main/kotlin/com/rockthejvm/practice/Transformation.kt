package com.rockthejvm.practice

abstract class Transformation {
    abstract fun process(image: Image): Image
    companion object {
        // factory method
        fun parse(string: String): Transformation {
            val tokens = string.split(" ")
            val cmd = tokens[0]
            return when (cmd) {
                "crop" -> Crop(
                    tokens[1].toInt(),
                    tokens[2].toInt(),
                    tokens[3].toInt(),
                    tokens[4].toInt(),
                )
                "blend" -> Blend(tokens[1], tokens[2])
                else -> Noop
            }
        }
    }
}

class Crop(val x: Int, val y: Int, val w: Int, val h: Int): Transformation() {
    override fun process(image: Image): Image =
        Image.fromPixels(
            w,
            h,
            image.pixels
                .chunked(image.width)
                .slice(y until (y + h))
                .map { it.slice(x until (x + w)) }
                .flatten()
        )
}

class Blend(val fgImage: Image, val mode: BlendMode): Transformation() {
    constructor(path: String, type: String): this(Image(path), BlendMode.parse(type))

    override fun process(bgImage: Image): Image {
        assert(fgImage.width == bgImage.width)
        assert(fgImage.height == bgImage.height)

        val width = bgImage.width
        val height = bgImage.height
        val pixels = mutableListOf<Color>()
        for (i in 0 until (width * height))
            pixels.add(mode.combine(fgImage.pixels[i], bgImage.pixels[i]))

        return Image.fromPixels(width, height, pixels)
    }
}

object Noop: Transformation() {
    override fun process(image: Image): Image = image
}



