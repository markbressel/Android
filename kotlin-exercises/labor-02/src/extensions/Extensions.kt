package extensions

fun String.monogram() : String{
    return this
        .split(" ")
        .map { it.first().uppercaseChar() }
        .joinToString ("")
}
fun List<String>.joinWithSeparator(separator: String): String = joinToString(separator)
fun List<String>.longest(): String? = maxByOrNull { it.length }