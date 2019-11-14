@file:Suppress("UNUSED_PARAMETER", "unused")

package lesson5

import lesson5.Graph.*
import java.util.*

/**
 * Эйлеров цикл.
 * Средняя
 *
 * Дан граф (получатель). Найти по нему любой Эйлеров цикл.
 * Если в графе нет Эйлеровых циклов, вернуть пустой список.
 * Соседние дуги в списке-результате должны быть инцидентны друг другу,
 * а первая дуга в списке инцидентна последней.
 * Длина списка, если он не пуст, должна быть равна количеству дуг в графе.
 * Веса дуг никак не учитываются.
 *
 * Пример:
 *
 *      G -- H
 *      |    |
 * A -- B -- C -- D
 * |    |    |    |
 * E    F -- I    |
 * |              |
 * J ------------ K
 *
 * Вариант ответа: A, E, J, K, D, C, H, G, B, C, I, F, B, A
 *
 * Справка: Эйлеров цикл -- это цикл, проходящий через все рёбра
 * связного графа ровно по одному разу
 *
 * Трудоемкость: О(V + E)
 * Ресурсоемкость: O(V + E)
 */
fun Graph.findEulerLoop(): List<Edge> {
    if (vertices.isEmpty() || edges.isEmpty() || !hasEulerPath()) return listOf()

    val vertexStack = Stack<Vertex>()
    val resultQueue = LinkedList<Vertex>()
    val edgesList = edges

    vertexStack.push(vertices.first())

    while (vertexStack.isNotEmpty()) {
        val currentVertex = vertexStack.peek()

        for (vertex in vertices) {
            val edge = getConnection(currentVertex, vertex) ?: continue

            if (edgesList.contains(edge)) {
                vertexStack.push(vertex)
                edgesList.remove(edge)
                break
            }
        }

        if (currentVertex == vertexStack.peek()) {
            vertexStack.pop()
            resultQueue.push(currentVertex)
        }
    }

    return (0 until resultQueue.size - 1).map { getConnection(resultQueue[it], resultQueue[it + 1])!! }
}

private fun Graph.hasEulerPath(): Boolean = vertices.none { getNeighbors(it).size % 2 != 0 }

/**
 * Минимальное остовное дерево.
 * Средняя
 *
 * Дан граф (получатель). Найти по нему минимальное остовное дерево.
 * Если есть несколько минимальных остовных деревьев с одинаковым числом дуг,
 * вернуть любое из них. Веса дуг не учитывать.
 *
 * Пример:
 *
 *      G -- H
 *      |    |
 * A -- B -- C -- D
 * |    |    |    |
 * E    F -- I    |
 * |              |
 * J ------------ K
 *
 * Ответ:
 *
 *      G    H
 *      |    |
 * A -- B -- C -- D
 * |    |    |
 * E    F    I
 * |
 * J ------------ K
 */
fun Graph.minimumSpanningTree(): Graph {
    TODO()
}

/**
 * Максимальное независимое множество вершин в графе без циклов.
 * Сложная
 *
 * Дан граф без циклов (получатель), например
 *
 *      G -- H -- J
 *      |
 * A -- B -- D
 * |         |
 * C -- F    I
 * |
 * E
 *
 * Найти в нём самое большое независимое множество вершин и вернуть его.
 * Никакая пара вершин в независимом множестве не должна быть связана ребром.
 *
 * Если самых больших множеств несколько, приоритет имеет то из них,
 * в котором вершины расположены раньше во множестве this.vertices (начиная с первых).
 *
 * В данном случае ответ (A, E, F, D, G, J)
 *
 * Если на входе граф с циклами, бросить IllegalArgumentException
 *
 * Эта задача может быть зачтена за пятый и шестой урок одновременно
 *
 * Трудоемкость: O(V + E)
 * Ресурсоемкость: O(V^2)
 */
fun Graph.largestIndependentVertexSet(): Set<Vertex> {
    if (vertices.isEmpty() || edges.isEmpty()) return emptySet()

    val bridges = findBridges() // called from Bridges.kt
    require(bridges.isNotEmpty()) // check for graph with loops

    val independentSets = mutableMapOf<Vertex, Set<Vertex>>()
    val result = mutableSetOf<Vertex>()
    val unconnectedVertices = findUnconnectedVertices()

    for (vertex in unconnectedVertices) {
        result.addAll(findIndependentVertices(independentSets, parent = null, vertex = vertex))
    }

    return result
}

private fun Graph.findUnconnectedVertices(): Set<Vertex> {
    val unconnectedVertices = mutableSetOf<Vertex>()

    depthFirstSearch(vertices.first())
    unconnectedVertices.add(vertices.first())

    for (vertex in vertices) {
        if (!vertex.isVisited) {
            unconnectedVertices.add(vertex)
            depthFirstSearch(vertex)
        }
    }

    return unconnectedVertices
}

private fun Graph.depthFirstSearch(vertex: Vertex) {
    vertex.isVisited = true

    for (neighbour in getNeighbors(vertex)) {
        if (!neighbour.isVisited) depthFirstSearch(neighbour)
    }
}

private fun Graph.findIndependentVertices(
    independentSets: MutableMap<Vertex, Set<Vertex>>,
    parent: Vertex?,
    vertex: Vertex
): Set<Vertex> = independentSets.getOrPut(vertex) {
    val children = findIndependentChildren(independentSets, parent, vertex)
    val childrenOfChildren = getNeighbors(vertex)
        .filterNot { it == parent }
        .flatMap { findIndependentChildren(independentSets, vertex, it) }
        .plus(vertex)

    if (children.size - 1 > childrenOfChildren.size) children.toSet() else childrenOfChildren.toSet()
}

private fun Graph.findIndependentChildren(
    independentSets: MutableMap<Vertex, Set<Vertex>>,
    parent: Vertex?,
    vertex: Vertex
): List<Vertex> = getNeighbors(vertex)
    .filterNot { it == parent }
    .flatMap { findIndependentVertices(independentSets, vertex, it) }

/**
 * Наидлиннейший простой путь.
 * Сложная
 *
 * Дан граф (получатель). Найти в нём простой путь, включающий максимальное количество рёбер.
 * Простым считается путь, вершины в котором не повторяются.
 * Если таких путей несколько, вернуть любой из них.
 *
 * Пример:
 *
 *      G -- H
 *      |    |
 * A -- B -- C -- D
 * |    |    |    |
 * E    F -- I    |
 * |              |
 * J ------------ K
 *
 * Ответ: A, E, J, K, D, C, H, G, B, F, I
 *
 * Трудоемкость: О(V!)
 * Ресурсоемкость: O(V!)
 */
fun Graph.longestSimplePath(): Path {
    if (vertices.isEmpty() || edges.isEmpty()) return Path()

    var longestPath = Path(vertices.first())
    val deque = ArrayDeque<Path>()

    vertices.mapTo(deque) { Path(it) }

    while (deque.isNotEmpty()) {
        val current = deque.pop()
        val last = current.vertices.last()
        val neighbors = getNeighbors(last)

        if (current.length > longestPath.length) longestPath = current

        neighbors.filter { it !in current }.forEach { deque.addLast(Path(previous = current, g = this, next = it)) }
    }

    return longestPath
}