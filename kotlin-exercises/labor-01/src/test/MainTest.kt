package test

import org.testng.Assert.assertEquals
import org.testng.annotations.Test
import org.testng.Assert.assertTrue as assertTrue1

class AnagramsGrouperTest {
    @Test
    fun threeGroupsAllLowerCase() {
        val anagrams = groupAnagrams(listOf("eat", "tea", "tan", "ate", "nat","bat").toTypedArray())
        assertEquals(3, anagrams.size)
        assertTrue1(anagrams.contains(listOf("eat", "tea", "ate")))
        assertTrue1(anagrams.contains(listOf("tan", "nat")))
        assertTrue1(anagrams.contains(listOf("bat")))
    }
    @Test
    fun threeGroupsSomeUpperCase() {
        val anagrams = groupAnagrams(listOf("eat", "tEa", "Tan", "atE", "NAT","bat").toTypedArray())
        assertEquals(3, anagrams.size)
        assertTrue1(anagrams.contains(listOf("eat", "tea", "ate")))
        assertTrue1(anagrams.contains(listOf("tan", "nat")))
        assertTrue1(anagrams.contains(listOf("bat")))
    }
    @Test
    fun validOneGroup() {
        val anagrams = groupAnagrams(listOf("eat").toTypedArray())
        assertEquals(1, anagrams.size)
        assertTrue1(anagrams.contains(listOf("eat")))
    }
    @Test
    fun noGroup() {
        val anagrams = groupAnagrams(emptyList<String>().toTypedArray())
        assertEquals(0, anagrams.size)
    }
}