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
    data class Post(val content: String)

    interface LayoutStrategy {
        fun layout(news: List<String>): List<Post>
        // easily combined with factory
        companion object {
            fun create(strategy: String) = when(strategy) {
                "compact" -> CompactStrategy(20)
                else -> SimpleStrategy
            }
        }
    }

    class CompactStrategy(val charLimit: Int): LayoutStrategy {
        override fun layout(news: List<String>): List<Post> {
            val result: MutableList<Post> = mutableListOf()
            for (article in news)
                result.add(
                    Post(article.chunked(charLimit).joinToString("\n"))
                )
            return result
        }
    }

    object SimpleStrategy: LayoutStrategy {
        override fun layout(news: List<String>): List<Post> {
            val result: MutableList<Post> = mutableListOf()
            for (article in news)
                result.add(Post(article))
            return result        }
    }

    // builder
    // command
    // observer
    // visitor

    @JvmStatic
    fun main(args: Array<String>) {
        val strategy = LayoutStrategy.create("simple")
        val posts = strategy.layout(
            listOf(
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean ullamcorper, leo et ornare tincidunt, nunc augue finibus justo, eget malesuada libero magna quis elit. Donec congue lorem dictum porta finibus. Praesent ut diam tortor. Maecenas vulputate dignissim iaculis. Maecenas nisl metus, volutpat quis tincidunt vitae, ullamcorper vitae erat. Etiam pharetra interdum sodales. Praesent sit amet quam et dui pretium condimentum. Ut aliquam malesuada ornare. Integer nulla ligula, lobortis vel malesuada id, blandit vel lectus. Maecenas quis lacus sed nisl elementum sollicitudin vel sit amet est. Curabitur mollis massa sed tellus commodo hendrerit. Morbi ultrices augue ut est pretium aliquet nec vitae neque. Morbi dignissim libero eget nunc vestibulum suscipit. Etiam maximus dolor quis ante tincidunt, at convallis nulla tristique. Aenean pharetra nisi erat, ut accumsan metus tempor et.",
                "Cras molestie tellus ac lacus fermentum, nec aliquet nisl porta. Praesent justo nibh, aliquam quis nisi a, finibus tempor velit. Cras porttitor sapien in diam convallis, vitae commodo metus fringilla. Fusce convallis convallis felis, in laoreet ex rhoncus vitae. Suspendisse ullamcorper leo at purus convallis, maximus tempus felis pulvinar. Sed congue turpis sit amet erat malesuada blandit. Curabitur egestas orci vitae magna gravida gravida. Etiam facilisis nisl eu ligula fringilla mattis. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum lacinia velit ligula, eu varius dui aliquet quis. Curabitur efficitur augue nec est gravida tempus. Interdum et malesuada fames ac ante ipsum primis in faucibus. Fusce ultrices metus eu auctor posuere. Morbi gravida scelerisque augue, ac congue ligula pulvinar sed."
            )
        )

        posts.forEach { println(it) }
    }
}