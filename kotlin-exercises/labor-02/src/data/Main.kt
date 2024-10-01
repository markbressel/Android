package data

import kotlin.Comparator as Comparator

fun main(){
    val today = Date()
    println(today)

    val leap = today.isLeapYear()
    val leap2 = Date(2023, 4, 12)
    val valid = today.isValidDate()
    val valid2 = Date(2023, 2, 29)

    println("Today is: $today is a leap year: $leap")
    println("The date is: $leap2 is a leap year: ${leap2.isLeapYear()}")
    println("Today is: $today is valid: $valid")
    println("The date is: $valid2 is valid: ${valid2.isValidDate()}")

    val validDates = mutableListOf<Date>()
    var count = 0
    while (validDates.size < 10){
        val randomDate = randomDates()
        if(randomDate.isValidDate()){
            validDates.add(randomDate)
        } else {
            println("\nThis is an invalid date: $randomDate")
            count++
        }
    }
    println("\nThe unsorted valid dates: ")
    validDates.forEach { println(it) }
    println("\nInvalid dates generated: $count")

    val sortedDates = validDates.sorted()
    println("\nSorted by natural ordering:")
    sortedDates.forEach { println(it) }
    val reverseDates = sortedDates.reversed()
    println("\nReversed sorted dates:")
    reverseDates.forEach { println(it) }
    val customSort = validDates.sortedWith(Comparator { d1, d2 ->
        when {
            d1.month != d2.month -> d1.month - d2.month
            d1.day != d2.day -> d1.day - d2.day
            else -> d1.year - d2.year
        }
    })
    println("\nCustom sorted dates (by month):")
    customSort.forEach { println(it) }
}