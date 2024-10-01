package extensions

fun main(){
    println("Define an extension function that prints the monogram of a String containing the\n" +
            "firstname and lastname. Example: John Smith → JS\n")
    val name = "Bressel Mark"
    println(name.monogram())
    println("\n")
    println("Define a compact extension function that returns the elements of a strings’ list joined by\n" +
            "a given separator!\n")
    val fruits = listOf("apple", "pear", "melon")
    println(fruits.joinWithSeparator("#"))
    println("\n")
    println("Define a compact extension function for a strings’ list that returns the longest string!\n")
    val words = listOf("apple", "banana", "watermelon", "pear")
    println(words.longest())
}