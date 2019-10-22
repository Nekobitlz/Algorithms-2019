package lesson3

import org.junit.jupiter.api.Tag
import java.util.*
import kotlin.test.*

const val ITERATIONS = 10

class BinaryTreeTest {
    private fun testAdd(create: () -> CheckableSortedSet<Int>) {
        val tree = create()
        assertEquals(0, tree.size)
        assertFalse(tree.contains(5))
        tree.add(10)
        tree.add(5)
        tree.add(7)
        tree.add(10)
        assertEquals(3, tree.size)
        assertTrue(tree.contains(5))
        tree.add(3)
        tree.add(1)
        tree.add(3)
        tree.add(4)
        assertEquals(6, tree.size)
        assertFalse(tree.contains(8))
        tree.add(8)
        tree.add(15)
        tree.add(15)
        tree.add(20)
        assertEquals(9, tree.size)
        assertTrue(tree.contains(8))
        assertTrue(tree.checkInvariant())
        assertEquals(1, tree.first())
        assertEquals(20, tree.last())
    }

    @Test
    @Tag("Example")
    fun testAddKotlin() {
        testAdd { createKotlinTree() }
    }

    @Test
    @Tag("Example")
    fun testAddJava() {
        testAdd { createJavaTree() }
    }

    private fun <T : Comparable<T>> createJavaTree(): CheckableSortedSet<T> = BinaryTree()

    private fun <T : Comparable<T>> createKotlinTree(): CheckableSortedSet<T> = KtBinaryTree()

    private fun testRemove(create: () -> CheckableSortedSet<Int>) {
        val tree = create()

        tree.add(42)
        assertFalse(tree.remove(42))

        tree.add(31)
        tree.add(32)
        assertTrue(tree.remove(31))

        tree.add(33)
        assertTrue(tree.remove(33))
        assertFalse(tree.remove(45))

        tree.add(32)
        assertTrue(tree.remove(32))
        assertFalse(tree.remove(32))

        val myList = listOf(0, 74, 60, 17, 54, 46, 29, 79, 29, 21, 34, 99, 39, 80, 52, 8, 47, 99, 20, 27)
        for (element in myList) {
            tree.add(element)
            print("$element ")
        }
        assertTrue(tree.remove(0))
        println()
        for (element in tree) {
            print("$element ")
        }
        assertEquals(
            tree.toString(),
            listOf(8, 17, 20, 21, 27, 29, 34, 39, 42, 46, 47, 52, 54, 60, 74, 79, 80, 99).toString()
        )

        val random = Random()
        for (iteration in 1..ITERATIONS) {
            val list = mutableListOf<Int>()
            for (i in 1..20) {
                list.add(random.nextInt(ITERATIONS))
            }
            val binarySet = create()
            assertFalse(binarySet.remove(42))
            for (element in list) {
                binarySet += element
            }
            val originalHeight = binarySet.height()
            val toRemove = list[random.nextInt(list.size)]
            val oldSize = binarySet.size
            assertTrue(binarySet.remove(toRemove))
            assertEquals(oldSize - 1, binarySet.size)
            println("Removing $toRemove from $list")
            for (element in list) {
                print("$element ")
                val inn = element != toRemove
                assertEquals(
                    inn, element in binarySet,
                    "$element should be ${if (inn) "in" else "not in"} tree"
                )
            }
            println()
            assertTrue(binarySet.checkInvariant(), "Binary tree invariant is false after tree.remove()")
            assertTrue(
                binarySet.height() <= originalHeight,
                "After removal of $toRemove from $list binary tree height increased"
            )
        }
    }

    @Test
    @Tag("Normal")
    fun testRemoveKotlin() {
        testRemove { createKotlinTree() }
    }

    @Test
    @Tag("Normal")
    fun testRemoveJava() {
        testRemove { createJavaTree() }
    }

    private fun testIterator(create: () -> CheckableSortedSet<Int>) {
        val mySet = create()
        val myIterator = mySet.iterator()
        assertFailsWith<NoSuchElementException> { myIterator.next() }

        val random = Random()
        for (iteration in 1..ITERATIONS) {
            val list = mutableListOf<Int>()
            for (i in 1..20) {
                list.add(random.nextInt(ITERATIONS))
            }
            val treeSet = TreeSet<Int>()
            val binarySet = create()
            assertFalse(binarySet.iterator().hasNext(), "Iterator of empty set should not have next element")
            for (element in list) {
                treeSet += element
                binarySet += element
            }
            val treeIt = treeSet.iterator()
            val binaryIt = binarySet.iterator()
            println("Traversing $list")
            while (treeIt.hasNext()) {
                assertEquals(treeIt.next(), binaryIt.next(), "Incorrect iterator state while iterating $treeSet")
            }
            val iterator1 = binarySet.iterator()
            val iterator2 = binarySet.iterator()
            println("Consistency check for hasNext $list")
            // hasNext call should not affect iterator position
            while (iterator1.hasNext()) {
                assertEquals(
                    iterator2.next(), iterator1.next(),
                    "Call of iterator.hasNext() changes its state while iterating $treeSet"
                )
            }
        }
    }

    @Test
    @Tag("Normal")
    fun testIteratorKotlin() {
        testIterator { createKotlinTree() }
    }

    @Test
    @Tag("Normal")
    fun testIteratorJava() {
        testIterator { createJavaTree() }
    }

    private fun testIteratorRemove(create: () -> CheckableSortedSet<Int>) {
        val mySet = create()
        val myIterator1 = mySet.iterator()
        assertFailsWith<NoSuchElementException> { myIterator1.remove() }

        val myList = listOf(3, 96, 15, 46, 52, 91, 47, 92, 83, 84, 80, 41, 40, 20, 90, 34, 4, 45, 66, 43)
        val myTreeSet = create()
        val myToRemove = 3
        for (element in myList) myTreeSet.add(element)
        val myIterator = myTreeSet.iterator()
        var myCounter = myTreeSet.size
        while (myIterator.hasNext()) {
            val element = myIterator.next()
            myCounter--
            print("$element ")
            if (element == myToRemove) {
                myIterator.remove()
            }
        }
        assertEquals(
            0, myCounter,
            "Iterator.remove() of $myToRemove from $myList changed iterator position: " +
                    "we've traversed a total of ${myTreeSet.size - myCounter} elements instead of ${myTreeSet.size}"
        )
        assertEquals(
            myTreeSet.toString(),
            listOf(3, 4, 15, 20, 34, 40, 41, 43, 45, 46, 47, 52, 66, 80, 83, 84, 90, 91, 92, 96).toString()
        )

        val random = Random()
        for (iteration in 1..ITERATIONS) {
            val list = mutableListOf<Int>()
            for (i in 1..20) {
                list.add(random.nextInt(ITERATIONS))
            }
            val treeSet = TreeSet<Int>()
            val binarySet = create()
            for (element in list) {
                treeSet += element
                binarySet += element
            }
            val toRemove = list[random.nextInt(list.size)]
            treeSet.remove(toRemove)
            println("Removing $toRemove from $list")
            val iterator = binarySet.iterator()
            var counter = binarySet.size
            while (iterator.hasNext()) {
                val element = iterator.next()
                counter--
                print("$element ")
                if (element == toRemove) {
                    iterator.remove()
                }
            }
            println()
            println(binarySet)
            assertEquals(
                0, counter,
                "Iterator.remove() of $toRemove from $list changed iterator position: " +
                        "we've traversed a total of ${binarySet.size - counter} elements instead of ${binarySet.size}"
            )
            println()
            assertEquals<SortedSet<*>>(treeSet, binarySet, "After removal of $toRemove from $list")
            assertEquals(treeSet.size, binarySet.size, "Size is incorrect after removal of $toRemove from $list")
            for (element in list) {
                val inn = element != toRemove
                assertEquals(
                    inn, element in binarySet,
                    "$element should be ${if (inn) "in" else "not in"} tree"
                )
            }
            assertTrue(binarySet.checkInvariant(), "Binary tree invariant is false after tree.iterator().remove()")
        }
    }

    @Test
    @Tag("Hard")
    fun testIteratorRemoveKotlin() {
        testIteratorRemove { createKotlinTree() }
    }

    @Test
    @Tag("Hard")
    fun testIteratorRemoveJava() {
        testIteratorRemove { createJavaTree() }
    }
}

/* private fun MutableList<Int>.addRandomElement(random: Random, e: Int) {
    val digit = random.nextInt(e)
    if (this.contains(digit)) this.addRandomElement(random, e)
    else this.add(digit)
}*/
