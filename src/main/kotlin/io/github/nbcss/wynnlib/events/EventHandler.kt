package io.github.nbcss.wynnlib.events

interface EventHandler<T> {
    fun handle(event: T)

    abstract class HandlerList<T> {
        private val handlers: MutableList<EventHandler<T>> = mutableListOf()
        fun registerListener(handler: EventHandler<T>) {
            handlers.add(handler)
        }

        fun handleEvent(event: T) {
            for (handler in handlers) {
                handler.handle(event)
            }
        }
    }
}