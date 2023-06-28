package com.rockthejvm.training

import java.util.concurrent.Executors

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




    @JvmStatic
    fun main(args: Array<String>) {

    }
}