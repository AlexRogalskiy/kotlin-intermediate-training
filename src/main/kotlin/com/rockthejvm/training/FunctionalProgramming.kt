package com.rockthejvm.training

object FunctionalProgramming {

    // referential transparency
    fun aPureFunction(a: Int, b: Int) = a + b

    // impure
    fun showMeTheMoney(money: Int): Int {
        println("here's your cash, Godfather")
        return money * 110/100
    }

    val smallLoan = showMeTheMoney(1000)
    val bigLoan = showMeTheMoney(1000) + showMeTheMoney(1000)

    /*
        side effects = things that happen outside normal computation
        examples
            - consoles
            - logging
            - UI displays
            - variable change (big)
            - calling other services (async etc)
     */

    fun tenxNumber(n: Int) = n * 10

    val tenx = fun(n: Int): Int { return n * 10 } // anonymous function
    val hundred = tenx(10)

    // syntax sugar for:
    abstract class MyFunction<A, B> {
        abstract operator fun invoke(a: A): B
    }

    val tenxFun = object: MyFunction<Int, Int>() {
        override operator fun invoke(a: Int): Int = a * 100
    }
    val hundred_v2 = tenxFun(10)
    val tenxFun_v2 = { x: Int -> x * 10 } // syntax sugar for tenx

    // pass functions as arguments
    val numbers = listOf(1,2,3,4,5)
    val numbersx10 = numbers.map(tenx) // [10,20,30,40,50]
    val numbersx10_v2 = numbers.map({ x: Int -> x * 10 }) // same
    val numbersx10_v2_sugar = numbers.map { x: Int -> x * 10 } // same, cut ()
    val numbersx10_v2_noType = numbers.map { x -> x * 10 } // same, type omitted
    val numbersx10_v3 = numbers.map { it * 10 } // same

    fun concatenate(n: Int): String {
        var result = ""
        for (i in 1..n)
            result += "kotlin"
        return result
    }

    val concatenateKotlin = { x: Int -> concatenate(x) }
    val kotlinRepetitions = numbers.map(concatenateKotlin)
    // ["kotlin", "kotlinkotlin"...]

    // filter
    val lessThan3 = numbers.filter { it < 3 } // [1,2]

    /**
     * TODO
     *  - list of strings, return their lengths
     *      ["kotlin", "is", "cool"] -> [6,2,4] - use map
     *  - two lists of numbers of the same size, return sum of corresponding elements
     *      [1,2,3,4], [5,6,7,8] -> [6,8,10,12] - zip
     *  - two lists of things, return all combinations as strings
     *      [1,2,3], ["a", "b", "c"] -> ["1a", "1b", "1c", "2a" ....] - flatMap
     *  - list of strings, return the concatenation of all strings
     *      ["kotlin", "is", "cool"] -> "kotliniscool"
     *      - reduce
     *      - fold
     *  - concatenate "kotlin" n times.
     */

    // 1 -
    val strings = listOf("kotlin", "is", "cool")
    val listLengths = strings.map { it.length }
    // 2 -
    val numbers1 = listOf(1,2,3,4)
    val numbers2 = listOf(5,6,7,8)
    val sums = numbers1.zip(numbers2).map { it.first + it.second }
    val sums_v2 = numbers1.zip(numbers2, { a, b -> a + b }) // "correct" call
    val sums_v2_sugar = numbers1.zip(numbers2) { a, b -> a + b } // same
    // 3 -
    val chars = listOf("a", "b", "c")
    val combinations = numbers1.flatMap { number ->
        chars.map { char -> number.toString() + char }
    }
    val combinations_v2 = numbers1 // list<int>
        .map { a ->  // int -> list<int>
            chars.map { b -> "$a$b" }
        } // list<list<int>>
        .flatMap { it /* <- list<int> */} // list<int>

    // 4 -
    val statement = strings.reduce { acc, elem -> acc + elem }
    val statement_v2 = strings.fold("") { acc, elem -> acc + elem }
    // 5 -
    val kotlinxn = { n: Int -> (1..n).fold("") { acc, _ -> acc + "Kotlin" } }
    val kotlinxn_v2 = { n: Int -> (1..n).map { "Kotlin" }.joinToString("") }

    @JvmStatic
    fun main(args: Array<String>) {
        println(listLengths)
        println(combinations)
        println(statement)
        println(statement_v2)
        println(kotlinxn(5))
        println(kotlinxn_v2(5))
    }

}
