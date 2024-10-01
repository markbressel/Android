package data
import java.time.LocalDate
import java.util.Random
data class Date(
    val year: Int = LocalDate.now().year,
    val month: Int = LocalDate.now().monthValue,
    val day: Int = LocalDate.now().dayOfMonth
) : Comparable<Date> {
    override fun compareTo(other: Date): Int {
        return when{
            this.year != other.year -> this.year - other.year
            this.month != other.month -> this.month - other.month
            else -> this.day - other.day
        }
    }
}
fun Date.isValidDate() : Boolean {
    val dayIsOk = return when(month){
        1, 3, 5, 7, 8, 10, 12 -> day in 1..31
        4, 6, 9, 11 -> day in 1..30
        2 -> day in 1..nrOfDaysInFebruary()
        else -> false
    }
    return dayIsOk
}
fun Date.isLeapYear() : Boolean {
    return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
}
fun Date.nrOfDaysInFebruary() : Int {
    return if (this.isLeapYear()) 29 else 28
}
fun randomDates(): Date {
    val random = Random()
    val year = random.nextInt(1500, 2025)
    val month = random.nextInt(1, 13)
    val day = random.nextInt(1, 32)
    return Date(year, month, day)
}