package io.github.nbcss.wynnlib.utils.keys

import java.util.function.Consumer
import java.util.function.Supplier

class ToggleCallback(private val provider: Supplier<Boolean>,
                     private val consumer: Consumer<Boolean>) {
    private var state: Boolean = provider.get()

    fun tick() {
        val current = provider.get()
        if (current != state) {
            state = current
            consumer.accept(state)
        }
    }
}