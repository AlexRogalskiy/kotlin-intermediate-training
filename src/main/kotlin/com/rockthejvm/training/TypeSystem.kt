package com.rockthejvm.training

object TypeSystem {

    // type bounds
    open class Car {
        fun run(): String = "vroom!"
    }
    class Supercar: Car()
    interface OffRoad
    class SUV: Car(), OffRoad
    class MonsterTruck: Car(), OffRoad

    class Garage<A: Car>(a: A) { // A must be a subtype of Car
        val statement = a.run() // can only use the most general API
    }
    val garage = Garage(Supercar()) // ok
    // val garageInvalid = Garage<String>() // ok

    fun <A: Car> makeSpecialGarage(a: A): Garage<A> = TODO()
    //  ^^^^^^^^ can only have one type bound

    fun <A> makeSpecialGarage(a: A): Garage<A>
    where A: Car, A: OffRoad =
        Garage(a) // implementation

    /*
        Variance
        Variance question: If A "extends" B, should List<A> "extend" List<B>?
            for List, YES -> List is COVARIANT ("producers" of A, "outputs" A)

        alternative answer: NO -> Thing is INVARIANT
            example: array, mutable list

        alternative answer: HELL NO -> Thing is CONTRAVARIANT ("consumes" A)
            example:
     */

    class MyList<out A> {
    //           ^^^ MyList is COVARIANT in A
        fun get(index: Int): A = TODO()
    }

    open class Animal
    class Dog(name: String): Animal()
    class Cat: Animal()

    val dogs: List<Dog> = listOf(Dog("lassie"), Dog("hachiko"))
    val animals: List<Animal> = dogs // substitution is possible

    class Vet<in A> {
        //    ^^ Vet is CONTRAVARIANT in A
        fun heal(a: A): Boolean = true // impl not important
    }

    val dog: Dog = Dog("laika")
    val vet: Vet<Dog> = Vet<Animal>() // Vet<Animal> subtype of Vet<Dog>
    val canHeal = vet.heal(dog)

    // example:
    interface JSONSerializer<in A> {
        fun serialize(a: A): String = "{...}"
    }

}