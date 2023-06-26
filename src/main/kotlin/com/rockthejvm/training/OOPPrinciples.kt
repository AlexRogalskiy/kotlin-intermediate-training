package com.rockthejvm.training

import java.lang.IllegalArgumentException

object OOPPrinciples {

    // class
    class Person(val name: String, val age: Int) { // primary constructor
        // init block runs after constructor, can run checks
        init {
            assert(name.isNotEmpty())
        }
        // data = fields
        // behavior = methods
        fun greet(): String =
            "Hi, my name is $name"

        // overloading = more methods with one name, different signatures (argument list)
        fun greet(someone: String): String =
            "Hi $someone, I'm $name"

        // infix notation, for methods with ONE ARGUMENT
        infix fun likes(movie: String) =
            "$name says: I like $movie"

        // secondary constructor
        constructor(name: String): this(name, 0) // must call another constructor

        companion object {
            // can define fields & methods that do not depend on a particular Person instance
            val N_HANDS = 2
            fun canFly(): Boolean = false // impl not important
        }
    }

    val daniel = Person("Daniel", 99) // instance of Person ("object")
    val daniel_v2 = Person("Daniel") // calling secondary constructor
    // type inference, strong typing
    // access data
    val danielsName = daniel.name
    // call methods
    val danielsGreeting = daniel.greet()
    // infix call
    val danielLikesMovie = daniel.likes("Forrest Gump") // normal syntax
    val danielLikesMovie_v2 = daniel likes "Forrest Gump" // infix syntax, same call
    //                        ^^      ^^    ^^
    //                      instance method argument


    class Vector2D(val x: Double, val y: Double) {
        infix operator fun plus(another: Vector2D) = // plus, minus, times, divide, rem
            Vector2D(x + another.x, y + another.y)
        infix operator fun plus(amount: Double) =
            Vector2D(x + amount, y)

        operator fun get(index: Int) =
            when (index) {
                0 -> x
                1 -> y
                else -> throw IllegalArgumentException("out of bounds index")
            }

        operator fun inc(): Vector2D = // same with dec()
            Vector2D(x + 1, y)
    }

    val vector1 = Vector2D(1.0, 2.0)
    val vector2 = Vector2D(0.0, 1.0)
    val vectorSum = vector1 + vector2 // equivalent to vector1.plus(vector2)
    val vector3 = vector1 + 3.0
    val vector1_x = vector1[0] // equivalent to vector1.get(0)

    // vector1++ // equivalent to vector1 = vector1.inc()

    /**
        objects, singletons and companions
      */
    object MyObject { // type + the only instance of this type
        // can have fields and methods
        val aField = 42
        fun aMethod(): Int {
            println("hello from object")
            return 42
        }
    }

    // companion objects = object defined in a class
    val canFly = Person.canFly() // "static" method
    val generalNHands = Person.N_HANDS

    /**
     * Inheritance
     *
     */

    open class Animal { // parent class
        open fun eat() { // open methods can be overridden
            println("I am eating, naf, naf")
        }
    }

    open class Dog: Animal() { // child/derived class
        // overriding = giving another implementation to parent method
        // overloading != overriding
        override fun eat() {
            super.eat() // calling parent method
            println("crunching woof woof")
        }
    }

    class Puppy: Dog() {
        override fun eat() {
            println("chit chit")
        }
    }

    // can use "final" to restrict inheritance (for classes or methods)
    // the "sealed" modifier restricts inheritance to this file only

    // polymorphism
    val anAnimal: Animal = Puppy() // Dog IS AN Animal

    // abstract classes
    abstract class Plant {
        abstract fun grow(): String
        companion object { // can add companions in abstract classes too
            val BASIC_NEEDS = listOf("earth", "water")
        }
    }

    class Strawberry: Plant() {
        override fun grow(): String = "yummy"
        // all abstract fields & methods MUST be implemented
    }

    val aPlant: Plant = Strawberry() // polymorphism works the same


    // interfaces = ultimate abstract types
    interface Carnivore { // describe functionality/behavior
        // all methods assumed abstract
        fun eat(animal: Animal): String
    }

    interface Herbivore {
        fun eat(plant: Plant): String
    }

    // can extend ONE class, multiple interfaces
    class Crocodile: Animal(), Carnivore {
        override fun eat(animal: Animal): String = "eating this poor sucker"
    }

    class Human: Carnivore, Herbivore {
        override fun eat(animal: Animal): String = "eating animal"
        override fun eat(plant: Plant): String = "eating plant"
    }

    // interfaces are behaviors/"traits"
    // classes are "things"/data

    // access modifiers = allow referring to fields/methods from certain places
    // default = public = can call from any place in the code
    // protected = can call from class & hierarchy
    // private = can call from class only
    // internal = can call from the same compiler module (used mostly for libraries)
    // can increase protection level across hierarchy (only increase, usually protected -> public)

    // data classes = lightweight data structures
    class CityNaive(val name: String, val population: Int, val country: String) {
        override fun equals(other: Any?): Boolean {
            val otherCity = other as? CityNaive
            return if (otherCity == null) false
            else name == otherCity.name && population == otherCity.population && country == otherCity.country
        }
    }

    /*
        Data class features
        - equals, hashCode, toString automatically implemented
        - copy, componentN functions
        - destructuring

        Data class restrictions:
            - cannot inherit from data class
            - must have at least one constructor arg
            - all constructor args must be fields (val, var)
     */
    data class City(val name: String, val population: Int, val country: String)

    fun demoEquals() {
        val bucharest = CityNaive("Bucharest", 2000000, "Romania")
        val bucharest_v2 = CityNaive("Bucharest", 2000000, "Romania")
        println(bucharest == bucharest_v2) // bucharest.equals(bucharest_v2)
    }

    fun demoDataClasses() {
        val bucharest = City("Bucharest", 2000000, "Romania")
        val bucharestNewPop = bucharest.copy(population = 1980000) // returns NEW INSTANCE
        val (name, pop, country) = bucharest // tuple destructuring, Python style
    }

    /**
        anonymous classes = classes instantiated on the spot, single use
    */
    val specialPlant: Plant = object:Plant() { // <- anonymous class
        override fun grow(): String = "weird flowers"
    }

    /*
        equivalent to:

        object MyPlant: Plant() {
                override fun grow(): String = "weird flowers"
        }
        val specialPlant: Plant = MyPlant
     */


    /**
     * Nested & inner classes
     */
    class Outer {
        val myField = 4
        // needs some impl details wrapped in another class
        class Nested { // Java: static class
            val nestedField = 42
        }

        inner class Inner { // Java: "normal" class inside another class
            val innerField = myField + 38
        }
    }

    val outer = Outer()
    val nested = Outer.Nested() // does NOT need an instance of Outer
    // val inner = Outer.Inner() // cannot do that, needs Outer instance
    val inner = outer.Inner()

    class MySuperBigService {
        // my cloud service
        fun login(person: Person): Boolean = false // impl not important

        // make a class hierarchy just for the service impl
        open class Role(name: String)
        class Admin: Role("ADMIN")
        // Employee, Recruiter, Guest...
    }

    // value class
    // wearable apps
    // boxing = wrapping simple types in another class
    @JvmInline value class ProductCode(val code: Int)
    @JvmInline value class CategoryCode(val code: Int)
    @JvmInline value class CountryCode(val code: Int)
    data class Product(val code: ProductCode, val category: CategoryCode, val country: CountryCode)

    fun identifyProduct(code: Int, category: Int, country: Int): Product =
        Product(ProductCode(code), CategoryCode(category), CountryCode(country))
    //          ^^^^^^^^^^^^^^^^^ will use an Int, will not create a new class instance

    data class ProductNaive(val code: Int, val category: Int, val country: Int)
    val product = ProductNaive(32, 45, 999) // can easily lead to bugs because of arg swaps
    val product_v2 = Product(
        ProductCode(32),
        CategoryCode(32),
        CountryCode(999)
    ) // better type safety
    /*
        Restriction:
            - only one constructor arg, must be val
            - no other fields
     */

    // generics - use the same code on multiple (different) types
    abstract class IntList {
        abstract fun head(): Int
        abstract fun tail(): IntList
    }
    // same for Int, Double, String, Person, Any..

    abstract class MyList<A> {
        // can use A as a type inside the class
        abstract fun head(): A
        abstract fun tail(): MyList<A>
    }

    class Empty<A>: MyList<A>() {
        override fun head(): A = throw NoSuchElementException()
        override fun tail(): MyList<A> = throw NoSuchElementException()
    }

    class NonEmpty<A>(val h: A, val t: MyList<A>): MyList<A>() {
        override fun head(): A = h
        override fun tail(): MyList<A> = t
    }

    val numbers: MyList<Int> = NonEmpty(1, NonEmpty(2, Empty()))

    // TODO talk variance

    // collections
    fun demoCollections() {
        // list = linear collection, can "get" an element at an index
        val aList = listOf(1,2,3,4) // immutable list
        val secondElement = aList[1] // 0-indexing

        // mutable list
        val aMutableList = mutableListOf(1,2,3,4) // can change elements
        aMutableList.add(5) // add last
        aMutableList.add(0,-1) // add at an index

        // other functions
        val listLength = aList.size
        val find4 = aList.indexOf(4)
        val has4 = aList.contains(4) // true
        val has4_v2 = 4 in aList // same
        val subl = aList.subList(1, 3) // [2,3]

        // array - map onto JVM (then native) arrays
        val anArray = arrayOf(1,2,3) // mutable by definition\
        val secondElemA = anArray[1] // get
        anArray[1] = 100 // set

        // set - contains unique values
        // fundamental API - add an element, test if an element is in the set
        val aSet = setOf(1,2,3,4,1,2,3) // [1,2,3,4] - immutable set
        val setHas3 = 3 in aSet // usually much faster than list
        val newSet = aSet + 5 // aSet.plus(5), returns another set
        // same with -, +/- with sets as arguments
        // similar API for mutable set

        // maps - associations between unique things (keys) and values
        val aMap = mapOf(
            Pair("Daniel", 123),
            "Alice" to 456, // == Pair("Alice", 456)
        )
        val danielInSet = "Daniel" in aMap

        // can convert between collections
        val numbersSet = aList.toSet().toList()
    }

    // extension methods
    fun concatenate(str: String, times: Int): String {
        var result: String = ""
        for (i in 1..times) result += str
        return result
    }

    infix operator fun Int.times(str: String): String {
        var result: String = ""
        for (i in 1..this) result += str
        return result
    }

    val kotlinx3 =  3 * "Kotlin" // = "KotlinKotlinKotlin"


    infix operator fun <A> Int.times(list: List<A>): List<A> {
        val result: MutableList<A> = mutableListOf()
        for (i in 1..this) result.addAll(list)
        return result
    }

    val numbersx3 = 3 * listOf(1,2,3)

    infix operator fun Int.times(another: Int) = this + another


    @JvmStatic
    fun main(args: Array<String>) {
        // !!
        // can only use Animal API (declared type), the most derived method gets run at RUNTIME (concrete type)
        anAnimal.eat()
        println(aPlant.grow())

        // equals test
        demoEquals()

        println(9.times(9))
    }
}

// public static void main(String[] args) =>

// object thing {
//   main(args: Array<String>): Unit
// }
//