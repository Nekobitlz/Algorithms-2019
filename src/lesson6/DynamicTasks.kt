@file:Suppress("UNUSED_PARAMETER")

package lesson6

import java.util.*

/**
 * Наибольшая общая подпоследовательность.
 * Средняя
 *
 * Дано две строки, например "nematode knowledge" и "empty bottle".
 * Найти их самую длинную общую подпоследовательность -- в примере это "emt ole".
 * Подпоследовательность отличается от подстроки тем, что её символы не обязаны идти подряд
 * (но по-прежнему должны быть расположены в исходной строке в том же порядке).
 * Если общей подпоследовательности нет, вернуть пустую строку.
 * Если есть несколько самых длинных общих подпоследовательностей, вернуть любую из них.
 * При сравнении подстрок, регистр символов *имеет* значение.
 *
 * Трудоемкость: О(first.length * second.length)
 * Ресурсоемкость: O(2 * 2 * second.length) = O(second.length)
 */
fun longestCommonSubSequence(first: String, second: String): String {
    // space in order to be able to start loop from 1 and take the remainder of the division
    val firstSequence = " $first"
    val secondSequence = " $second"

    val dp = Array(2) { IntArray(second.length + 1) }
    val strDp = Array(2) { Array(second.length + 1) { "" } }

    for (i in 1..first.length) {
        for (j in 1..second.length) {
            if (firstSequence[i] == secondSequence[j]) {
                dp[i % 2][j] = dp[(i - 1) % 2][j - 1] + 1
                strDp[i % 2][j] = strDp[(i - 1) % 2][j - 1] + firstSequence[i]
            } else {
                var x: Int
                var y: Int

                if (dp[(i - 1) % 2][j] > dp[i % 2][j - 1]) {
                    x = (i - 1) % 2
                    y = j
                } else {
                    x = i % 2
                    y = j - 1
                }

                dp[i % 2][j] = dp[x][y]
                strDp[i % 2][j] = strDp[x][y]
            }
        }
    }

    return strDp[first.length % 2][second.length]
}

/**
 * Наибольшая возрастающая подпоследовательность
 * Сложная
 *
 * Дан список целых чисел, например, [2 8 5 9 12 6].
 * Найти в нём самую длинную возрастающую подпоследовательность.
 * Элементы подпоследовательности не обязаны идти подряд,
 * но должны быть расположены в исходном списке в том же порядке.
 * Если самых длинных возрастающих подпоследовательностей несколько (как в примере),
 * то вернуть ту, в которой числа расположены раньше (приоритет имеют первые числа).
 * В примере ответами являются 2, 8, 9, 12 или 2, 5, 9, 12 -- выбираем первую из них.
 *
 * Трудоемкость: O(n * k), где k - количество возрастающих подпоследовательностей
 * Ресурсоемкость: O(n * k), где k - количество возрастающих подпоследовательностей
 */
fun longestIncreasingSubSequence(list: List<Int>): List<Int> {
    val sequences = mutableListOf<LinkedList<Int>>()

    for (number in list) {
        val increasingSequence = sequences.filter { it.last < number }

        increasingSequence.map { it.addLast(number) }

        if (increasingSequence.isEmpty()) {
            val newSequence = LinkedList<Int>().apply { add(number) }

            sequences.add(newSequence)
        }
    }

    val result = sequences.maxBy { it.size } ?: LinkedList()

    if (result.size in 2 until list.size) {
        val headSequence = LinkedList<Int>()
        val firstIndex = list.indexOf(result.remove())

        for (i in 0..firstIndex) {
            val number = list[i]

            if (result.first > number && (headSequence.isEmpty() || headSequence.last < number)) {
                headSequence.addLast(number)
            }
        }

        result.addAll(0, headSequence)
    }

    return result
}

/**
 * Самый короткий маршрут на прямоугольном поле.
 * Средняя
 *
 * В файле с именем inputName задано прямоугольное поле:
 *
 * 0 2 3 2 4 1
 * 1 5 3 4 6 2
 * 2 6 2 5 1 3
 * 1 4 3 2 6 2
 * 4 2 3 1 5 0
 *
 * Можно совершать шаги длиной в одну клетку вправо, вниз или по диагонали вправо-вниз.
 * В каждой клетке записано некоторое натуральное число или нуль.
 * Необходимо попасть из верхней левой клетки в правую нижнюю.
 * Вес маршрута вычисляется как сумма чисел со всех посещенных клеток.
 * Необходимо найти маршрут с минимальным весом и вернуть этот минимальный вес.
 *
 * Здесь ответ 2 + 3 + 4 + 1 + 2 = 12
 */
fun shortestPathOnField(inputName: String): Int {
    TODO()
}

// Задачу "Максимальное независимое множество вершин в графе без циклов"
// смотрите в уроке 5