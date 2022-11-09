package cryptography

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileNotFoundException
import javax.imageio.ImageIO
import java.lang.Exception
import java.security.MessageDigest

const val BITS_IN_BYTE = 8

fun modifyImage(image: BufferedImage) {
    for (x in 0 until image.width) {
        for (y in 0 until image.height) {
            val color = image.getRGB(x, y)
            val newColor = color or (1 + (1 shl 8) + (1 shl 16))
            image.setRGB(x, y, newColor)
        }
    }
}

fun getBit(n: Int, pos: Int): Int {
    return (n shr (BITS_IN_BYTE - 1 - pos)) and 1
}

fun hideMessageInImage(message: String, image: BufferedImage) {

    val bytes = message.encodeToByteArray() + byteArrayOf(0, 0, 3)
    val nBits = BITS_IN_BYTE * bytes.size
    if (nBits > image.width * image.height) {
        throw Exception("The input image is not large enough to hold this message.")
    }
    for (i in 0 until nBits) {
        val x = i % image.width
        val y = i / image.width
        val q = i / BITS_IN_BYTE  // index of byte in array
        val r = i % BITS_IN_BYTE  // index of bit in byte
        val b = getBit(bytes[q].toInt(), r)
        val color = image.getRGB(x, y)
        val newColor = (color shr 1 shl 1) or b
        // println(color)
        // println(newColor)
        image.setRGB(x, y, newColor)
    }
}

fun readMessageFromImage(image: BufferedImage): String {
    val endPattern = listOf("00000000".toUByte(2), "00000000".toUByte(2), "00000011".toUByte(2))
    var bits = ""
    var messageList = ubyteArrayOf()

    for (y in 0 until image.height) {
        for (x in 0 until image.width) {
            if (bits.length == BITS_IN_BYTE) {
                messageList += bits.toUByte(2)
                bits = ""
                if (messageList.size >= 3) {
                    val index = messageList.lastIndex
                    val lastThreeBytes = listOf(messageList[index - 2], messageList[index - 1], messageList[index])
                    if (lastThreeBytes == endPattern) {
                            val message = messageList.dropLast(3).toUByteArray().toByteArray()
                            return message.toString(Charsets.UTF_8)}
                }
            }
            val color = image.getRGB(x, y)
            bits += (color and 1).toString()
        }
    }
    val message = messageList.dropLast(3).toUByteArray().toByteArray()
    return message.toString(Charsets.UTF_8)

}

fun hide() {
    println("Input image file:")
    val inputName = readln()
    println("Output image file:")
    val outputName = readln()
    println("Message to hide:")
    val message = readln()
    val inputFile = File(inputName)
    try {
        val image: BufferedImage = ImageIO.read(inputFile)
        // modifyImage(image)
        hideMessageInImage(message, image)
        val outputFile = File(outputName)
        ImageIO.write(image, "png", outputFile)
        println("Message saved in $outputName image.")
    } catch (e: FileNotFoundException) {
        println("Can't read input file!")
    } catch (e: Exception) {
        println(e.toString())
    }
}

fun show() {
    println("Input image file:")
    val inputName = readln()
    val inputFile = File(inputName)
    try {
        val image: BufferedImage = ImageIO.read(inputFile)
        val message = readMessageFromImage(image)
        println("Message:")
        println(message)

    } catch (e: FileNotFoundException) {
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
                // println("Obtaining message from image.")
                show()
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

