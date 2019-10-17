@file:Suppress("UNUSED_PARAMETER")

package lesson2

import java.io.File
import java.util.*

/**
 * Получение наибольшей прибыли (она же -- поиск максимального подмассива)
 * Простая
 *
 * Во входном файле с именем inputName перечислены цены на акции компании в различные (возрастающие) моменты времени
 * (каждая цена идёт с новой строки). Цена -- это целое положительное число. Пример:
 *
 * 201
 * 196
 * 190
 * 198
 * 187
 * 194
 * 193
 * 185
 *
 * Выбрать два момента времени, первый из них для покупки акций, а второй для продажи, с тем, чтобы разница
 * между ценой продажи и ценой покупки была максимально большой. Второй момент должен быть раньше первого.
 * Вернуть пару из двух моментов.
 * Каждый момент обозначается целым числом -- номер строки во входном файле, нумерация с единицы.
 * Например, для приведённого выше файла результат должен быть Pair(3, 4)
 *
 * В случае обнаружения неверного формата файла бросить любое исключение.
 *
 * Трудоемкость: O(n + n) = O(n)
 * Ресурсоемкость: O(n)
 */
fun optimizeBuyAndSell(inputName: String): Pair<Int, Int> {
    val lines = File(inputName).readLines()
    var result: Pair<Int, Int> = 1 to 2
    val stocks = mutableListOf<Int>()

    for (line in lines) stocks.add(line.toInt())

    var max = -1
    var minIndex = 0

    for (i in 1 until stocks.size) {
        if (stocks[i] - stocks[minIndex] > max) {
            result = minIndex + 1 to i + 1
            max = stocks[i] - stocks[minIndex]
        } else {
            if (stocks[i] < stocks[minIndex]) minIndex = i
        }
    }

    return result
}

/**
 * Задача Иосифа Флафия.
 * Простая
 *
 * Образовав круг, стоят menNumber человек, пронумерованных от 1 до menNumber.
 *
 * 1 2 3
 * 8   4
 * 7 6 5
 *
 * Мы считаем от 1 до choiceInterval (например, до 5), начиная с 1-го человека по кругу.
 * Человек, на котором остановился счёт, выбывает.
 *
 * 1 2 3
 * 8   4
 * 7 6 х
 *
 * Далее счёт продолжается со следующего человека, также от 1 до choiceInterval.
 * Выбывшие при счёте пропускаются, и человек, на котором остановился счёт, выбывает.
 *
 * 1 х 3
 * 8   4
 * 7 6 Х
 *
 * Процедура повторяется, пока не останется один человек. Требуется вернуть его номер (в данном случае 3).
 *
 * 1 Х 3
 * х   4
 * 7 6 Х
 *
 * 1 Х 3
 * Х   4
 * х 6 Х
 *
 * х Х 3
 * Х   4
 * Х 6 Х
 *
 * Х Х 3
 * Х   х
 * Х 6 Х
 *
 * Х Х 3
 * Х   Х
 * Х х Х
 *
 * Общий комментарий: решение из Википедии для этой задачи принимается,
 * но приветствуется попытка решить её самостоятельно.
 *
 * Трудоемкость: O(n)
 * Ресурсоемкость: O(1)
 */
fun josephTask(menNumber: Int, choiceInterval: Int): Int {
    var result = 0

    for (i in 1..menNumber) {
        result = (result + choiceInterval) % i
    }

    return result + 1
}

/**
 * Наибольшая общая подстрока.
 * Средняя
 *
 * Дано две строки, например ОБСЕРВАТОРИЯ и КОНСЕРВАТОРЫ.
 * Найти их самую длинную общую подстроку -- в примере это СЕРВАТОР.
 * Если общих подстрок нет, вернуть пустую строку.
 * При сравнении подстрок, регистр символов *имеет* значение.
 * Если имеется несколько самых длинных общих подстрок одной длины,
 * вернуть ту из них, которая встречается раньше в строке first.
 *
 * Трудоемкость: O(first.length * second.length)
 * Ресурсоемкость: O(first.length * second.length)
 */
fun longestCommonSubstring(first: String, second: String): String {
    var maxLength = 0
    var endIndex = first.length
    val lengths = Array(first.length + 1) { IntArray(second.length + 1) }

    for (i in 1..first.length) {
        for (j in 1..second.length) {
            if (first[i - 1] == second[j - 1]) {
                lengths[i][j] = lengths[i - 1][j - 1] + 1

                if (lengths[i][j] > maxLength) {
                    maxLength = lengths[i][j]
                    endIndex = i
                }
            }
        }
    }

    return first.substring(endIndex - maxLength, endIndex)
}

/**
 * Число простых чисел в интервале
 * Простая
 *
 * Рассчитать количество простых чисел в интервале от 1 до limit (включительно).
 * Если limit <= 1, вернуть результат 0.
 *
 * Справка: простым считается число, которое делится нацело только на 1 и на себя.
 * Единица простым числом не считается.
 */
fun calcPrimesNumber(limit: Int): Int {
    TODO()
}

/**
 * Балда
 * Сложная
 *
 * В файле с именем inputName задана матрица из букв в следующем формате
 * (отдельные буквы в ряду разделены пробелами):
 *
 * И Т Ы Н
 * К Р А Н
 * А К В А
 *
 * В аргументе words содержится множество слов для поиска, например,
 * ТРАВА, КРАН, АКВА, НАРТЫ, РАК.
 *
 * Попытаться найти каждое из слов в матрице букв, используя правила игры БАЛДА,
 * и вернуть множество найденных слов. В данном случае:
 * ТРАВА, КРАН, АКВА, НАРТЫ
 *
 * И т Ы Н     И т ы Н
 * К р а Н     К р а н
 * А К в а     А К В А
 *
 * Все слова и буквы -- русские или английские, прописные.
 * В файле буквы разделены пробелами, строки -- переносами строк.
 * Остальные символы ни в файле, ни в словах не допускаются.
 *
 * Трудоемкость: O(n * m * words.size * word.length)
 * Ресурсоемкость: O(n * m + words.length + words.length + n * m) = O(n * m)
 */
const val SIZE = Char.MAX_VALUE.toInt()

fun baldaSearcher(inputName: String, words: Set<String>): Set<String> {
    val file = File(inputName).readLines()
    val root = TrieNode()
    val dictionary = arrayOfNulls<String>(words.size)
    val lines = ArrayList<String>()

    for (line in file) lines.add(line)

    for ((index, word) in words.withIndex()) dictionary[index] = word

    for (char in words) insertIntoTree(root, char)

    val height = lines.size
    val width = lines[0].split(" ").size
    val arrayOfLetters = Array(height) { CharArray(width) }

    for (i in 0 until height) {
        val letters = lines[i].split(" ")

        for (j in 0 until width) {
            arrayOfLetters[i][j] = letters[j][0]
        }
    }

    return findWords(arrayOfLetters, root)
}

class TrieNode {
    var child = arrayOfNulls<TrieNode>(SIZE)
    var isEnd = false

    init {
        isEnd = false

        for (i in 0 until SIZE) child[i] = null
    }
}

fun insertIntoTree(root: TrieNode, key: String) {
    var pChild = root

    for (char in key) {
        val index = char - 'A'

        if (pChild.child[index] == null)
            pChild.child[index] = TrieNode()

        pChild = pChild.child[index]!!
    }

    pChild.isEnd = true
}

fun findWords(matrix: Array<CharArray>, root: TrieNode): TreeSet<String> {
    val set = TreeSet<String>()
    val height = matrix.size
    val width = matrix[0].size
    val isVisited = Array(height) { BooleanArray(width) }
    var str = ""

    fun searchWord(
        root: TrieNode, matrix: Array<CharArray>, i: Int,
        j: Int, isVisited: Array<BooleanArray>, str: String
    ) {
        if (root.isEnd) set.add(str) //word found

        if (isValid(i, j, isVisited)) {
            isVisited[i][j] = true

            for (k in 0 until SIZE) {
                if (root.child[k] != null) {
                    val char = (k + 'A'.toInt()).toChar()

                    if (isValid(i, j + 1, isVisited) && matrix[i][j + 1] == char)
                        searchWord(root.child[k]!!, matrix, i, j + 1, isVisited, str + char)

                    if (isValid(i + 1, j, isVisited) && matrix[i + 1][j] == char)
                        searchWord(root.child[k]!!, matrix, i + 1, j, isVisited, str + char)

                    if (isValid(i, j - 1, isVisited) && matrix[i][j - 1] == char)
                        searchWord(root.child[k]!!, matrix, i, j - 1, isVisited, str + char)

                    if (isValid(i - 1, j, isVisited) && matrix[i - 1][j] == char)
                        searchWord(root.child[k]!!, matrix, i - 1, j, isVisited, str + char)
                }
            }

            isVisited[i][j] = false
        }
    }

    for (i in 0 until height) {
        for (j in 0 until width) {
            if (root.child[matrix[i][j] - 'A'] != null) {
                str += matrix[i][j]

                searchWord(
                    root.child[matrix[i][j] - 'A']!!,
                    matrix, i, j, isVisited, str
                )

                str = ""
            }
        }
    }

    return set
}

fun isValid(i: Int, j: Int, visited: Array<BooleanArray>): Boolean {
    val height = visited.size
    val width = visited[0].size

    return inRange(i, j, height, width) && !visited[i][j]
}

fun inRange(i: Int, j: Int, height: Int, width: Int): Boolean = i in 0 until height && j in 0 until width