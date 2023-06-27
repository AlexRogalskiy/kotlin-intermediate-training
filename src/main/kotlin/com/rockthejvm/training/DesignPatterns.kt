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

    fun demoStrategy() {
        val strategy = LayoutStrategy.create("simple")
        val posts = strategy.layout(
            listOf(
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean ullamcorper, leo et ornare tincidunt, nunc augue finibus justo, eget malesuada libero magna quis elit. Donec congue lorem dictum porta finibus. Praesent ut diam tortor. Maecenas vulputate dignissim iaculis. Maecenas nisl metus, volutpat quis tincidunt vitae, ullamcorper vitae erat. Etiam pharetra interdum sodales. Praesent sit amet quam et dui pretium condimentum. Ut aliquam malesuada ornare. Integer nulla ligula, lobortis vel malesuada id, blandit vel lectus. Maecenas quis lacus sed nisl elementum sollicitudin vel sit amet est. Curabitur mollis massa sed tellus commodo hendrerit. Morbi ultrices augue ut est pretium aliquet nec vitae neque. Morbi dignissim libero eget nunc vestibulum suscipit. Etiam maximus dolor quis ante tincidunt, at convallis nulla tristique. Aenean pharetra nisi erat, ut accumsan metus tempor et.",
                "Cras molestie tellus ac lacus fermentum, nec aliquet nisl porta. Praesent justo nibh, aliquam quis nisi a, finibus tempor velit. Cras porttitor sapien in diam convallis, vitae commodo metus fringilla. Fusce convallis convallis felis, in laoreet ex rhoncus vitae. Suspendisse ullamcorper leo at purus convallis, maximus tempus felis pulvinar. Sed congue turpis sit amet erat malesuada blandit. Curabitur egestas orci vitae magna gravida gravida. Etiam facilisis nisl eu ligula fringilla mattis. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum lacinia velit ligula, eu varius dui aliquet quis. Curabitur efficitur augue nec est gravida tempus. Interdum et malesuada fames ac ante ipsum primis in faucibus. Fusce ultrices metus eu auctor posuere. Morbi gravida scelerisque augue, ac congue ligula pulvinar sed."
            )
        )
        posts.forEach { println(it) }
    }

    // builder - useful for configurable data structures & constructors with lots of args
    data class SparkSession(
        val masterNode: String,
        val nExecutors: Int,
        val jars: List<String>,
        val configs: List<String>
    ) {
        // nested class
        class Builder { // mutable instance
            var masterNode: String = ""
            var nExecutors: Int = 0
            var jars: MutableList<String> = mutableListOf()
            var configs: MutableList<String> = mutableListOf()

            // setters for ALL FIELDS
            fun withMasterNode(master: String): Builder {
                this.masterNode = master
                return this
            }

            fun withNExecutors(n: Int): Builder {
                this.nExecutors = n
                return this
            }

            fun withJar(jar: String): Builder {
                this.jars.add(jar)
                return this
            }

            fun withConfig(config: String): Builder {
                this.configs.add(config)
                return this
            }

            // with a final build method
            fun build(): SparkSession =
                SparkSession(masterNode, nExecutors, jars, configs)
        }

        companion object {
            fun builder() = Builder()
        }
    }

    val spark = SparkSession.builder()
        .withJar("com.rockthejvm....")
        .withJar("com.typesafe.akka")
        .withConfig("--executor-memory 5g")
        .withMasterNode("localhost")
        .withNExecutors(2)
        .build()

    // observer

    // notification
    data class Notification(val app: String, val content: String)
    // observable/"notifier"
    class Smartphone {
        val observers: MutableSet<Observer> = mutableSetOf()

        // add/remove subscriptions
        fun addObserver(obs: Observer) {
            observers.add(obs)
        }

        fun removeObserver(obs: Observer) {
            observers.remove(obs)
        }

        // notify observers
        private fun notifyObservers(notification: Notification) {
            for (obs in observers)
                obs.update(notification)
        }

        // change state
        private fun invokeGmailService(): List<String> =
            listOf("Your tickets to Untold", "You got a new message")

        // public API
        fun checkEmail(account: String) {
            // call Gmail API
            val emails = invokeGmailService()
            notifyObservers(Notification("Gmail", "You got ${emails.size} emails"))
        }

        fun receivePhoneCall() {
            notifyObservers(Notification("phone", "dad calling"))
        }
    }

    // observer/listener
    interface Observer {
        fun update(notification: Notification)
    }

    object UIObserver: Observer {
        override fun update(notification: Notification) {
            println("[notification center] ${notification.content}")
        }
    }

    class BackupObserver(val location: String): Observer {
        override fun update(notification: Notification) {
            // potentially DIFFERENT impl
            println("[$location] Log successful: ${notification.app} - ${notification.content}")
        }
    }

    fun demoObserver() {
        val phone = Smartphone()
        phone.addObserver(UIObserver)
        phone.addObserver(BackupObserver("Google Drive"))
        phone.addObserver(BackupObserver("local disk"))

        // in some other place in the code
        phone.checkEmail("daniel@rockthejvm.com")
        phone.receivePhoneCall()
    }

    // command
    // visitor

    @JvmStatic
    fun main(args: Array<String>) {
        demoObserver()
    }
}