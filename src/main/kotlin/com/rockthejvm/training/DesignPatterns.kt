package com.rockthejvm.training

import java.lang.IllegalArgumentException

object DesignPatterns {

    // singleton = one instance of a type
    class MyCoreService private constructor() { // single instance
        companion object {
            lateinit var instance: MyCoreService

            fun getOrCreate(): MyCoreService {
                // initialization
                // config
                if (!Companion::instance.isInitialized)
                    instance = MyCoreService() // do the initializations

                return instance
            }
        }

        // API
        fun callMyApi(param: String): String = "impl not important"
    }

    val service = MyCoreService.getOrCreate()
    // use API
    val serviceCall = service.callMyApi("/resource/image")

    // Kotlin idiomatic singleton - use objects
    // some of the first to be initialized in the whole app
    object MyService {
        // define API
        fun callMyApi(param: String): String = "impl not important"
    }

    // call this from any other file
    val betterServiceCall = MyService.callMyApi("/my/path")

    // factory
    interface Window
    data class Win11Window(val width: Int, val height: Int, val transparency: Double): Window
    data class MacWindow(val width: Int, val height: Int, val style: String): Window

    interface Button
    data class Win11Button(val position: String): Button
    data class MacButton(val fullWidth: Boolean): Button

    object UIFactoryService {
        // factory method
        fun makeWindow(os: String): Window =
            when (os) {
                "macos" -> MacWindow(800, 600, "nice")
                "win" -> Win11Window(800, 600, 0.5)
                else -> throw IllegalArgumentException("OS not supported")
            }

        // same for makeButton etc, or other UI elements
    }

    val someWindow = UIFactoryService.makeWindow("macos")
    // use the Window API for this variable

    // variation: abstract factory
    interface UIFactory {
        fun makeWindow(): Window
        fun makeButton(): Button

        companion object {
            fun make(os: String): UIFactory = when (os) {
                "win" -> WinUIFactory()
                "macos" -> MacUIFactory()
                else -> throw IllegalArgumentException("OS not supported")
            }
        }
    }

    class WinUIFactory: UIFactory {
        override fun makeButton(): Button = Win11Button("center")
        override fun makeWindow(): Window = Win11Window(800, 600, 0.5)
    }

    class MacUIFactory: UIFactory {
        override fun makeButton(): Button = MacButton(true)
        override fun makeWindow(): Window = MacWindow(800, 600, "very nice")
    }

    val uiFactory = UIFactory.make("macos")
    val aButton = uiFactory.makeButton()
    val aWindow = uiFactory.makeWindow()

    // strategy
    // command
    // observer
    // visitor

    @JvmStatic
    fun main(args: Array<String>) {

    }
}