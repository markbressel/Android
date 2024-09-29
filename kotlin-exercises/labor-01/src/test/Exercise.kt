package test
fun main(){
    val anag1 = groupAnagrams(arrayOf("eat", "tea", "tan", "ate", "nat", "bat"))
    println(anag1)

    val anag2 = groupAnagrams(arrayOf("eat", "tEa", "Tan", "atE", "NAT", "bat"))
    println(anag2)
}
fun groupAnagrams(strs: Array<String>): List<List<String>> {
    val anagramGroups = mutableMapOf<String, MutableList<String>>()
    for (str in strs){
        val sortedStr = str.toLowerCase().toCharArray().sorted().joinToString("")
        anagramGroups.computeIfAbsent(sortedStr) { mutableListOf() }.add(str.toLowerCase())
    }
    return anagramGroups.values.toList()
}