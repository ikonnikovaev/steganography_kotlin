package cryptography

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import java.lang.Exception


fun modifyImage(image: BufferedImage) {
    for (x in 0 until image.width) {
        for (y in 0 until image.height) {
            val color = image.getRGB(x, y)
            val newColor = color or (1 + (1 shl 8) + (1 shl 16))
            image.setRGB(x, y, newColor)
        }
    }
}

fun hide() {
    println("Input image file:")
    val inputName = readln()
    println("Output image file:")
    val outputName = readln()
    val inputFile = File(inputName)
    try {
        val image: BufferedImage = ImageIO.read(inputFile)
        modifyImage(image)
        val outputFile = File(outputName)
        ImageIO.write(image, "png", outputFile)
        println("Input Image: $inputName\n" +
                "Output Image: $outputName\n" +
                "Image $outputName is saved."
        )
    } catch (e: Exception) {
        println("Can't read input file!")
    }

}


fun chooseTask() {
    while (true) {
        println("Task (hide, show, exit):")
        val input = readln()
        when (input) {
            "exit" -> {
                println("Bye!")
                break
            }
            "hide" -> {
                // println("Hiding message in image.")
                hide()
            }
            "show" -> {
                println("Obtaining message from image.")
            }
            else -> {
                println("Wrong task: [input String]")
            }
        }
    }
}

fun main() {
    chooseTask()
}

