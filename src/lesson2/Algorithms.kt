@file:Suppress("UNUSED_PARAMETER")

package lesson2

import java.io.File

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
fun baldaSearcher(inputName: String, words: Set<String>): Set<String> {
    val lines = File(inputName).readLines()
    val result = mutableSetOf<String>()
    val matrix = mutableListOf<List<Char>>()
    val wordsSet = words.toMutableSet()

    for (i in 0 until lines.size) {
        matrix.add(lines[i].split(" ")
            .filter { it != "" }
            .map { it[0] }
        )
    }

    for (y in 0 until matrix.size) {
        for (x in 0 until matrix[y].size) {
            for (word in wordsSet) {
                if (matrix[y][x] == word[0] && matrix.containsWord(word.substring(2), word[1], Pair(x, y)))
                    result.add(word)
            }

            wordsSet.removeAll(result)
            if (wordsSet.isEmpty()) return result
        }
    }

    return result
}

fun List<List<Char>>.containsWord(
    word: String,
    char: Char,
    position: Pair<Int, Int>
): Boolean {
    val matrix = this
    val moves = listOf(1 to 0, 0 to 1, 0 to -1, -1 to 0)

    for (move in moves) {
        val x = move.first + position.first
        val y = move.second + position.second

        if (x >= 0 && x < matrix[position.second].size &&
            y >= 0 && y < matrix.size && matrix[y][x] == char
        ) {
            when {
                word.isEmpty() -> return true
                matrix.containsWord(word.substring(1), word[0], Pair(x, y)) -> return true
            }
        }
    }

    return false
}