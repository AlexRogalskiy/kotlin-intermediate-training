package com.rockthejvm.practice

class Image {
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
