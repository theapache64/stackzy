package com.theapache64.stackzy.util

/**
 * To get non-repetitive random items
 */
class PureRandom<T>(
    private val items: Set<T>
) {
    private val takenItems = mutableSetOf<T>()

    fun take(): T {

        if (takenItems.size >= items.size) {
            // all items taken so clear the list
            println("Clear!")
            takenItems.clear()
        }

        val takenItem = items.random()
        if (takenItems.contains(takenItem)) {
            // already taken
            println("Already taken")
            return take()
        }
        takenItems.add(takenItem)
        return takenItem
    }
}