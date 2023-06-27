package com.rockthejvm.practice

object App {
    @JvmStatic
    fun main(args: Array<String>) {
        val paris = Image("src/main/resources/pictures/paris.jpeg")
        val gradient = Image("src/main/resources/pictures/redblue.jpg")

        val combined = Blend(gradient, Multiply()).process(paris)
        combined.write("src/main/resources/results/paris-red.jpg")

        val cropped = Crop(400, 400, 500, 500).process(paris)
        cropped.write("src/main/resources/results/paris-cropped.jpg")
    }
}