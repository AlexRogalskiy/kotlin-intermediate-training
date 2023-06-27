package com.rockthejvm.practice

import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.WindowConstants
import java.awt.Dimension
import java.awt.Graphics
import java.awt.image.BufferedImage

object App {
    private lateinit var imagePanel: ImagePanel

    class ImagePanel(private var image: Image) : JPanel() {
        override fun paintComponent(g: Graphics) {
            super.paintComponent(g)
            g.drawImage(image.bfImage, 0, 0, null)
        }

        override fun getPreferredSize(): Dimension {
            return Dimension(image.width, image.height)
        }

        fun updateImage(newImage: Image) {
            image = newImage
            preferredSize = Dimension(newImage.width, newImage.height)
            revalidate()
            repaint()
        }
    }

    fun load(path: String) {
        val image = Image(path)
        val frame = JFrame("Image Display")
        imagePanel = ImagePanel(image)

        frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        frame.contentPane.add(imagePanel)
        frame.pack()
        frame.isVisible = true
    }

    @JvmStatic
    fun main(args: Array<String>) {
        load("src/main/resources/pictures/paris.jpeg")
    }
}
