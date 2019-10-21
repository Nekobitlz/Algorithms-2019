package lesson3

import java.util.*
import kotlin.NoSuchElementException
import kotlin.math.max

// Attention: comparable supported but comparator is not
class KtBinaryTree<T : Comparable<T>> : AbstractMutableSet<T>(), CheckableSortedSet<T> {

    private var root: Node<T>? = null

    override var size = 0
        private set

    private class Node<T>(var value: T) {
        var parent: Node<T>? = null

        var left: Node<T>? = null

        var right: Node<T>? = null
    }

    override fun add(element: T): Boolean {
        val closest = find(element)
        val comparison = if (closest == null) -1 else element.compareTo(closest.value)
        if (comparison == 0) {
            return false
        }
        val newNode = Node(element)
        newNode.parent = closest
        when {
            closest == null -> root = newNode
            comparison < 0 -> {
                assert(closest.left == null)
                closest.left = newNode
            }
            else -> {
                assert(closest.right == null)
                closest.right = newNode
            }
        }
        size++
        return true
    }

    override fun checkInvariant(): Boolean =
        root?.let { checkInvariant(it) } ?: true

    override fun height(): Int = height(root)

    private fun checkInvariant(node: Node<T>): Boolean {
        val left = node.left
        if (left != null && (left.value >= node.value || !checkInvariant(left))) return false
        val right = node.right
        return right == null || right.value > node.value && checkInvariant(right)
    }

    private fun height(node: Node<T>?): Int {
        if (node == null) return 0
        return 1 + max(height(node.left), height(node.right))
    }

    /**
     * Удаление элемента в дереве
     * Средняя
     *
     * Трудоемкость - O(h)
     * Ресурсоемкость - O(1)
     */
    override fun remove(element: T): Boolean {
        val closest = find(element)

        if (closest == null || closest.value != element) return false

        val result = removeNode(root, element)

        return if (result != null) {
            size--
            true
        } else false

    }

    private fun removeNode(node: Node<T>?, value: T): Node<T>? {
        when {
            node == null -> return null
            value < node.value -> node.left = removeNode(node.left, value)
            value > node.value -> node.right = removeNode(node.right, value)
            else -> when {
                node.left == null && node.right == null -> return null
                node.left == null -> return node.right
                node.right == null -> return node.left
                else -> {
                    val minimum = findMinimum(node.right!!)
                    node.value = minimum.value
                    node.right = removeNode(node.right, minimum.value)
                }
            }
        }

        return node
    }

    private fun findMinimum(node: Node<T>): Node<T> = if (node.left == null) node else findMinimum(node.left!!)

    private fun findMaximum(node: Node<T>): Node<T> = if (node.right == null) node else findMaximum(node.right!!)

    override operator fun contains(element: T): Boolean {
        val closest = find(element)
        return closest != null && element.compareTo(closest.value) == 0
    }

    private fun find(value: T): Node<T>? =
        root?.let { find(it, value) }

    private fun find(start: Node<T>, value: T): Node<T> {
        val comparison = value.compareTo(start.value)
        return when {
            comparison == 0 -> start
            comparison < 0 -> start.left?.let { find(it, value) } ?: start
            else -> start.right?.let { find(it, value) } ?: start
        }
    }

    inner class BinaryTreeIterator internal constructor() : MutableIterator<T> {

        private var current: Node<T>? = null
        private var stack = Stack<Node<T>>()

        init {
            var node = root

            while (node != null) {
                stack.push(node)
                node = node.left
            }
        }

        /**
         * Проверка наличия следующего элемента
         * Средняя
         *
         * Трудоемкость - O(1)
         * Ресурсоемкость - O(1)
         */
        override fun hasNext(): Boolean {
            return stack.isNotEmpty()
        }

        /**
         * Поиск следующего элемента
         * Средняя
         *
         * Трудоемкость - O(n)
         * Ресурсоемкость - O(1)
         */
        override fun next(): T {
            current = stack.pop()
            var node = current

            when {
                node == null -> throw NoSuchElementException()
                node.right != null -> {
                    node = node.right

                    while (node != null) {
                        stack.push(node)
                        node = node.left
                    }
                }
            }

            return current!!.value
        }

        /**
         * Удаление следующего элемента
         * Сложная
         */
        override fun remove() {
            if (current != null) remove(current!!.value)
            else throw NoSuchElementException()
        }
    }

    override fun iterator(): MutableIterator<T> = BinaryTreeIterator()

    override fun comparator(): Comparator<in T>? = null

    /**
     * Найти множество всех элементов в диапазоне [fromElement, toElement)
     * Очень сложная
     */
    override fun subSet(fromElement: T, toElement: T): SortedSet<T> {
        TODO()
    }

    /**
     * Найти множество всех элементов меньше заданного
     * Сложная
     */
    override fun headSet(toElement: T): SortedSet<T> {
        TODO()
    }

    /**
     * Найти множество всех элементов больше или равных заданного
     * Сложная
     */
    override fun tailSet(fromElement: T): SortedSet<T> {
        TODO()
    }

    override fun first(): T {
        var current: Node<T> = root ?: throw NoSuchElementException()
        while (current.left != null) {
            current = current.left!!
        }
        return current.value
    }

    override fun last(): T {
        var current: Node<T> = root ?: throw NoSuchElementException()
        while (current.right != null) {
            current = current.right!!
        }
        return current.value
    }
}
