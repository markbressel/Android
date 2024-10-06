import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import test.TextGenerator

class TextGeneratorTest {

    private lateinit var generator: TextGenerator

    @BeforeEach
    fun setUp() {
        val text = "Now is not the time for sleep, now is the time for party!"
        generator = TextGenerator(text)
        generator.learnWords()
    }

    @Test
    fun testGenerateText() {
        val generatedText = generator.generateText()
        assertTrue(generatedText.contains("time"), "Generated text does not contain expected word 'time'.")
        assertTrue(generatedText.contains("party"), "Generated text does not contain expected word 'party'.")
    }

    @Test
    fun testRepeatedWordPairs() {
        val repeatedText = "the cat and the cat"
        val repeatedGenerator = TextGenerator(repeatedText)
        repeatedGenerator.learnWords()
        val generatedText = repeatedGenerator.generateText()
        assertTrue(generatedText.contains("the cat"), "Generated text did not follow correct word pairs")
    }

    @Test
    fun testEmptyInput() {
        val emptyGenerator = TextGenerator("")
        emptyGenerator.learnWords()
        val generatedText = emptyGenerator.generateText()
        assertEquals("", generatedText, "Generated text for empty input should also be empty.")
    }

    @Test
    fun testSingleWordInput() {
        val singleWordGenerator = TextGenerator("Hello")
        singleWordGenerator.learnWords()
        val generatedText = singleWordGenerator.generateText()
        assertEquals("Hello", generatedText, "Generated text for single word input should return that word.")
    }
}