package test
class TextGenerator(private val text: String) {
    val prefixPostfixMap = mutableMapOf<Pair<String, String>, MutableList<String>>()

    fun learnWords() {
        val words = text.split(" ")
        if (words.size < 3) return
        for (i in 0 until words.size - 2) {
            val prefix = Pair(words[i], words[i + 1])
            val postfix = words[i + 2]
            prefixPostfixMap.computeIfAbsent(prefix) { mutableListOf() }.add(postfix)
        }
    }

    fun generateText(): String {
        val words = text.split(" ")
        if (words.isEmpty()) return ""
        if (words.size == 1) return words[0]

        val generatedWords = mutableListOf<String>()
        var currentPrefix = Pair(words[0], words[1])
        generatedWords.add(currentPrefix.first)
        generatedWords.add(currentPrefix.second)
        println("1. ${currentPrefix.first} ${currentPrefix.second}")

        var step = 2
        while (true) {
            val possiblePostfixes = prefixPostfixMap[currentPrefix] ?: break
            val nextWord = possiblePostfixes.random()
            generatedWords.add(nextWord)
            println("$step. ${generatedWords.joinToString(" ")}")
            step++
            currentPrefix = Pair(currentPrefix.second, nextWord)
            if (currentPrefix.first == words[words.size - 2] && currentPrefix.second == words[words.size - 1]) {
                break
            }
        }
        return generatedWords.joinToString(" ")
    }
}