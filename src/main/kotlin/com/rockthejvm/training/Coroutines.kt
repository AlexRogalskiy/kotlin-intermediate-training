package com.rockthejvm.training

import kotlinx.coroutines.*
import org.slf4j.LoggerFactory

object Coroutines {

    /*
        JVM thread are expensive
     */

    // light threads = coroutines
    // coroutines are simple data structures
    // JVM threads = 1000s/CPU
    // coroutines = 10000000/GB heap
    // coroutines are scheduled on a few JVM threads.

    // alternatives: thread pools, async (callback hell), "reactive" programming (RxJava)

    val logger = LoggerFactory.getLogger("CoroutinesDemo")

    suspend fun sendMessage() {
        logger.info("Sending a small message") // thread 1
        // yield point
        delay(500)  // coroutine can be descheduled here
        logger.info("message done") // thread 2
    }

    suspend fun listenToMusic() {
        logger.info("music started") // thread 2
        delay(1000)
        logger.info("Music off") // thread 2
    }

    suspend fun takeAShower() {
        logger.info("water on")
        delay(2000)
        logger.info("shower off")
    }

    suspend fun writeWhileMusic() {
        coroutineScope {
            // launch coroutines
            // launch = coroutine builder
            launch { sendMessage() }
            launch { listenToMusic() }
        }
    }

    // cooperative scheduling
    // semantic blocking

    suspend fun writeSequentially() {
        coroutineScope {
            sendMessage()
            listenToMusic()
        }
    }

    suspend fun writeParallelWithHandle() {
        coroutineScope {
            val messageJob = launch { sendMessage() }
            val musicJob = launch { listenToMusic() }
            // handle to manipulate the coroutine
            messageJob.join() // semantically blocking
            musicJob.join()
        }

        // the entire previous coroutine scope must close before another starts
        // i.e. all child coroutines are done

        coroutineScope {
            launch { takeAShower() }
        }
    }

    // dispatchers = thread pools for coroutines
    // Dispatchers.Default - "normal" coroutines
    // Dispatchers.IO - blocking coroutines

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun workOnMyDispatcher() {
        val dispatcher: CoroutineDispatcher = Dispatchers.IO.limitedParallelism(10)
        coroutineScope {
            // runs on the dispatcher
            launch(dispatcher) { takeAShower() }
        }
    }

    suspend fun blockingShower() {
        coroutineScope {
            runBlocking { takeAShower() } // will run on the IO dispatcher
        }
    }

    // async
    suspend fun buyFriendPresent(friend: String): String {
        logger.info("out at the mall")
        delay(2000)
        logger.info("got a present for $friend")
        return "Guitar"
    }

    suspend fun multipleRoutine() {
        coroutineScope {
            launch { sendMessage() }
            launch { listenToMusic() }
        }

        coroutineScope {
            val presentPromise = async { buyFriendPresent("Jon") }
            val present = presentPromise.await() // semantically blocking
        }
    }

    // AutoCloseable = management of resources
    class Desk: AutoCloseable {
        init {
            logger.info("using desk")
        }
        override fun close() {
            logger.info("releasing desk resources")
        }
    }

    suspend fun workNicely() {
        while(true) {
            // remember to yield
            logger.info("working...")
            delay(500)
        }
    }

    suspend fun forgotPresent() {
        coroutineScope {
//            val workingJob = launch {
//                val desk = Desk()
//                workNicely()
//                // I forgot to close the desk - resource leak!
//            }

            val workingJob = launch {
                Desk().use { desk ->
                    workNicely()
                } // desk calls close() when job finishes (success, failure, cancellation)
            }

            launch {
                delay(3000)
                logger.info("I forgot present!")
                workingJob.cancel() // non-blocking
                workingJob.join()
                // can combine cancel + join => cancelAndJoin()
                buyFriendPresent("Jon")
            }
        }
    }
}

suspend fun main(args: Array<String>) {
    Coroutines.forgotPresent()
}
