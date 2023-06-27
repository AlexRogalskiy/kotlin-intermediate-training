package com.rockthejvm.practice

import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.WindowConstants
import java.awt.Dimension
import java.awt.Graphics

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
/**
 * TODO exercise 1
 *      Create a CommandLine singleton with a forever system.in reader.
 *      Just read the lines and print them back for now.
 *      When the CommandLine reads "load (path)" you'll make the ImageEditor display the image at that path in a window.
 *      The command "exit" will exit the application.
 *      Any other command will be simply shown back to you.
 *
 *
 * TODO exercise 2
 *      Create a Transformation type with a simple API to take an image and return another image.
 *      Make a factory of the following types of transformations
 *          - CropTransformation(x,y,w,h)
 *          - BlendTransformation(path, mode)
 *      What signature would the factory method have?
 *      Where would you put that factory?
 *      From the command line app, trigger the transformation changes.
 *          Possible commands:
 *              - crop x y w h
 *              - blend path mode
 */

