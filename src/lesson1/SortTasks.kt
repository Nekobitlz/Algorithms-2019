@file:Suppress("UNUSED_PARAMETER")

package lesson1

import java.io.File
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Сортировка времён
 *
 * Простая
 * (Модифицированная задача с сайта acmp.ru)
 *
 * Во входном файле с именем inputName содержатся моменты времени в формате ЧЧ:ММ:СС AM/PM,
 * каждый на отдельной строке. См. статью википедии "12-часовой формат времени".
 *
 * Пример:
 *
 * 01:15:19 PM
 * 07:26:57 AM
 * 10:00:03 AM
 * 07:56:14 PM
 * 01:15:19 PM
 * 12:40:31 AM
 *
 * Отсортировать моменты времени по возрастанию и вывести их в выходной файл с именем outputName,
 * сохраняя формат ЧЧ:ММ:СС AM/PM. Одинаковые моменты времени выводить друг за другом. Пример:
 *
 * 12:40:31 AM
 * 07:26:57 AM
 * 10:00:03 AM
 * 01:15:19 PM
 * 01:15:19 PM
 * 07:56:14 PM
 *
 * В случае обнаружения неверного формата файла бросить любое исключение.
 *
 * Трудоемкость: О(n + n * log(n) + n) = O(n * log(n))
 * Ресурсоемкость: O(n + n) = O(n)
 */
fun sortTimes(inputName: String, outputName: String) {
    val dateList = File(inputName).readLines()

    File(outputName).bufferedWriter().use { writer ->

        val dateFormat: DateFormat = SimpleDateFormat("hh:mm:ss a", Locale.US)
        val formattedDates = mutableListOf<Date>()

        for (line in dateList) {
            val date: Date = dateFormat.parse(line)
            formattedDates.add(date)
        }

        formattedDates.sort()

        for (date in formattedDates) writer.write(dateFormat.format(date) + "\n")

        writer.close()
    }
}

/**
 * Сортировка адресов
 *
 * Средняя
 *
 * Во входном файле с именем inputName содержатся фамилии и имена жителей города с указанием улицы и номера дома,
 * где они прописаны. Пример:
 *
 * Петров Иван - Железнодорожная 3
 * Сидоров Петр - Садовая 5
 * Иванов Алексей - Железнодорожная 7
 * Сидорова Мария - Садовая 5
 * Иванов Михаил - Железнодорожная 7
 *
 * Людей в городе может быть до миллиона.
 *
 * Вывести записи в выходной файл outputName,
 * упорядоченными по названию улицы (по алфавиту) и номеру дома (по возрастанию).
 * Людей, живущих в одном доме, выводить через запятую по алфавиту (вначале по фамилии, потом по имени). Пример:
 *
 * Железнодорожная 3 - Петров Иван
 * Железнодорожная 7 - Иванов Алексей, Иванов Михаил
 * Садовая 5 - Сидоров Петр, Сидорова Мария
 *
 * В случае обнаружения неверного формата файла бросить любое исключение.
 *
 * Трудоемкость: О(n + log(n) + n) = O(log(n))
 * Ресурсоемкость: O(n + n) = O(n)
 */
fun sortAddresses(inputName: String, outputName: String) {
    val lines = File(inputName).readLines()
    val addressMap = mutableMapOf<Pair<String, Int>, SortedSet<String>>()

    File(outputName).bufferedWriter().use { writer ->
        val regex =
            Regex("""[А-ЯЁA-Z-][А-ЯЁA-Zа-яёa-z-]+ [А-ЯЁA-Z-][А-ЯЁA-Zа-яёa-z-]+ - [А-ЯЁA-Z-][А-ЯЁA-Zа-яёa-z-]+ \d+""")

        for (line in lines) {
            if (!line.matches(regex)) throw IllegalArgumentException("Wrong argument")

            val splitLine = line.split(" - ")
            val name = splitLine[0]
            val address = splitLine[1].split(" ")
            val street = address[0]
            val house = address[1].toInt()

            if (addressMap.containsKey(street to house)) addressMap.getValue(street to house).add(name)
            else addressMap[street to house] = sortedSetOf(name)
        }

        val sortedMap = addressMap.toSortedMap(compareBy<Pair<String, Int>> { it.first }.thenBy { it.second })

        for ((address, name) in sortedMap)
            writer.write("${address.first} ${address.second} - ${name.joinToString(", ")}\n")

        writer.close()
    }
}

/**
 * Сортировка температур
 *
 * Средняя
 * (Модифицированная задача с сайта acmp.ru)
 *
 * Во входном файле заданы температуры различных участков абстрактной планеты с точностью до десятых градуса.
 * Температуры могут изменяться в диапазоне от -273.0 до +500.0.
 * Например:
 *
 * 24.7
 * -12.6
 * 121.3
 * -98.4
 * 99.5
 * -12.6
 * 11.0
 *
 * Количество строк в файле может достигать ста миллионов.
 * Вывести строки в выходной файл, отсортировав их по возрастанию температуры.
 * Повторяющиеся строки сохранить. Например:
 *
 * -98.4
 * -12.6
 * -12.6
 * 11.0
 * 24.7
 * 99.5
 * 121.3
 *
 * Трудоемкость: О(n + n + (n + k) + n) = O(n + k), где k - максимальный элемент массива
 * Ресурсоемкость: O(n + n + n) = O(n)
 */
fun sortTemperatures(inputName: String, outputName: String) {
    val lines = File(inputName).readLines().filter { it.trim().isNotEmpty() }
    val tempsList = mutableListOf<Int>()

    File(outputName).bufferedWriter().use { writer ->
        val minValue = 273.0 * 10.0 // Add when sorting, because sorting doesn't count negative numbers

        if (lines.isEmpty()) {
            writer.close()
            return
        }

        for (line in lines) {
            val temp = line.toDouble()

            if (temp !in -273.0..550.0) throw IllegalArgumentException("Wrong argument")

            tempsList.add((temp * 10 + minValue).toInt())
        }

        var tempsArray = tempsList.toIntArray()
        tempsArray = countingSort(tempsArray, tempsList.max()!!)

        for (temp in tempsArray) writer.write("${(temp - minValue) / 10}\n")

        writer.close()
    }
}

/**
 * Сортировка последовательности
 *
 * Средняя
 * (Задача взята с сайта acmp.ru)
 *
 * В файле задана последовательность из n целых положительных чисел, каждое в своей строке, например:
 *
 * 1
 * 2
 * 3
 * 2
 * 3
 * 1
 * 2
 *
 * Необходимо найти число, которое встречается в этой последовательности наибольшее количество раз,
 * а если таких чисел несколько, то найти минимальное из них,
 * и после этого переместить все такие числа в конец заданной последовательности.
 * Порядок расположения остальных чисел должен остаться без изменения.
 *
 * 1
 * 3
 * 3
 * 1
 * 2
 * 2
 * 2
 *
 * Трудоемкость: О(n + n * 1 + n + n + n + n + n) = O(n) , т.к. containsKey обычно работает за O(1)
 * Ресурсоемкость: O(n + n + n + n) = O(n)
 */
fun sortSequence(inputName: String, outputName: String) {
    val lines = File(inputName).readLines().filter { it.trim().isNotEmpty() }

    File(outputName).bufferedWriter().use { writer ->
        val repetitions = mutableMapOf<Int, Int>()
        val sequence = mutableListOf<Int>()
        val maxSequence = mutableListOf<Int>()
        var minOfMax = Int.MAX_VALUE

        if (lines.isEmpty()) {
            writer.close()
            return
        }

        for (line in lines) {
            val digit = line.toInt()

            repetitions[digit] = repetitions[digit]?.inc() ?: 1
        }

        val max = repetitions.maxBy { (_, v) -> v }!!.value

        for (line in repetitions) {
            if (line.value == max && line.key < minOfMax) {
                minOfMax = line.key
            }
        }

        for (line in lines) {
            val digit = line.toInt()

            if (digit == minOfMax) maxSequence.add(digit)
            else sequence.add(digit)
        }

        sequence.addAll(maxSequence)
        for (line in sequence) writer.write("$line\n")

        writer.close()
    }
}

/**
 * Соединить два отсортированных массива в один
 *
 * Простая
 *
 * Задан отсортированный массив first и второй массив second,
 * первые first.size ячеек которого содержат null, а остальные ячейки также отсортированы.
 * Соединить оба массива в массиве second так, чтобы он оказался отсортирован. Пример:
 *
 * first = [4 9 15 20 28]
 * second = [null null null null null 1 3 9 13 18 23]
 *
 * Результат: second = [1 3 4 9 9 13 15 20 23 28]
 *
 * Трудоемкость: О(n)
 * Ресурсоемкость: O(1)
 */
fun <T : Comparable<T>> mergeArrays(first: Array<T>, second: Array<T?>) {
    var left = 0
    var right = first.size

    for (i in 0 until second.size) {
        if (left < first.size && (right == second.size || first[left] <= second[right]!!)) {
            second[i] = first[left]
            left++
        } else {
            second[i] = second[right]
            right++
        }
    }
}