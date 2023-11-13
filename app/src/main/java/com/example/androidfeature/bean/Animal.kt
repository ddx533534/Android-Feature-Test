package com.example.androidfeature.bean

open class Animal

class Dog : Animal()
class Cat : Animal()

interface Box<out T> {
    fun get(): T
}

interface Cage<in T> {
    fun put(input: T)
}

fun testOut() {
    val dogBox = object : Box<Dog> {
        override fun get(): Dog {
            return Dog()
        }
    }
    val catBox = object : Box<Cat> {
        override fun get(): Cat {
            return Cat()
        }
    }
    // 将 Box<Dog>实例 赋值给 Box<Animal> 变量 - 协变，编译通过
    var animalBox: Box<Animal> = dogBox
    // 狗箱子取出的是🐶 - 协变允许我们安全地从箱子中取出狗对象并"向上转型"为Animal
    var animal: Animal = animalBox.get()
    println(animal)

    animalBox = catBox
    // 猫箱子取出的是🐱 - 协变允许我们安全地从箱子中取出狗对象并"向上转型"为Animal
    animal = animalBox.get()
    println(animal)
}

fun testIn() {
    val animalCage = object : Cage<Animal> {
        override fun put(input: Animal) {
            println(input)
        }
    }

    val dogCage: Cage<Dog> = animalCage
    dogCage.put(Dog())
    val catCage: Cage<Cat> = animalCage
    catCage.put(Cat())
}

fun main() {

}
