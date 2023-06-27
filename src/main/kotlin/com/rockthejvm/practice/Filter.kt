package com.rockthejvm.practice

abstract class Filter {
    abstract fun process(image: Image): Image
}

class Crop(val x: Int, val y: Int, val w: Int, val h: Int): Filter() {
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

class Blend(val fgImage: Image, val mode: BlendMode): Filter() {
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



