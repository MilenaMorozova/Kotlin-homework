import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.io.File

class Game {
    private var words = File("src\\main\\words.txt").readLines()
    private val composedWordFilename = "src\\main\\your_words.txt"

    private fun randomWord(): String = words.filter { it.length >= 8 }.random()

    private fun checkWordForValidChars(startWord: String, composedWord: String): Boolean {
        var startWord = startWord
        for(letter in composedWord){
            if (startWord.indexOf(letter) == -1){
                println("Не все буквы вашего слова имеются в стартовом слове!\n")
                return false
            }
            startWord = startWord.replaceFirst(letter.toString(), "")
        }
        return true
    }

    private fun writeWordsToFile(words: MutableList<String>){
        val text = words.toString().replace(", ", "\n").removePrefix("[").removeSuffix("]")
        File(composedWordFilename).writeText(text)
    }

    private fun countPoint(word: String): Int{
        var points = 0
        if (words.contains(word)){
            points += word.length
        }
        else{
            println("Слова $word нет в словаре")
        }
        return points
    }

    fun mainLoop(){
        val startWord = randomWord()

        println("Стартовое слово: $startWord")
        println("Напишите 'Конец' для подсчёта очков.\n" +
                "Пишите новое слово на новой строке. Буквы не повторяются. Удачи!\n")
        println("Write your words:\n")
        val composedWords: MutableList<String> = arrayListOf()

        while(true){
            val composedWord = readLine()
            if (composedWord.isNullOrEmpty()){
                println("Введите слово или 'Конец'\n")
                continue
            }
            if (composedWord == "Конец"){
                writeWordsToFile(composedWords)
                break
            }
            if (checkWordForValidChars(startWord, composedWord)){
                composedWords.add(composedWord)
            }
        }
        //count points
        val points = composedWords.map { word ->
            GlobalScope.async {
                countPoint(word)
            }
        }

        runBlocking {
            val sum = points.sumBy { it.await() }
            println("Your points: $sum")
        }

    }
}
