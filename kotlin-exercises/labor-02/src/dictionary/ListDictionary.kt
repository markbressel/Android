package dictionary

import java.io.File

object ListDictionary : IDictionary {

    private val words : MutableList<String> = mutableListOf()

    init {
        File(IDictionary.DICTIONARY_FILE).forEachLine { words.add(it) }
    }

    override fun add(word: String): Boolean {
        return if(!words.contains(word)){
            words.add(word)
            true
        } else {
            false
        }
    }

    override fun find(word: String): Boolean {
        return words.contains(word)
    }

    override fun size(): Int {
        return words.size
    }
}