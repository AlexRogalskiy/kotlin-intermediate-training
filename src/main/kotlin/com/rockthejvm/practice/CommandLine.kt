package com.rockthejvm.practice

import java.util.Scanner
import kotlin.system.exitProcess

object CommandLine {

    fun start() {
        val scanner = Scanner(System.`in`)

        while (true) {
            print("> ")
            val command = scanner.nextLine()
            val tokens = command.split(" ")
            val cmd = tokens[0]
            when (cmd) {
                "load" -> AppUI.load(tokens[1])
                "exit" -> exitProcess(0)
                else -> {
                    val transformation = Transformation.parse(command)
                    val newImage = transformation.process(AppUI.getImage())
                    AppUI.update(newImage)
                }
            }
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        start()
    }
}

// src/main/resources/pictures