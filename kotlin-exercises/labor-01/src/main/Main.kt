package main

import kotlin.random.Random

fun main(args: Array<String>) {
    println("1. Write a main function that adds two values (immutable variables) and prints the result using a\n" +
            "String template in the following format: 2 + 3 = 5.")
    val num1 = 2
    val num2 = 4
    println("$num1 + $num2 = ${num1 + num2}")
    println("\n")
    println("2. Write a main function that declares an immutable list (listOf) daysOfWeek containing the\n" +
            "days of the week.\n" +
            "● Use a for loop that iterates over the list and prints the list to the standard output.\n")
    val daysOfWeek = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
//    for (i in daysOfWeek){
//        println(i)
//    }
    daysOfWeek.forEach { t -> println(t) }
    println("\n")
    println("● Use a list filter to print the days starting with letter ‘T’\n")
    daysOfWeek
        .filter { it.startsWith("T") }
        .forEach { println(it) }
    println("\n")
    println("● Use a list filter to print the days containing the letter ‘e’\n")
    daysOfWeek
        .filter { it.contains("e") }
        .forEach { println(it) }
    println("\n")
    println("● Use a list filter to print all the days of length 6 (e.g. Friday)\n")
    daysOfWeek
        .filter { it.length == 6 }
        .forEach { println(it) }
    println("\n")
    println("3. Write a function that checks whether a number is prime or not. Write a main function that\n" +
            "prints prime numbers within a range.\n")
    val num3 = 7
    if(isPrime(num3) == true){
        println("The number is: $num3, and it is a prime number!")
    } else {
        println("The number is: $num3, and it is not a prime number!")
    }
    val num4 = 1
    val num5 = 20
    println("The primes in range $num4 and $num5 is: ")
    primesInRange(num4, num5)
    println("\n")
    println("4. Write an encode and a corresponding decode function that encodes and respectively\n" +
            "decodes the characters of a string. You may use any encoding strategy.\n")
    val originalMessage = "Hello World!"
    val encodedMessage = messageCoding(originalMessage, ::encode)
    println("Encoded Message: $encodedMessage")
    val decodedMessage = messageCoding(encodedMessage, ::decode)
    println("Decoded Message: $decodedMessage")
    println("\n")
    println("5. Write a compact function that prints the even numbers from a list. Use a list filter!")
    val num6 = 6
    if(isEvenCompact(num6) == true){
        println("The number is: $num6, and it is an even number!")
    } else {
        println("The number is: $num6, and it is not an even number!")
    }
    println("The even numbers in 1-10: ")
    val numbers = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    printEvenNumbers(numbers)
    println("\n")
    println("6. The map() performs the same transformation on every list item and returns the result list.\n" +
            "Using map, perform the following operations:\n" +
            "● Double the elements of a list of integers and print it.\n")
    val list = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    doubleElements(list)
    println("\n")
    println("● Print the days of week capitalized (e.g. MONDAY for Monday)\n")
    capitalizedLetters(daysOfWeek)
    println("\n")
    println("● Print the first character of each day capitalized (e.g. m for Monday)\n")
    firstLetter(daysOfWeek)
    println("\n")
    println("● Print the length of days (number of characters, e.g. Monday → 6)\n")
    sizeLetters(daysOfWeek)
    println("\n")
    println("● Compute the average length of days (in number of characters)\n")
    println(averageLength(daysOfWeek))
    println("\n")
    println("7. Mutable lists.\n" +
            "● Convert the daysOfWeek immutable list into a mutable one. Remove all days containing\n" +
            "the letter ‘n’, then print the mutable list.\n")
    val daysOfWeek2 = mutableListOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
    daysOfWeek2
        .removeAll{ it.contains("n", ignoreCase = true) }
    println(daysOfWeek2)
    println("\n")
    println("● Print each element of the list in a new line together with the index of the element (convert\n" +
            "the list to list with index using the withIndex() function!)\n")
    daysOfWeek2.withIndex().forEach { (index, day) ->
        println("Item at " + index + " is " + day)
    }
    println("\n")
    println("● Sort the result list alphabetically!\n")
    daysOfWeek2
        .removeAll{ it.contains("n", ignoreCase = true) }
    daysOfWeek2.sort()
    println(daysOfWeek2)
    println("\n")
    println("8. Arrays.\n" +
            "● Generate an array of 10 random integers between 0 and 100, and use forEach to print\n" +
            "each element of the array in a new line.\n")
    val randomNumbers = IntArray(10) { Random.nextInt(0, 101) }
    randomNumbers.forEach { println(it) }
    println("\n")
    println("● Print the array sorted in ascending order!\n")
    val sortedNumbers = randomNumbers.sorted()
    sortedNumbers.forEach { println(it) }
    println("\n")
    println("● Check whether the array contains any even number!\n")
    val evenNumbers = randomNumbers.filter { it % 2 == 0 }
    if (evenNumbers.isNotEmpty()) {
        println("The array contains at least one even number.\n")
        evenNumbers.forEach { println(it) }
    } else {
        println("The array does not contain any even numbers.\n")
    }
    println("\n")
    println("● Check whether all the numbers are even!\n")
    val allEven = randomNumbers.all { it % 2 == 0 }
    randomNumbers.forEach { println(it) }
    if (allEven){
        println("All numbers is even!\n")
    } else {
        println("Not all numbers are even!\n")
    }
    println("\n")
    println("● Calculate the average of generated numbers and print it using forEach!\n")
    val average = randomNumbers.average()
    listOf(average).forEach { println("The average of the generated numbers is (using forEach): $it") }
}

fun isPrime(num: Int): Boolean {
    if (num <= 1) {
        return false
    }
    for (i in 2..Math.sqrt(num.toDouble()).toInt()) {
        if (num % i == 0) {
            return false
        }
    }
    return true
}

fun primesInRange(start: Int, end: Int) {
    for (num in start..end) {
        if (isPrime(num)) {
            println("$num is a prime number")
        }
    }
}

fun encode(message: String): String {
    return message.map { char ->
        if (char.isLetter()) {
            val shift = if (char.isLowerCase()) 'a' else 'A'
            (shift + (char - shift + 3) % 26).toChar()
        } else {
            char
        }
    }.joinToString("")
}

fun decode(message: String): String {
    return message.map { char ->
        if (char.isLetter()) {
            val shift = if (char.isLowerCase()) 'a' else 'A'
            (shift + (char - shift - 3 + 26) % 26).toChar()
        } else {
            char
        }
    }.joinToString("")
}

fun messageCoding(msg: String, func: (String) -> String): String {
    return func(msg)
}

fun isEvenCompact(number : Int) : Boolean = number % 2 == 0
fun printEvenNumbers(numbers: List<Int>) = numbers.filter { it % 2 == 0 }.forEach { println(it) }
fun doubleElements(numbers: List<Int>) = numbers.map { it * 2 }.forEach{ println(it) }
fun capitalizedLetters(days: List<String>) = days.map { it.uppercase() }.forEach { println(it) }
fun firstLetter(days: List<String>) = days.map { it.first().lowercase() }.forEach{ println(it)}
fun sizeLetters(days: List<String>) = days.map { it.length }.forEach{ println(it) }
fun averageLength(days: List<String>): Double = days.map { it.length }.average()