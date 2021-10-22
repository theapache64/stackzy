package com.theapache64.stackzy.util

/**
 * To get non-repetitive random items
 */
class PureRandom<T>(
    private val items: Set<T>
) {
    private val takenItems = mutableSetOf<T>()

    fun get(): T {

        if (takenItems.size >= items.size) {
            // all items taken so clear the list
            takenItems.clear()
        }

        val takenItem = items.random()
        if (takenItems.contains(takenItem)) {
            // already taken
            return get()
        }
        takenItems.add(takenItem)
        return takenItem
    }
}